package com.example.reptimex;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateRoutineActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    protected final static String ACTIVITY_NAME = "CreateRoutineActivity";
    final ArrayList<Exercise> exerciseArray = new ArrayList<>();
    ExerciseAdapter globalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_set);
        this.setTitle(R.string.title_activity_create_routine);

        final ListView listview = findViewById(R.id.exercise_list);
        listview.setOnItemClickListener(this);
        final ExerciseAdapter exerciseAdapter = new ExerciseAdapter(this);
        this.globalAdapter = exerciseAdapter;
        listview.setAdapter(exerciseAdapter);

        final Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME,"clicked save button");
                CreateRoutineActivity.this.finish();
            }
        });

        final Button addExerciseButton = findViewById(R.id.button_add);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder exerciseBuilder = new AlertDialog.Builder(CreateRoutineActivity.this);
                LayoutInflater inflater = CreateRoutineActivity.this.getLayoutInflater();
                final View v = inflater.inflate(R.layout.add_exercise_dialog, null);
                exerciseBuilder.setView(v);
                exerciseBuilder.setTitle(R.string.add_exercise_dialog);

                final Spinner durationSpinner = v.findViewById(R.id.duration_unit);
                ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(CreateRoutineActivity.this,R.array.units_time,android.R.layout.simple_spinner_item);
                durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                durationSpinner.setAdapter(durationAdapter);

                final Spinner breakSpinner = v.findViewById(R.id.break_unit);
                breakSpinner.setAdapter(durationAdapter);

                final Spinner weightdistSpinner = v.findViewById(R.id.weight_dist_unit);
                ArrayAdapter<CharSequence> weightdistAdapter = ArrayAdapter.createFromResource(CreateRoutineActivity.this,R.array.units_weight_dist,android.R.layout.simple_spinner_item);
                weightdistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                weightdistSpinner.setAdapter(weightdistAdapter);

                final EditText exerciseET = v.findViewById(R.id.exercise_text);
                final EditText durationET = v.findViewById(R.id.duration_text);
                final EditText breakET = v.findViewById(R.id.break_text);
                final EditText weightDistET = v.findViewById(R.id.weight_dist_text);

                exerciseBuilder.setPositiveButton(R.string.DialogYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = exerciseET.getText().toString();
                        int duration, breakDuration, weightDist;
                        try{
                            duration = Integer.parseInt(durationET.getText().toString());
                        } catch (NumberFormatException e1){
                            duration = 0;
                        }
                        String durationUnit = (String) durationSpinner.getSelectedItem();
                        try {
                            breakDuration = Integer.parseInt(breakET.getText().toString());
                        } catch (NumberFormatException e2){
                            breakDuration = 0;
                        }
                        String breakUnit = (String) breakSpinner.getSelectedItem();
                        try {
                            weightDist = Integer.parseInt(weightDistET.getText().toString());
                        } catch (NumberFormatException e3){
                            weightDist = 0;
                        }
                        String weightDistUnit = (String) weightdistSpinner.getSelectedItem();
                        Exercise newExercise = new Exercise(name, duration, durationUnit, breakDuration, breakUnit, weightDist, weightDistUnit);
                        exerciseArray.add(newExercise);
                        exerciseAdapter.notifyDataSetChanged();
                    }
                });
                exerciseBuilder.setNegativeButton(R.string.DialogNo,null);

                AlertDialog exerciseDialog = exerciseBuilder.create();
                exerciseDialog.show();

            }
        });

    }

    public void onItemClick(AdapterView l, View view, int position, long id){
        final int p = position;
        Exercise currentExercise = exerciseArray.get(p);
        AlertDialog.Builder exerciseBuilder = new AlertDialog.Builder(CreateRoutineActivity.this);
        LayoutInflater inflater = CreateRoutineActivity.this.getLayoutInflater();
        final View v = inflater.inflate(R.layout.add_exercise_dialog, null);
        exerciseBuilder.setView(v);
        exerciseBuilder.setTitle(R.string.add_exercise_dialog);

        final Spinner durationSpinner = v.findViewById(R.id.duration_unit);
        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(CreateRoutineActivity.this,R.array.units_time,android.R.layout.simple_spinner_item);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAdapter);

        final Spinner breakSpinner = v.findViewById(R.id.break_unit);
        breakSpinner.setAdapter(durationAdapter);

        final Spinner weightdistSpinner = v.findViewById(R.id.weight_dist_unit);
        ArrayAdapter<CharSequence> weightdistAdapter = ArrayAdapter.createFromResource(CreateRoutineActivity.this,R.array.units_weight_dist,android.R.layout.simple_spinner_item);
        weightdistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightdistSpinner.setAdapter(weightdistAdapter);

        final EditText exerciseET = v.findViewById(R.id.exercise_text);
        exerciseET.setText(currentExercise.toString());
        final EditText durationET = v.findViewById(R.id.duration_text);
        durationET.setText(Integer.toString(currentExercise.getDuration()));
        final EditText breakET = v.findViewById(R.id.break_text);
        breakET.setText(Integer.toString(currentExercise.getBreakDuration()));
        final EditText weightDistET = v.findViewById(R.id.weight_dist_text);
        weightDistET.setText(Integer.toString(currentExercise.getWeightDist()));

        exerciseBuilder.setPositiveButton(R.string.DialogYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = exerciseET.getText().toString();
                int duration, breakDuration, weightDist;
                try{
                    duration = Integer.parseInt(durationET.getText().toString());
                } catch (NumberFormatException e1){
                    duration = 0;
                }
                String durationUnit = (String) durationSpinner.getSelectedItem();
                try {
                    breakDuration = Integer.parseInt(breakET.getText().toString());
                } catch (NumberFormatException e2){
                    breakDuration = 0;
                }
                String breakUnit = (String) breakSpinner.getSelectedItem();
                try {
                    weightDist = Integer.parseInt(weightDistET.getText().toString());
                } catch (NumberFormatException e3){
                    weightDist = 0;
                }
                String weightDistUnit = (String) weightdistSpinner.getSelectedItem();
                Exercise newExercise = new Exercise(name,duration,durationUnit,breakDuration,breakUnit,weightDist,weightDistUnit);
                exerciseArray.set(p, newExercise);
                globalAdapter.notifyDataSetChanged();
            }
        });
        exerciseBuilder.setNegativeButton(R.string.DialogNo,null);

        AlertDialog exerciseDialog = exerciseBuilder.create();
        exerciseDialog.show();
    }

    private class ExerciseAdapter extends ArrayAdapter<String>{

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
            LayoutInflater inflater = CreateRoutineActivity.this.getLayoutInflater();
            View v = inflater.inflate(R.layout.exercise_item,null);
            TextView name = v.findViewById(R.id.exercise_item_text);
            name.setText(getItem(position));
            return v;
        }

    }
}
