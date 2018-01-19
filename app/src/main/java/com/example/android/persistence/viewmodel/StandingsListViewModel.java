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
import android.arch.lifecycle.MediatorLiveData;

import com.example.android.persistence.BasicApp;
import com.example.android.persistence.db.entity.TeamEntity;

import java.util.List;

public class StandingsListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<TeamEntity>> mObservableTeams;

    public StandingsListViewModel(Application application) {
        super(application);

        mObservableTeams = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableTeams.setValue(null);

        LiveData<List<TeamEntity>> teams = ((BasicApp) application).getRepository()
                .getTeams();

        // observe the changes of the teams from the database and forward them
        mObservableTeams.addSource(teams, mObservableTeams::setValue);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<TeamEntity>> getTeams() {
        return mObservableTeams;
    }
}
