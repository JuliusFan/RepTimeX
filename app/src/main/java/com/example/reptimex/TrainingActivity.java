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

    EditText timer;
    Button startTimer;
    Button createRoutine;
    Button cancelTraining;
    long startTime;
    long originalStartTime;
    ProgressBar progressBar;



    //supposed to be current exercise, then when on to next exercise, increments by 1 until exerciseArray,size();
    int current_index = 0;
    private TextView countDownText;
    private Button countdownButton;



    private TextView currentexercise;
    private TextView nextexercise;
    private CountDownTimer stopwatch;


    //hard coded 10 minutes
    private long timeleftmilliseconds = 600000;

   ExerciseAdapter globalAdapter;



    private boolean isTimerRunning;

    protected final static String DATA_KEY = "exerciseArray";
    ArrayList<Exercise> exerciseArray;
    //CreateRoutineActivity.ExerciseAdapter globalAdapter;



    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        timer = findViewById(R.id.timer);
        startTimer = findViewById(R.id.startTimer);
        createRoutine = findViewById(R.id.createRoutine);
        cancelTraining = findViewById(R.id.cancelTraining);
        progressBar = findViewById(R.id.progressBar);


        currentexercise=findViewById(R.id.current_exercise);
        nextexercise = findViewById(R.id.next_exercise);

        countDownText = findViewById(R.id.countdown);
        countdownButton = findViewById(R.id.countdown_button);




        loadData();
        currentexercise.setText("Current Exercise: "+ exerciseArray.get(current_index).toString());
        nextexercise.setText("Next Exercise: "+ exerciseArray.get(current_index+1).toString());


        final ListView list = findViewById(R.id.exercise_list_main);
        //list.setOnItemClickListener(this);
        final ExerciseAdapter exerciseAdapter = new ExerciseAdapter(this);
        this.globalAdapter = exerciseAdapter;
        list.setAdapter(exerciseAdapter);


        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });


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
                Intent intent = new Intent(TrainingActivity.this, RoutinesActivity.class);
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
    public void onResume(){
        super.onResume();
        loadData();

        final ListView list = findViewById(R.id.exercise_list_main);
        //list.setOnItemClickListener(this);
        final ExerciseAdapter exerciseAdapter = new ExerciseAdapter(this);
        this.globalAdapter = exerciseAdapter;
        list.setAdapter(exerciseAdapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        timerHandler.removeCallbacks(timerRunnable);
    }


    //dan

    public void startTimer(){
        stopwatch = new CountDownTimer(timeleftmilliseconds,1000) {
            @Override
            public void onTick(long l) {
                timeleftmilliseconds=l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        countdownButton.setText("PAUSE");
        isTimerRunning=true;
    }
    public void stopTimer(){
        stopwatch.cancel();
        isTimerRunning=false;
        countdownButton.setText("START");
    }

    //changes the text of the timer textview
    public void updateTimer(){
        int minutes = (int) timeleftmilliseconds/60000;
        int seconds = (int) timeleftmilliseconds%60000/1000;

        String timelefttext = ""+minutes+":";
        if (seconds<10)timelefttext+="0" ;
        timelefttext+=seconds;
        countDownText.setText(timelefttext);
    }
    //pause function for timer
    public void startStop() {
        if (isTimerRunning) {
            stopTimer();
        } else {
            startTimer();
        }

    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedpref",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(DATA_KEY,null);
        Type type = new TypeToken<ArrayList<Exercise>>() {}.getType();
        this.exerciseArray = gson.fromJson(json, type);
        if (this.exerciseArray == null)
            this.exerciseArray = new ArrayList<>();
    }


    private class ExerciseAdapter extends ArrayAdapter<String> {

        public ExerciseAdapter(Context context){
            super(context, 0);
        }

        public int getCount(){
            return exerciseArray.size();
        }

        public String getItem(int position){
            return exerciseArray.get(position).toString();
        }

        public View getView(int position, View convertView, ViewGroup Parent){
            LayoutInflater inflater = TrainingActivity.this.getLayoutInflater();
            View v = inflater.inflate(R.layout.list_item,null);
            TextView name = v.findViewById(R.id.list_item_text);
            name.setText(getItem(position));
            return v;
        }

    }

}