package com.example.android.persistence.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.persistence.R;
import com.example.android.persistence.databinding.GoalItemBinding;
import com.example.android.persistence.model.Goal;

import java.util.List;
import java.util.Objects;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private List<? extends Goal> mGoalList;

    public GoalAdapter() {
    }

    public void setGoalList(final List<? extends Goal> goals) {
        if (mGoalList == null) {
            mGoalList = goals;
            notifyItemRangeInserted(0, goals.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mGoalList.size();
                }

                @Override
                public int getNewListSize() {
                    return goals.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Goal old = mGoalList.get(oldItemPosition);
                    Goal goal = goals.get(newItemPosition);
                    return old.getId() == goal.getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Goal old = mGoalList.get(oldItemPosition);
                    Goal goal = goals.get(newItemPosition);
                    return old.getId() == goal.getId()
                            && old.getGameId() == goal.getGameId()
                            && old.getMatchMinute() == goal.getMatchMinute()
                            && Objects.equals(old.getScorerName(), goal.getScorerName());
                }
            });
            mGoalList = goals;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GoalItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.goal_item,
                        parent, false);
        return new GoalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GoalViewHolder holder, int position) {
        holder.binding.setGoal(mGoalList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mGoalList == null ? 0 : mGoalList.size();
    }

    static class GoalViewHolder extends RecyclerView.ViewHolder {

        final GoalItemBinding binding;

        GoalViewHolder(GoalItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}