package com.example.reptimex;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FragmentTimer extends Fragment {
    private Button btnNavFrag1;
    private Button btnNavFrag2;
    private Button btnNavSecondActivity;
    private TextView countDownText;
    private Button countdownButton;
    private TextView workOutText;
    private ProgressBar progress;

    private int index = 0;
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

    View view;
    Routine routine;
    ArrayList<Exercise> exerciseArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_timer,container,false);
        btnNavFrag2 = (Button) view.findViewById(R.id.btnNavFrag2);
        btnNavFrag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),R.string.menu_about, Toast.LENGTH_SHORT).show();
                ((TrainingActivity)getActivity()).setViewPager(1);
            }
        });


        this.routineNum = TrainingActivity.routineNum;
        countDownText = view.findViewById(R.id.countdown);
        countdownButton = view.findViewById(R.id.countdown_button);
        currentExercise = view.findViewById(R.id.current_exercise);
        workOutText = view.findViewById(R.id.routine_title);
        progress = view.findViewById(R.id.progress_bar);
        progress.setProgress(0);
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


        int temp = exercisecount;

        for (int j=0;j<exerciseArray.size();j++){
            if(exerciseArray.get(j).getBreakDurationMS()>0){
                temp++;
            }
        }
        progress.setMax(temp);


        currentExercise.setText(getString(R.string.label_current_exercise)+" "+ current.toString());

        updateTimer();

        return view;
    }
    public void startStop(){
        if(isTimerRunning) {
            stopTimer();
            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),R.string.pause_button, Snackbar.LENGTH_SHORT);
            snackBar.show();
        }else {
            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),R.string.start_button
                    , Snackbar.LENGTH_SHORT);
            snackBar.show();
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
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(),R.string.toast_exercise_done, Toast.LENGTH_SHORT).show();

//                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
//                        R.string.toast_exercise_done,
//                        Toast.LENGTH_SHORT);
//                toast.show();
                stopTimer();
               progress.incrementProgressBy(1);

                if (hasbreak > 0) {
                    Toast.makeText(getActivity(),R.string.label_break, Toast.LENGTH_SHORT).show();

//                    toast = Toast.makeText(getActivity().getApplicationContext(),R.string.label_break
//                            ,
//                            Toast.LENGTH_SHORT);
//                    toast.show();


                    timeleftmilliseconds = (int) hasbreak;
                    currentExercise.setText(R.string.label_break);
                    updateTimer();
                    hasbreak = 0;
                    timeleftmilliseconds+=500;
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
                        timeleftmilliseconds+=500;

                        startTimer();
                    }else{
                        if (hasbreak > 0) {
                            Toast.makeText(getActivity(),R.string.label_break, Toast.LENGTH_SHORT).show();

//                            toast = Toast.makeText(getActivity().getApplicationContext(), R.string.label_break,
//                                    Toast.LENGTH_SHORT);
//                            toast.show();

                            timeleftmilliseconds = (int) hasbreak;
                            currentExercise.setText(R.string.label_break);
                            updateTimer();
                            timeleftmilliseconds+=500;

                            hasbreak = 0;
                            startTimer();

                        }else {
                            Toast.makeText(getActivity(),R.string.toast_routine_done, Toast.LENGTH_SHORT).show();

//                            toast = Toast.makeText(getActivity().getApplicationContext(),R.string.toast_routine_done,
//                                    Toast.LENGTH_SHORT);
//                            toast.show();
                            index = 0;
                            exercisesdone = 0;
                            current = exerciseArray.get(0);
                            if (current.getBreakDurationMS() > 0) {
                                hasbreak = current.getBreakDurationMS();
                            }
                            currentExercise.setText(getString(R.string.label_current_exercise)+" "+exerciseArray.get((index)).toString());
                            countdownButton.setText(R.string.restart_button);
                            timeleftmilliseconds = current.getDurationMS();
                            progress.setProgress(0);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }
}
