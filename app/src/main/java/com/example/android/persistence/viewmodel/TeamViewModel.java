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
import com.example.android.persistence.db.entity.TeamEntity;

public class TeamViewModel extends AndroidViewModel {

    private final LiveData<TeamEntity> mObservableTeam;

    public ObservableField<TeamEntity> team = new ObservableField<>();

    private final int mTeamId;

    public TeamViewModel(@NonNull Application application, DataRepository repository,
                         final int teamId) {
        super(application);
        mTeamId = teamId;

        mObservableTeam = repository.loadTeam(mTeamId);
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */

    public LiveData<TeamEntity> getObservableProduct() {
        return mObservableTeam;
    }

    public void setTeam(TeamEntity team) {
        this.team.set(team);
    }

    /**
     * A creator is used to inject the team ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the team ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mTeamId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int teamId) {
            mApplication = application;
            mTeamId = teamId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TeamViewModel(mApplication, mRepository, mTeamId);
        }
    }
}
