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
import android.support.annotation.NonNull;

import com.example.android.persistence.model.Game;
import com.example.android.persistence.model.Team;

import java.text.SimpleDateFormat;
import java.util.Date;


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
        indices = {@Index(value = "team1Id"), @Index(value = "team2Id")
        })
public class GameEntity implements Game, Comparable {

    @PrimaryKey
    private int id;
    private int team1Id;
    private String team1Name;
    private String team1Icon;
    private int team2Id;
    private String team2Name;
    private String team2Icon;
    private Date time;
    private Date lastEdited;
    private int team1Score;
    private int team2Score;
    private int gameWeek;
    private boolean isFinished;

    @Ignore
    public GameEntity(int id, int team1Id, String team1Name, String team1Icon, int team2Id, String team2Name, String team2Icon, Date time, int gameWeek, boolean isFinished) {
        this.id = id;
        this.team1Id = team1Id;
        this.team1Name = team1Name;
        this.team1Icon = team1Icon;
        this.team2Id = team2Id;
        this.team2Name = team2Name;
        this.team2Icon = team2Icon;
        this.time = time;
        this.gameWeek = gameWeek;
        this.isFinished = isFinished;
        this.lastEdited = new Date();
    }

    public GameEntity() {
    }

    @Override
    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    @Override
    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    @Override
    public String getTeam1Icon() {
        return team1Icon;
    }

    public void setTeam1Icon(String team1Icon) {
        this.team1Icon = team1Icon;
    }

    @Override
    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    @Override
    public String getTeam2Icon() {
        return team2Icon;
    }

    public void setTeam2Icon(String team2Icon) {
        this.team2Icon = team2Icon;
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
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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

    @Override
    public int compareTo(@NonNull Object o) {
        return ((GameEntity) o).getTime().compareTo(time);

    }

    public String getWeekdayFormatted(){
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        String dateString = formatter.format(time);
        return dateString;
    }

    public String getDateFormatted(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        String dateString = formatter.format(time);
        return dateString;
    }

    public String getTimeFormatted(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        String dateString = formatter.format(time);
        return dateString;
    }
}
