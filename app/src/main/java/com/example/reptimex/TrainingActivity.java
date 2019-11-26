package com.example.reptimex;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TrainingActivity extends AppCompatActivity {
    private TextView countDownText;
    private Button countdownButton;
    private TextView workOutText;
    private int index = 0;
    private int routineNum=0;

    private CountDownTimer timer;

    private long timeleftmilliseconds = 600000;
    private boolean isTimerRunning;
    private TextView currentExercise;



    ArrayList<Routine> routineArrayList = RoutinesActivity.routineArrayList;

    Routine current;
    ArrayList<Exercise> exerciseArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        countDownText = findViewById(R.id.countdown);
        countdownButton = findViewById(R.id.countdown_button);
        currentExercise = findViewById(R.id.current_exercise);
        workOutText = findViewById(R.id.routine_title);


        //
//        Exercise pushup = new Exercise("Push Up", 5000, "Seconds", 5000, "Seconds", 0, null);
//        Exercise situp = new Exercise("Sit Up", 5000, "Seconds", 5000, "Seconds", 0, null);
//        Exercise lift = new Exercise("Bench Press", 5000, "Seconds", 5000, "Seconds", 100, "lbs");
//        ArrayList<Exercise> Exercises = new ArrayList<Exercise>();
//
//        Routine routine1 = new Routine("Daily Workout", Exercises);
//
//
//        ArrayList<Exercise> test = new ArrayList<Exercise>();
        //final boolean add = test.add(pushup);
        //this.Exercises.add(pushup);
//        this.Exercises.add(pushup);
//        this.Exercises.add(situp);
//        this.Exercises.add(lift);

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });

        current = routineArrayList.get(routineNum);
        exerciseArray = current.getExercises();

        workOutText.setText(current.toString());

        
        currentExercise.setText("Current Exercise: "+ exerciseArray.get((index)).toString()+ " "+ exerciseArray.get((index)).getDuration());

        timeleftmilliseconds=exerciseArray.get((index)).getDurationMS();


        int minutes = (int) timeleftmilliseconds/60000;
        int seconds = (int) timeleftmilliseconds%60000/1000;

        String timelefttext = ""+minutes+":";
        if (seconds<10)timelefttext+="0" ;
        timelefttext+=seconds;
        countDownText.setText(timelefttext);
    }

    public void startStop(){
        if(isTimerRunning) {
            stopTimer();
        }else {
            startTimer();
        }

    }
    public void startTimer(){
        timer = new CountDownTimer(timeleftmilliseconds,1000) {
            @Override
            public void onTick(long l) {
                timeleftmilliseconds=l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                index++;

                if (index<exerciseArray.size()) {
                    currentExercise.setText("Current Exercise: " + exerciseArray.get((index)).toString() + " " + exerciseArray.get((index)).getDuration());
                    timeleftmilliseconds = exerciseArray.get((index)).getDurationMS();

                    int minutes = (int) timeleftmilliseconds / 60000;
                    int seconds = (int) timeleftmilliseconds % 60000 / 1000;

                    String timelefttext = "" + minutes + ":";
                    if (seconds < 10) timelefttext += "0";
                    timelefttext += seconds;
                    countDownText.setText(timelefttext);


                    timer.start();
                }
                else{
                    countdownButton.setText("Restart");
                    index=0;
                    timeleftmilliseconds = exerciseArray.get((index)).getDurationMS();
                    currentExercise.setText("Current Exercise: " + exerciseArray.get((index)).toString() + " " + exerciseArray.get((index)).getDuration());

                    int minutes = (int) timeleftmilliseconds / 60000;
                    int seconds = (int) timeleftmilliseconds % 60000 / 1000;

                    String timelefttext = "" + minutes + ":";
                    if (seconds < 10) timelefttext += "0";
                    timelefttext += seconds;
                    countDownText.setText(timelefttext);

                }
                //countdownButton.setText("Restart");
            }


        }.start();
        countdownButton.setText("PAUSE");
        isTimerRunning=true;
    }

    public void stopTimer(){
        timer.cancel();
        isTimerRunning=false;
        countdownButton.setText("START");

    }

    public void updateTimer(){
        int minutes = (int) timeleftmilliseconds/60000;
        int seconds = (int) timeleftmilliseconds%60000/1000;

        String timelefttext = ""+minutes+":";
        if (seconds<10)timelefttext+="0" ;
        timelefttext+=seconds;
        countDownText.setText(timelefttext);
    }
}