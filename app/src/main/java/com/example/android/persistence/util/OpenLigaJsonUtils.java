package com.example.android.persistence.util;

import android.util.Log;

import com.example.android.persistence.db.entity.TeamEntity;
import com.example.android.persistence.model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class OpenLigaJsonUtils {

    public static List<TeamEntity> parseTeams(String jsonInput) {
        List<TeamEntity> teams = new ArrayList<TeamEntity>();
        JSONArray teamsArray = null;
        try {
            teamsArray = new JSONArray(jsonInput);
            for (int i = 0; i < teamsArray.length(); i++) {
                JSONObject team = teamsArray.getJSONObject(i);
                int id = Integer.parseInt(team.getString("TeamId"));
                String name = team.getString("TeamName");
                String shortName = (team.getString("ShortName"));
                shortName = (shortName.equals("")) ? name : shortName;
                String iconURL = team.getString("TeamIconUrl");
                TeamEntity t = new TeamEntity(id, name, shortName, iconURL);
                teams.add(t);
                Log.d("DATA", "Processed " + name + " team entry");
            }
            return teams;
        } catch (JSONException e) {
            Log.e("DATA", "Error processing JSONArray: " + jsonInput);
            e.printStackTrace();
        }
        return null;
    }

    public static List<TeamEntity> initStatistics(String jsonInput, List<TeamEntity> teams, int endingGameweek) {
        JSONArray gamesArray = null;
        try {
            gamesArray = new JSONArray(jsonInput);
            int currentGameweek = 0;
            int i = 0;
            while(currentGameweek<=endingGameweek) {
                JSONObject game = gamesArray.getJSONObject(i);
                if (game.getBoolean("MatchIsFinished")) {
                    int team1id = game.getJSONObject("Team1").getInt("TeamId");
                    int team2id = game.getJSONObject("Team2").getInt("TeamId");
                    JSONArray results = game.getJSONArray("MatchResults");
                    JSONObject finalResult = results.getJSONObject(1);
                    int team1Score = finalResult.getInt("PointsTeam1");
                    int team2Score = finalResult.getInt("PointsTeam2");
                    teams = updateTeamStats(teams, team1id, team2id, team1Score, team2Score);
                }
                currentGameweek = game.getJSONObject("Group").getInt("GroupOrderID");
                i++;
            }
        } catch (JSONException e) {
            Log.e("DATA", "Error processing JSONArray: " + jsonInput);
            e.printStackTrace();
        }
        return teams;
    }

    private static List<TeamEntity> updateTeamStats(List<TeamEntity> teams, int team1id, int team2id, int team1Score, int team2Score) {
        TeamEntity team1 = teams.stream().filter(item -> item.getId() == team1id).findFirst().get();
        TeamEntity team2 = teams.stream().filter(item -> item.getId() == team2id).findFirst().get();
        team1.addGoalsScored(team1Score);
        team1.addGoalsConceded(team2Score);
        team2.addGoalsScored(team2Score);
        team2.addGoalsConceded(team1Score);

        if (team1Score > team2Score) {
            team1.incrementWins();
            team2.incrementLosses();
        } else if (team1Score < team2Score) {
            team1.incrementLosses();
            team2.incrementWins();

        } else {
            team1.incrementTies();
            team2.incrementTies();
        }
        return teams;
    }

    public static int getGameweekNumber(String jsonInput) {
        try {
            JSONArray gamesArray = new JSONArray(jsonInput);
            JSONObject game = gamesArray.getJSONObject(0);
            JSONObject group = game.getJSONObject("Group");
            return group.getInt("GroupOrderID");
        } catch (JSONException e) {
            Log.e("DATA", "Error processing JSONArray: " + jsonInput);
            e.printStackTrace();
        }
        return 0;
    }

}
