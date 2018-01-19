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

package com.example.android.persistence.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.persistence.R;
import com.example.android.persistence.databinding.StandingsFragmentBinding;
import com.example.android.persistence.db.entity.TeamEntity;
import com.example.android.persistence.model.Team;
import com.example.android.persistence.viewmodel.StandingsListViewModel;

import java.util.List;

public class TeamListFragment extends Fragment {

    public static final String TAG = "StandingsListViewModel";

    private StandingsAdapter mStandingsAdapter;
    //private ListFragmentBinding
    private StandingsFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.standings_fragment, container, false);

        mStandingsAdapter = new StandingsAdapter(mTeamClickCallback);
        mBinding.teamsList.setAdapter(mStandingsAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final StandingsListViewModel viewModel =
                ViewModelProviders.of(this).get(StandingsListViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(StandingsListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getTeams().observe(this, new Observer<List<TeamEntity>>() {
            @Override
            public void onChanged(@Nullable List<TeamEntity> teams) {
                if (teams != null) {
                    mBinding.setIsLoading(false);
                    mStandingsAdapter.setTeamList(teams);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }

    private final TeamClickCallback mTeamClickCallback = new TeamClickCallback() {
        @Override
        public void onClick(Team team) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(team);
            }
        }
    };
}
