package com.example.android.persistence.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.android.persistence.BasicApp;
import com.example.android.persistence.DataRepository;
import com.example.android.persistence.db.entity.GameEntity;

public class GameViewModel extends AndroidViewModel {

    private final LiveData<GameEntity> mObservableGame;

    public ObservableField<GameEntity> game = new ObservableField<>();

    private final int mGameId;

    public GameViewModel(@NonNull Application application, DataRepository repository,
                         final int gameId) {
        super(application);
        mGameId = gameId;

        mObservableGame = repository.loadGame(mGameId);
    }

    public LiveData<GameEntity> getObservableGame() {
        return mObservableGame;
    }

    public void setGame(GameEntity game) {
        this.game.set(game);
    }

    /**
     * A creator is used to inject the game ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the game ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mGameId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int productId) {
            mApplication = application;
            mGameId = productId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new GameViewModel(mApplication, mRepository, mGameId);
        }
    }
}