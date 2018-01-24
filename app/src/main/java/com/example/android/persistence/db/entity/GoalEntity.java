package com.example.android.persistence.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.example.android.persistence.model.Goal;

@Entity(tableName = "goals",
        indices = {@Index(value = "gameId")
        })
public class GoalEntity implements Goal {
    @PrimaryKey
    private int id;
    private int gameId;
    private int team1Score;
    private int team2Score;
    private String scorerName;
    private int matchMinute;

    @Ignore
    public GoalEntity(int id, int gameId, int team1Score, int team2Score, String scorerName, int matchMinute) {
        this.id = id;
        this.gameId = gameId;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.scorerName = scorerName;
        this.matchMinute = matchMinute;
    }

    public GoalEntity() {
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    @Override
    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    @Override
    public String getScorerName() {
        return scorerName;
    }

    public void setScorerName(String scorerName) {
        this.scorerName = scorerName;
    }

    @Override
    public int getMatchMinute() {
        return matchMinute;
    }

    public void setMatchMinute(int matchMinute) {
        this.matchMinute = matchMinute;
    }
}
