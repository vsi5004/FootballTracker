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

package com.example.android.persistence.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.example.android.persistence.model.Game;
import com.example.android.persistence.model.Team;


@Entity(tableName = "games",
        foreignKeys = {
                @ForeignKey(entity = TeamEntity.class,
                        parentColumns = "id",
                        childColumns = "team1Id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = TeamEntity.class,
                        parentColumns = "id",
                        childColumns = "team2Id",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "team1Id"),@Index(value = "team2Id")
        })
public class GameEntity implements Game {

    @PrimaryKey
    private int id;
    private int team1Id;
    private int team2Id;
    private String time;
    private int team1Score;
    private int team2Score;
    private int gameWeek;
    private boolean isFinished;

    public GameEntity(int id, int team1Id, int team2Id, String time, int gameWeek, boolean isFinished) {
        this.id = id;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.time = time;
        this.gameWeek = gameWeek;
        this.isFinished = isFinished;
    }

    public GameEntity() {
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(int team1Id) {
        this.team1Id = team1Id;
    }

    @Override
    public int getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(int team2Id) {
        this.team2Id = team2Id;
    }

    @Override
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
    public int getGameWeek() {
        return gameWeek;
    }

    public void setGameWeek(int gameWeek) {
        this.gameWeek = gameWeek;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean finished) {
        isFinished = finished;
    }
}
