/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.persistence.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.example.android.persistence.AppExecutors;
import com.example.android.persistence.db.converter.DateConverter;
import com.example.android.persistence.db.dao.GameDao;
import com.example.android.persistence.db.dao.GoalDao;
import com.example.android.persistence.db.dao.MatchdayDao;
import com.example.android.persistence.db.dao.TeamDao;
import com.example.android.persistence.db.entity.GameEntity;
import com.example.android.persistence.db.entity.GoalEntity;
import com.example.android.persistence.db.entity.MatchdayEntity;
import com.example.android.persistence.db.entity.TeamEntity;
import com.example.android.persistence.util.NetworkUtils;
import com.example.android.persistence.util.OpenLigaJsonUtils;

import java.util.HashMap;
import java.util.List;

@Database(entities = {TeamEntity.class, GameEntity.class, MatchdayEntity.class, GoalEntity.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "basic-sample-db";

    public abstract TeamDao teamDao();
    public abstract GameDao gameDao();
    public abstract MatchdayDao matchdayDao();
    public abstract GoalDao goalDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            // Generate the data for pre-population
                            AppDatabase database = AppDatabase.getInstance(appContext, executors);

                            String allTeamsData = NetworkUtils.runQuery(NetworkUtils.QUERY_AVAILABLE_TEAMS);
                            String allMatchesData = NetworkUtils.runQuery(NetworkUtils.QUERY_ALL_GAMES);
                            List<TeamEntity> teams = OpenLigaJsonUtils.parseTeams(allTeamsData);
                            HashMap data = OpenLigaJsonUtils.parseGames(allMatchesData,teams);
                            int currentMatchday = OpenLigaJsonUtils.getGameweekNumber(NetworkUtils.runQuery(NetworkUtils.QUERY_CURRENT_MATCHDAY_GAMES));
                            MatchdayEntity currMatchday = new MatchdayEntity(currentMatchday);
                            insertData(database, (List<TeamEntity>)data.get("teams"),(List<GameEntity>)data.get("games"), (List<GoalEntity>)data.get("goals"), currMatchday);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();

                        });
                    }
                }).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData(final AppDatabase database, final List<TeamEntity> teams, final List<GameEntity> games, final List<GoalEntity> goals, final MatchdayEntity matchday) {
        database.runInTransaction(() -> {
            database.teamDao().insertAll(teams);
            database.gameDao().insertAll(games);
            database.goalDao().insertAll(goals);
            database.matchdayDao().insert(matchday);
        });
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
