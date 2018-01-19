package com.example.android.persistence.util;

import android.util.Log;

import com.example.android.persistence.db.entity.GameEntity;
import com.example.android.persistence.db.entity.TeamEntity;
import com.example.android.persistence.model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class OpenLigaJsonUtils {

    public static List<TeamEntity> parseTeams(String jsonInput) {
        List<TeamEntity> teams = new ArrayList<>();
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
                teams.add(new TeamEntity(id, name, shortName, iconURL));
                Log.d("DATA", "Processed " + name + " team entry");
            }
            return teams;
        } catch (JSONException e) {
            Log.e("DATA", "Error processing JSONArray: " + jsonInput);
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<String, List> parseGames(String jsonInput, List<TeamEntity> teams) {
        List<GameEntity> games = new ArrayList<>();
        HashMap<String, List> data = new HashMap();
        data.put("teams", teams);
        data.put("games", games);
        JSONArray gamesArray = null;
        try {
            gamesArray = new JSONArray(jsonInput);
            for (int i = 0; i < gamesArray.length(); i++) {
                JSONObject gameJSON = gamesArray.getJSONObject(i);
                int currentGameweek = gameJSON.getJSONObject("Group").getInt("GroupOrderID");
                int gameId = gameJSON.getInt("MatchID");
                int team1id = gameJSON.getJSONObject("Team1").getInt("TeamId");
                int team2id = gameJSON.getJSONObject("Team2").getInt("TeamId");
                boolean isFinished = gameJSON.getBoolean("MatchIsFinished");
                String gameTime = gameJSON.getString("MatchDateTimeUTC");
                GameEntity game = new GameEntity(gameId, team1id, team2id, gameTime, currentGameweek, isFinished);
                if (isFinished) {
                    JSONArray results = gameJSON.getJSONArray("MatchResults");
                    JSONObject finalResult = results.getJSONObject(1);
                    int team1Score = finalResult.getInt("PointsTeam1");
                    int team2Score = finalResult.getInt("PointsTeam2");
                    teams = updateTeamStats(teams, team1id, team2id, team1Score, team2Score);
                    game.setTeam1Score(team1Score);
                    game.setTeam2Score(team2Score);
                }
                games.add(game);
            }
        } catch (JSONException e) {
            Log.e("DATA", "Error processing JSONArray: " + jsonInput);
            e.printStackTrace();
        }
        return data;
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
