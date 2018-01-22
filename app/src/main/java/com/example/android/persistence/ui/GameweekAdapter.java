package com.example.android.persistence.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.persistence.R;
import com.example.android.persistence.databinding.GameItemBinding;
import com.example.android.persistence.model.Game;

import java.util.List;
import java.util.Objects;

public class GameweekAdapter extends RecyclerView.Adapter<GameweekAdapter.GameViewHolder> {

    List<? extends Game> mGameList;

    @Nullable
    private final GameClickCallback mGameClickCallback;

    public GameweekAdapter(@Nullable GameClickCallback clickCallback) {
        mGameClickCallback = clickCallback;
    }

    public void setGameList(final List<? extends Game> gameList) {
        if (mGameList == null) {
            mGameList = gameList;
            notifyItemRangeInserted(0, gameList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mGameList.size();
                }

                @Override
                public int getNewListSize() {
                    return gameList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mGameList.get(oldItemPosition).getId() ==
                            gameList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Game newGame = gameList.get(newItemPosition);
                    Game oldGame = mGameList.get(oldItemPosition);
                    return newGame.getId() == oldGame.getId()
                            && oldGame.getTeam1Score() == newGame.getTeam1Score()
                            && oldGame.getTeam2Score() == newGame.getTeam2Score()
                            && oldGame.getIsFinished() == newGame.getIsFinished();
                }
            });
            mGameList = gameList;
            result.dispatchUpdatesTo(this);
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
        holder.binding.setGame(mGameList.get(position));
        Log.d("UI","Set game binding at index"+position);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        int count = (mGameList == null) ? 0 : mGameList.size();
        Log.d("UI","Returned game item count of "+count);
        return count;
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {

        final GameItemBinding binding;

        public GameViewHolder(GameItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}