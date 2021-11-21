package com.example.triviaapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class KinzaPrefs {

    public static final String HIGHEST_SCORE = "highest_score";
    public static final String INDEX = "trivia_index";
    private SharedPreferences preferences;


    public KinzaPrefs(Activity context) {
        this.preferences = context.getPreferences(context.MODE_PRIVATE);
    }

    public void saveHighestScore(int score){

        int currentScore = score;

        int lastScore = preferences.getInt(HIGHEST_SCORE, 0);


        if(currentScore > lastScore) {


            preferences.edit().putInt(HIGHEST_SCORE, currentScore).apply();

        }
        }

    public int getHighestScore(){

        return preferences.getInt(HIGHEST_SCORE, 0);
    }

    public void setState(int index){

        preferences.edit().putInt(INDEX, index).apply();;

    }

    public int getState(){

        return preferences.getInt(INDEX, 0);
    }

}
