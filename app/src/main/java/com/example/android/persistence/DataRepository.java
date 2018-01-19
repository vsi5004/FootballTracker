package com.example.android.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.android.persistence.db.AppDatabase;
import com.example.android.persistence.db.entity.TeamEntity;

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
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<TeamEntity>> getTeams() {
        return mObservableTeams;
    }

    public LiveData<TeamEntity> loadTeam(final int teamId) {
        return mDatabase.teamDao().loadTeam(teamId);
    }
}
