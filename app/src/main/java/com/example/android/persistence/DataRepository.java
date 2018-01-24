package com.example.android.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.example.android.persistence.db.AppDatabase;
import com.example.android.persistence.db.entity.GameEntity;
import com.example.android.persistence.db.entity.GoalEntity;
import com.example.android.persistence.db.entity.MatchdayEntity;
import com.example.android.persistence.db.entity.TeamEntity;
import com.example.android.persistence.util.NetworkUtils;
import com.example.android.persistence.util.OpenLigaJsonUtils;

import java.util.List;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<TeamEntity>> mObservableTeams;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableTeams = new MediatorLiveData<>();
        //mObservableGames = new MediatorLiveData<>();

        mObservableTeams.addSource(mDatabase.teamDao().loadAllTeams(),
                teamEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableTeams.postValue(teamEntities);
                    }
                });

    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of Teams from the database and get notified when the data changes.
     */
    public LiveData<List<TeamEntity>> getTeams() {
        return mObservableTeams;
    }

    //public LiveData<List<GameEntity>> getGames() {
    //    return mObservableGames;
    //}

    public LiveData<TeamEntity> loadTeam(final int teamId) {
        return mDatabase.teamDao().loadTeam(teamId);
    }

    public LiveData<GameEntity> loadGame(final int gameId) {
        Log.d("DATA", "DataRepoReturned Game: " + gameId);
        return mDatabase.gameDao().loadGame(gameId);
    }

    public LiveData<List<GameEntity>> loadGames(final int teamId) {
        return mDatabase.gameDao().loadGames(teamId);
    }

    public LiveData<List<GameEntity>> getGames(final int gameweekNumber) {
        return mDatabase.gameDao().loadGameweekGames(gameweekNumber);
    }

    public MatchdayEntity getMatchday() {
        return mDatabase.matchdayDao().loadMatchday();
    }

    public LiveData<List<GoalEntity>> loadGoals(int mGameId) {
        return mDatabase.goalDao().loadGoals(mGameId);
    }
}
