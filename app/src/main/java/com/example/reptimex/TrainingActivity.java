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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TrainingActivity extends AppCompatActivity {
    private TextView countDownText;
    private Button countdownButton;
    private TextView workOutText;


    private int index = 0;
   // private int routineNum=0;

    private CountDownTimer timer;

    private long timeleftmilliseconds = 500;
    private boolean isTimerRunning;
    private TextView currentExercise;
    int routineNum;
    Exercise current;
    private long hasbreak = 0;
    int exercisesdone = 0;
    int exercisecount;

    ArrayList<Routine> routineArrayList = RoutinesActivity.routineArrayList;

    Routine routine;
    ArrayList<Exercise> exerciseArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        this.routineNum = intent.getIntExtra("index",0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        countDownText = findViewById(R.id.countdown);
        countdownButton = findViewById(R.id.countdown_button);
        currentExercise = findViewById(R.id.current_exercise);
        workOutText = findViewById(R.id.routine_title);



       //String id = intent.getStringExtra("index");

        routine = routineArrayList.get(routineNum);

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });


        exerciseArray = routine.getExercises();

        workOutText.setText(routine.toString());

        current = exerciseArray.get(index);

        if(current.getBreakDurationMS()>0){
            hasbreak = current.getBreakDurationMS();
        }

//
//
//
        if(current.getDurationMS()>0){
            timeleftmilliseconds=current.getDurationMS();
        }

        exercisecount= exerciseArray.size();


       currentExercise.setText(getString(R.string.label_current_exercise)+" "+ current.toString());

        updateTimer();
    }

    public void startStop(){
        if(isTimerRunning) {
            stopTimer();
        }else {
            startTimer();
        }

    }
//
    public void startTimer(){
        timer = new CountDownTimer(timeleftmilliseconds,1000) {
            @Override
            public void onTick(long l) {
                timeleftmilliseconds=l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                Toast toast = Toast.makeText(getApplicationContext(),
                        R.string.toast_exercise_done,
                        Toast.LENGTH_SHORT);
                toast.show();
                stopTimer();
                if (hasbreak > 0) {
                    toast = Toast.makeText(getApplicationContext(),R.string.label_break
                            ,
                            Toast.LENGTH_SHORT);
                    toast.show();


                    timeleftmilliseconds = (int) hasbreak;
                    currentExercise.setText(R.string.label_break);
                    updateTimer();
                    hasbreak = 0;
                    startTimer();

                } else if(index<exercisecount){
                    exercisesdone++;
                    index++;
                    if (index < exercisecount) {
                        current = exerciseArray.get(index);
                        currentExercise.setText(getString(R.string.label_current_exercise)+" "+ exerciseArray.get((index)).toString());
                        timeleftmilliseconds = (int) current.getDurationMS();
                        if(current.getBreakDurationMS()>0){
                            hasbreak = current.getBreakDurationMS();
                        }
                        updateTimer();
                        startTimer();
                    }else{
                        if (hasbreak > 0) {
                            toast = Toast.makeText(getApplicationContext(), R.string.label_break,
                                    Toast.LENGTH_SHORT);
                            toast.show();

                            timeleftmilliseconds = (int) hasbreak;
                            currentExercise.setText(R.string.label_break);
                            updateTimer();
                            hasbreak = 0;
                            startTimer();

                        }else {
                            toast = Toast.makeText(getApplicationContext(),R.string.toast_routine_done,
                                    Toast.LENGTH_SHORT);
                            toast.show();
                            index = 0;
                            exercisesdone = 0;
                            current = exerciseArray.get(0);
                            if (current.getBreakDurationMS() > 0) {
                                hasbreak = current.getBreakDurationMS();
                            }
                            currentExercise.setText(getString(R.string.label_current_exercise)+" "+exerciseArray.get((index)).toString());
                            countdownButton.setText(R.string.restart_button);
                            timeleftmilliseconds = current.getDurationMS();
                            updateTimer();
                        }
                    }



                }

                }





        }.start();
        countdownButton.setText(R.string.pause_button);
        isTimerRunning=true;
    }

    public void stopTimer(){
        timer.cancel();
        isTimerRunning=false;
        countdownButton.setText(R.string.button_start);

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