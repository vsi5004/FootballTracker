package com.example.android.persistence.ui;

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
import com.example.android.persistence.databinding.GameFragmentBinding;
import com.example.android.persistence.db.entity.GameEntity;
import com.example.android.persistence.viewmodel.GameViewModel;

public class GameFragment extends Fragment {

    private static final String KEY_GAME_ID = "game_id";

    private GameFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GameViewModel.Factory factory = new GameViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_GAME_ID));

        final GameViewModel model = ViewModelProviders.of(this, factory)
                .get(GameViewModel.class);

        mBinding.setGameViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final GameViewModel model) {

        // Observe game data
        model.getObservableGame().observe(this, new Observer<GameEntity>() {
            @Override
            public void onChanged(@Nullable GameEntity gameEntity) {
                model.setGame(gameEntity);
            }
        });
    }

    /** Creates game fragment for specific game ID */
    public static GameFragment forGame(int gameId) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_GAME_ID, gameId);
        fragment.setArguments(args);
        Log.d("UI","Made gameFragment for Game: "+gameId);
        return fragment;
    }
}
