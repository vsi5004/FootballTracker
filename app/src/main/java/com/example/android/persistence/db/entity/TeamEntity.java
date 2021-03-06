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
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.android.persistence.model.Team;


@Entity(indices = {@Index(value = "name",
        unique = true)}, tableName = "teams")
public class TeamEntity implements Team, Comparable {

    @PrimaryKey
    private final int id;

    private String name;

    private String shortName;

    private String iconName;

    private int standing;

    private int points;

    private int wins;

    private int losses;

    private int ties;

    private int goalsScored;

    private int goalsConceded;

    //public TeamEntity(){
    //}

    @Ignore
    public TeamEntity(int id, String name, String shortName, String iconURL) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        //TODO fix this to check if icon exists in location, otherwise download it
        this.iconName = "android.resource://com.example.android.persistence/drawable/" + shortName.replaceAll("[^a-zA-Z]", "").toLowerCase();
        this.standing = 0;
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
        this.points = 0;
    }

    public TeamEntity(int id, String name, String shortName, String iconName, int standing, int wins, int losses, int ties, int goalsScored, int goalsConceded) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.iconName = iconName;
        this.standing = standing;
        this.wins = wins;
        this.losses = losses;
        this.ties = ties;
        this.goalsScored = goalsScored;
        this.goalsConceded = goalsConceded;
        this.points = getPoints();
    }

    public int getGamesPlayed() {
        return wins + losses + ties;
    }

    public int getPoints() {
        return 3 * wins + ties;
    }

    public void setPoints(int points) {
        this.points = getPoints();
    }

    public int getGoalDifference() {
        return goalsScored - goalsConceded;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public int getStanding() {
        return standing;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTies() {
        return ties;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setStanding(int standing) {
        this.standing = standing;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded = goalsConceded;
    }

    @Override
    public int getId() {
        return id;
    }

    public void addGoalsScored(int numGoals) {
        goalsScored += numGoals;
    }

    public void addGoalsConceded(int numGoals) {
        goalsConceded += numGoals;
    }

    public void incrementWins() {
        wins += 1;
    }

    public void incrementLosses() {
        losses += 1;
    }

    public void incrementTies() {
        ties += 1;
    }

    @Override
    public int compareTo(@NonNull Object o) {

        if (((TeamEntity) o).getPoints() > getPoints()) {
            return 1;
        }
        if (((TeamEntity) o).getPoints() == getPoints()) {
            if(((TeamEntity) o).getGoalDifference() > getGoalDifference()){
                return 1;
            }
            if(((TeamEntity) o).getGoalDifference() < getGoalDifference()){
                return -1;
            }
            else {
                return 0;
            }
        } else {
            return -1;
        }
    }

}
