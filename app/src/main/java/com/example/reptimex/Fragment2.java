package com.example.reptimex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Fragment2 extends Fragment {
    private Button btnNavFrag1;
    private Button btnNavSecondActivity;

    int routineNum;
    Routine routine;
    ArrayList<Exercise> exerciseArray;
    TextView aboutexercises;
    ArrayList<Routine> routineArrayList = RoutinesActivity.routineArrayList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment2_layout,container,false);
        btnNavFrag1 = (Button) view.findViewById(R.id.btnNavFrag1);

        btnNavFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), R.string.menu_timer, Toast.LENGTH_SHORT).show();

                ((TrainingActivity)getActivity()).setViewPager(0);
            }
        });
        this.routineNum = TrainingActivity.routineNum;
        routine = routineArrayList.get(routineNum);
        exerciseArray = routine.getExercises();


        aboutexercises = view.findViewById(R.id.aboutExercises);
        String text = "";

        for (int i=0;i<exerciseArray.size();i++){
            Exercise current = exerciseArray.get(i);
            text+=current.toString() +": " + current.getDuration() + current.getDurationUnit();
            if(current.getWeightDist()>0){
                text+= " "+ current.getWeightDist() + current.getWeightDistUnit();
            }
            if (current.getBreakDuration()>0){
                text += "\n" + "Break: " + current.getBreakDuration() + current.getBreakUnit();
            }
            text+="\n";
        }

        aboutexercises.setText(text);

        return view;
    }
}
