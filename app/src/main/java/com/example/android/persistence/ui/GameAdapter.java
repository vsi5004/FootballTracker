
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

import com.example.android.persistence.databinding.GameItemBinding;
import com.example.android.persistence.db.entity.GameEntity;
import com.example.android.persistence.model.Game;
import com.example.android.persistence.R;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<? extends Game> mTeamGameList;

    @Nullable
    private final GameClickCallback mGameClickCallback;

    public GameAdapter(@Nullable GameClickCallback gameClickCallback) {
        mGameClickCallback = gameClickCallback;
    }

    public void setGameList(final List<? extends Game> games) {

        if (mTeamGameList == null) {

            mTeamGameList = games;
            notifyItemRangeInserted(0, games.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mTeamGameList.size();
                }

                @Override
                public int getNewListSize() {
                    return games.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Game old = mTeamGameList.get(oldItemPosition);
                    Game game = games.get(newItemPosition);
                    return old.getId() == game.getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Game old = mTeamGameList.get(oldItemPosition);
                    Game game = games.get(newItemPosition);
                    return old.getId() == game.getId()
                            && old.getTeam1Score() == game.getTeam1Score()
                            && old.getTeam2Score() == game.getTeam2Score()
                            && old.getIsFinished() == game.getIsFinished();
                }
            });
            mTeamGameList = games;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GameItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.game_item,
                        parent, false);
        binding.setCallback(mGameClickCallback);
        return new GameViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        holder.binding.setGame(mTeamGameList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mTeamGameList == null ? 0 : mTeamGameList.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {

        final GameItemBinding binding;

        GameViewHolder(GameItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}