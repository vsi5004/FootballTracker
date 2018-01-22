package com.example.android.persistence.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.android.persistence.AppExecutors;
import com.example.android.persistence.BasicApp;
import com.example.android.persistence.db.entity.GameEntity;
import com.example.android.persistence.db.entity.MatchdayEntity;
import com.example.android.persistence.model.Matchday;

import java.util.List;

public class GameweekListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<GameEntity>> mObservableGames;
    private final MutableLiveData<MatchdayEntity> mObservableMatchday;

    public GameweekListViewModel(Application application) {
        super(application);

        mObservableGames = new MediatorLiveData<>();
        mObservableMatchday = new MutableLiveData<>();
        // set by default null, until we get data from the database.
        mObservableGames.setValue(null);
        mObservableMatchday.setValue(null);

        AppExecutors executors = new AppExecutors();
        executors.diskIO().execute(() -> {
            MatchdayEntity currentMatchday = ((BasicApp) application).getRepository().getMatchday();
            mObservableMatchday.postValue(currentMatchday);
            LiveData<List<GameEntity>> games = ((BasicApp) application).getRepository().getGames(currentMatchday.getId());

            // observe the changes of the games from the database and forward them
            mObservableGames.addSource(games, mObservableGames::postValue);
        });

    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<GameEntity>> getGames() {
        return mObservableGames;
    }
    public LiveData<MatchdayEntity> getMatchday() {
        return mObservableMatchday;
    }
}