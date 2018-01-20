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

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.persistence.R;
import com.example.android.persistence.databinding.StandingsTeamBinding;
import com.example.android.persistence.model.Team;

import java.util.List;
import java.util.Objects;

public class StandingsAdapter extends RecyclerView.Adapter<StandingsAdapter.TeamViewHolder> {

    List<? extends Team> mTeamList;

    @Nullable
    private final TeamClickCallback mTeamClickCallback;

    public StandingsAdapter(@Nullable TeamClickCallback clickCallback) {
        mTeamClickCallback = clickCallback;
    }

    public void setTeamList(final List<? extends Team> teamList) {
        if (mTeamList == null) {
            mTeamList = teamList;
            notifyItemRangeInserted(0, teamList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mTeamList.size();
                }

                @Override
                public int getNewListSize() {
                    return teamList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mTeamList.get(oldItemPosition).getId() ==
                            teamList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Team newTeam = teamList.get(newItemPosition);
                    Team oldTeam = mTeamList.get(oldItemPosition);
                    return newTeam.getId() == oldTeam.getId()
                            && Objects.equals(newTeam.getName(), oldTeam.getName());
                }
            });
            mTeamList = teamList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StandingsTeamBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.standings_team,
                        parent, false);
        binding.setCallback(mTeamClickCallback);
        return new TeamViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        mTeamList.get(position).setStanding(position+1);
        holder.binding.setTeam(mTeamList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mTeamList == null ? 0 : mTeamList.size();
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {

        final StandingsTeamBinding binding;

        public TeamViewHolder(StandingsTeamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
