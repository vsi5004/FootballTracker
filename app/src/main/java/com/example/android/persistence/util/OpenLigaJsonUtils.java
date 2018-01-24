package com.example.android.persistence.util;

import android.util.Log;

import com.example.android.persistence.db.converter.DateConverter;
import com.example.android.persistence.db.entity.GameEntity;
import com.example.android.persistence.db.entity.GoalEntity;
import com.example.android.persistence.db.entity.TeamEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
        List<GoalEntity> goals = new ArrayList<>();
        HashMap<String, List> data = new HashMap();
        data.put("teams", teams);
        data.put("games", games);
        data.put("goals", goals);
        JSONArray gamesArray = null;
        try {
            gamesArray = new JSONArray(jsonInput);
            for (int i = 0; i < gamesArray.length(); i++) {
                JSONObject gameJSON = gamesArray.getJSONObject(i);
                int currentGameweek = gameJSON.getJSONObject("Group").getInt("GroupOrderID");
                int gameId = gameJSON.getInt("MatchID");
                int team1id = gameJSON.getJSONObject("Team1").getInt("TeamId");
                int team2id = gameJSON.getJSONObject("Team2").getInt("TeamId");
                TeamEntity team1 = getTeamById(team1id, teams);
                String team1Name = team1.getShortName();
                String team1Icon = team1.getIconName();
                TeamEntity team2 = getTeamById(team2id, teams);
                String team2Name = team2.getShortName();
                String team2Icon = team2.getIconName();
                boolean isFinished = gameJSON.getBoolean("MatchIsFinished");
                Date gameTime = DateConverter.toDate(gameJSON.getString("MatchDateTimeUTC"));
                GameEntity game = new GameEntity(gameId, team1id, team1Name, team1Icon, team2id, team2Name, team2Icon, gameTime, currentGameweek, isFinished);
                if (isFinished) {
                    JSONArray results = gameJSON.getJSONArray("MatchResults");
                    JSONObject finalResult = results.getJSONObject(1);
                    int team1Score = finalResult.getInt("PointsTeam1");
                    int team2Score = finalResult.getInt("PointsTeam2");
                    teams = updateTeamStats(teams, team1id, team2id, team1Score, team2Score);
                    game.setTeam1Score(team1Score);
                    game.setTeam2Score(team2Score);
                    goals = parseGoals(goals, gameJSON, gameId);
                }
                games.add(game);
            }
        } catch (JSONException e) {
            Log.e("DATA", "Error processing JSONArray: " + jsonInput);
            e.printStackTrace();
        }
        return data;
    }

    private static List<GoalEntity> parseGoals(List<GoalEntity> goals, JSONObject gameJSON, int gameId) throws JSONException {
        JSONArray goalsArray = gameJSON.getJSONArray("Goals");
        if (goalsArray != null) {
            for (int n = 0; n < goalsArray.length(); n++) {
                JSONObject jsonGoal = goalsArray.getJSONObject(n);
                Log.d("DATA","Loading "+jsonGoal.toString());
                int goalId = jsonGoal.getInt("GoalID");
                int team1Goals = jsonGoal.getInt("ScoreTeam1");
                int team2Goals = jsonGoal.getInt("ScoreTeam2");
                String scorerName = jsonGoal.getString("GoalGetterName");
                int matchMinute = (jsonGoal.getString("MatchMinute")=="null"?0:jsonGoal.getInt("MatchMinute"));
                goals.add(new GoalEntity(goalId, gameId, team1Goals, team2Goals, scorerName, matchMinute));
            }
        }
        return goals;
    }

    private static TeamEntity getTeamById(int teamId, List<TeamEntity> teams) {
        for (TeamEntity team : teams) {
            if (teamId == team.getId()) {
                return team;
            }
        }
        return null;
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
