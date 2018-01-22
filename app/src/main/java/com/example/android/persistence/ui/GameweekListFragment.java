package com.example.android.persistence.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.persistence.R;
import com.example.android.persistence.databinding.GameweekFragmentBinding;
import com.example.android.persistence.db.entity.GameEntity;
import com.example.android.persistence.db.entity.MatchdayEntity;
import com.example.android.persistence.model.Game;
import com.example.android.persistence.model.Matchday;
import com.example.android.persistence.viewmodel.GameweekListViewModel;

import java.util.List;


public class GameweekListFragment extends Fragment {

    public static final String TAG = "GameweekListViewModel";

    private GameweekAdapter mGameWeekAdapter;

    private GameweekFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.gameweek_fragment, container, false);

        mGameWeekAdapter = new GameweekAdapter(mGameClickCallback);
        mBinding.gamesList.setAdapter(mGameWeekAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final GameweekListViewModel viewModel =
                ViewModelProviders.of(this).get(GameweekListViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(GameweekListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getGames().observe(this, new Observer<List<GameEntity>>() {
            @Override
            public void onChanged(@Nullable List<GameEntity> myGames) {
                if (myGames != null) {
                    mBinding.setIsLoading(false);
                    mGameWeekAdapter.setGameList(myGames);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
        viewModel.getMatchday().observe(this, new Observer<MatchdayEntity>() {
            @Override
            public void onChanged(@Nullable MatchdayEntity matchday) {
                if (matchday != null) {
                    mBinding.setIsLoading(false);
                    mBinding.setGameweek(matchday.getName());
                } else {
                    mBinding.setIsLoading(true);
                }
                mBinding.executePendingBindings();
            }
        });
    }

    private final GameClickCallback mGameClickCallback = new GameClickCallback() {
        @Override
        public void onClick(Game game) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((GameweekActivity)getActivity()).show(game);
            }
        }
    };
}
