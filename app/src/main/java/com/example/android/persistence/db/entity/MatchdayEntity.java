package com.example.android.persistence.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.android.persistence.model.Matchday;


@Entity(tableName = "matchdays")
public class MatchdayEntity implements Matchday{

    @PrimaryKey
    private int id;
    private String name;

    @Ignore
    public MatchdayEntity(int id) {
        this.id = id;
        this.name = "Matchday "+id;
    }

    public MatchdayEntity() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName(){
        return name;
    }
}
