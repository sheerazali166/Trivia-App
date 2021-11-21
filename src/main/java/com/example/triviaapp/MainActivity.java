package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.triviaapp.data.Repository;
import com.example.triviaapp.data.answerListAsyncResponse;
import com.example.triviaapp.databinding.ActivityMainBinding;
import com.example.triviaapp.model.Question;
import com.example.triviaapp.model.Score;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private int currentQuestionIndex;

    private List<Question> questionList;

   // private int questionScore;

    private int scoreCounter = 0;

    private Score score;


    KinzaPrefs kinzaPrefs;

    private int currenQuestionIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        SharedPreferences sharedPreferences = getSharedPreferences("001", MODE_PRIVATE);
//
//        String score = String.valueOf(sharedPreferences.getInt("score", 0));

      //  binding.textViewScore.setText("Score::  " + score + " points");




        score = new Score();

        kinzaPrefs = new KinzaPrefs(MainActivity.this);

        Log.d("Kinza", "Saving Score: " + kinzaPrefs.getHighestScore());

        currenQuestionIndex = kinzaPrefs.getState();

        binding.textViewHighest.setText("Highest: " + kinzaPrefs.getHighestScore());

//        Log.d("Pref", "onClick: " + kinzaPrefs.getHighestScore());

        questionList = new Repository().getQuestions(new answerListAsyncResponse() {

            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                binding.quesionsTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());

                updateCounter();

                Log.d("Kinza", String.valueOf(questionArrayList));
            }
        });

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getNextQuestion();

//                kinzaPrefs.saveHighestScore(scoreCounter);
//
//                Log.d("Pref", "onClick: " + kinzaPrefs.getHighestScore());

            }
        });

        binding.buttonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                updateQuetion();
              //  preferencesScore();

            }
        });

        binding.buttonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                updateQuetion();
              //  preferencesScore();

            }
        });


        binding.textViewScore.setText("Current Score: " + String.valueOf(score.getScore()));
 }

    private void getNextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuetion();
    }

    private void checkAnswer(boolean userChoseCorrect) {

        boolean answer = questionList.get(currentQuestionIndex).getAnswerTrue();
        int snackMessageId;

        if(answer == userChoseCorrect){
            snackMessageId = R.string.correct_answer;
            fadeAnimation();
     //       questionScore = questionScore + 10;
            addPoints();
        }
        else{
            deductPoints();
            snackMessageId = R.string.incorrect;
            shakeAnimation();

//            if(questionScore > 0){
//
//            questionScore = questionScore - 10;
//
//            }
        }

        Snackbar.make(binding.cardView, snackMessageId, Snackbar.LENGTH_SHORT).show();
    }

    private void updateCounter() {
        binding.textViewOutOf.setText("Question: " + currentQuestionIndex + "/" + questionList.size());
    }

    private void fadeAnimation(){

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                binding.quesionsTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                binding.quesionsTextView.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void updateQuetion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        binding.quesionsTextView.setText(question);

        updateCounter();
    }

    private void shakeAnimation(){

        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_anim);

        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                binding.quesionsTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                binding.quesionsTextView.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

//    private void preferencesScore(){
//
//        SharedPreferences sharedPreferences = getSharedPreferences("001", MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putInt("score", questionScore);
//
//        editor.commit();
//    }


    private void deductPoints(){



        if (scoreCounter > 0) {
            scoreCounter -= 100;
            score.setScore(scoreCounter);

            binding.textViewScore.setText("Current Score: " +
                        String.valueOf(score.getScore()));
            }
        else{
            scoreCounter = 0;
            score.setScore(scoreCounter);


        }

      //  Log.d("Ramsha", "deductPoints: " + score.getScore());
    }

    private void addPoints(){

        scoreCounter += 100;

        score.setScore(scoreCounter);


            binding.textViewScore.setText("Current Score: " +
                    String.valueOf(score.getScore()));


//        Log.d("Kinza", "addPoints: " + score.getScore());


    }

    @Override
    protected void onPause() {

        kinzaPrefs.saveHighestScore(score.getScore());

        kinzaPrefs.setState(currentQuestionIndex);

        Log.d("State", "Saving State: " + kinzaPrefs.getState());

        Log.d("Pause", "Saving Score: " + kinzaPrefs.getHighestScore());

        super.onPause();


    }
}