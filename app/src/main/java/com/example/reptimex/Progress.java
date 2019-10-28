package com.example.reptimex;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Progress extends AppCompatActivity {

    EditText timer;
    Button startTimer;
    Button createRoutine;
    Button cancelTraining;
    long startTime;
    long originalStartTime;
    ProgressBar progressBar;

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        timer = findViewById(R.id.timer);
        startTimer = findViewById(R.id.startTimer);
        createRoutine = findViewById(R.id.createRoutine);
        cancelTraining = findViewById(R.id.cancelTraining);
        progressBar = findViewById(R.id.progressBar);

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Cheese", "Cheese");
            }
        });
        startTime = 20*60*1000;
        originalStartTime = startTime;
        // Hard coding setting time
        long millis = startTime;
        int showMils = (int) (millis % 1000);
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        timer.setText(String.format("%02d:%02d:%03d", minutes, seconds, showMils));

        startTimer.setText("Start Timer");
        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer = (Button) v;
                if (startTimer.getText().equals("Stop Timer")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    startTimer.setText("Start Timer");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    startTimer.setText("Stop Timer");
                }
            }
        });

        // GOTO Create Routine (Julius)
        createRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Cheese", "Cheese");
                Intent intent = new Intent(Progress.this, CreateRoutineActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        // Ends program or resets timer?
        cancelTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = originalStartTime;
                long millis = startTime;
                int showMils = (int) (millis % 1000);
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timer.setText(String.format("%02d:%02d:%03d", minutes, seconds, showMils));
            }
        });
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int showMils = (int) (millis % 1000);
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timer.setText(String.format("%02d:%02d:%03d", minutes, seconds, showMils));
            progressBar.setProgress(seconds);

            timerHandler.postDelayed(this, 0);
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        startTimer.setText("Start Timer");
    }

    @Override
    public void onStop() {
        super.onStop();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
