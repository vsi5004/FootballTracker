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
import com.example.android.persistence.databinding.TeamFragmentBinding;
import com.example.android.persistence.db.entity.GameEntity;
import com.example.android.persistence.db.entity.TeamEntity;
import com.example.android.persistence.model.Game;
import com.example.android.persistence.viewmodel.TeamViewModel;

import java.util.List;

public class StandingsFragment extends Fragment {

    private static final String KEY_TEAM_ID = "team_id";

    private TeamFragmentBinding mBinding;
    private GameAdapter mGameAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.team_fragment, container, false);
        mGameAdapter = new GameAdapter(mGameClickCallback);
        mBinding.gameList.setAdapter(mGameAdapter);

        return mBinding.getRoot();
    }

    private final GameClickCallback mGameClickCallback = new GameClickCallback() {
        @Override
        public void onClick(Game game) {
            // no-op

        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TeamViewModel.Factory factory = new TeamViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_TEAM_ID));

        final TeamViewModel model = ViewModelProviders.of(this, factory)
                .get(TeamViewModel.class);

        mBinding.setTeamViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final TeamViewModel model) {

        // Observe team data
        model.getObservableProduct().observe(this, new Observer<TeamEntity>() {
            @Override
            public void onChanged(@Nullable TeamEntity teamEntity) {
                model.setTeam(teamEntity);
            }
        });

        // Observe games
        model.getGames().observe(this, new Observer<List<GameEntity>>() {
            @Override
            public void onChanged(@Nullable List<GameEntity> gameEntities) {
                if (gameEntities != null) {
                    mBinding.setIsLoading(false);
                    mGameAdapter.setGameList(gameEntities);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }

    /** Creates team fragment for specific team ID */
    public static StandingsFragment forTeam(int teamId) {
        StandingsFragment fragment = new StandingsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TEAM_ID, teamId);
        fragment.setArguments(args);
        return fragment;
    }
}
