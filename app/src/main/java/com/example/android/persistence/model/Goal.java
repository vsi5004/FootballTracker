package com.example.android.persistence.model;

/**
 * Created by isirc on 1/24/2018.
 */

public interface Goal {
    int getId();
    int getGameId();
    int getTeam1Score();
    int getTeam2Score();
    String getScorerName();
    int getMatchMinute();
}
