package com.example.reptimex;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CreateRoutineActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    protected final static String ACTIVITY_NAME = "CreateRoutineActivity";
    protected final static String DATA_KEY = "routineArray";
    protected final static String DELETE_KEY = "delete";
    ArrayList<Exercise> exerciseArray;
    ArrayList<Routine> routineArrayList = RoutinesActivity.routineArrayList;
    String name;
    ExerciseAdapter globalAdapter;
    int position;
    private final int MAX_SIZE = 10;
    private ProgressBar progressBar;
    private Button addExerciseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);
        this.setTitle(R.string.title_activity_create_routine);

        Toolbar toolbar = findViewById(R.id.exercises_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.position = intent.getIntExtra("position",-1);

        if (this.position == -1){
            this.exerciseArray = new ArrayList<>();
        } else {
            this.exerciseArray = routineArrayList.get(this.position).getExercises();
        }

        final ListView listview = findViewById(R.id.exercise_list);
        listview.setOnItemClickListener(this);
        final ExerciseAdapter exerciseAdapter = new ExerciseAdapter(this);
        this.globalAdapter = exerciseAdapter;
        listview.setAdapter(exerciseAdapter);

        final EditText nameET = findViewById(R.id.routine_name);
        if (this.position != -1){
            nameET.setText(routineArrayList.get(this.position).toString());
        }

        final Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "clicked save button");
                name = nameET.getText().toString();
                if (name.equals(""))
                    name = "Routine";

                saveData();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(DELETE_KEY,-1);
                setResult(Activity.RESULT_OK,resultIntent);
                CreateRoutineActivity.this.finish();
            }
        });

        final Button deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "clicked delete button");
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateRoutineActivity.this);
                builder.setTitle(R.string.delete_dialog);
                builder.setPositiveButton(R.string.DialogYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(DELETE_KEY,position);
                        setResult(Activity.RESULT_OK,resultIntent);
                        CreateRoutineActivity.this.finish();
                    }
                });
                builder.setNegativeButton(R.string.DialogNo, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        final Button startButton = findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateRoutineActivity.this, TrainingActivity.class);
                intent.putExtra("index", position);
                name = nameET.getText().toString();
                if (name.equals(""))
                    name = "Routine";
                saveData();
                startActivity(intent);
            }
        });

        addExerciseButton = findViewById(R.id.button_add);
        disableButtonCheck();
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.i(ACTIVITY_NAME, Integer.toString(exerciseArray.size()));
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
                        if (name.equals(""))
                            name = getString(R.string.label_exercise);
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
                        updateProgressBar();
                        disableButtonCheck();
                        Snackbar.make(view,R.string.SnackbarAdd,Snackbar.LENGTH_SHORT).show();
                    }
                });
                exerciseBuilder.setNegativeButton(R.string.DialogNo,null);

                AlertDialog exerciseDialog = exerciseBuilder.create();
                exerciseDialog.show();
            }
        });

        this.progressBar = findViewById(R.id.exercise_bar);
        updateProgressBar();
    }

    private void updateProgressBar(){
        this.progressBar.setProgress(this.exerciseArray.size() * 100 / MAX_SIZE);
    }

    private void disableButtonCheck(){
        if (this.exerciseArray.size()==MAX_SIZE)
            addExerciseButton.setClickable(false);
        else
            addExerciseButton.setClickable(true);
    }

    private void saveData(){
        if (this.position == -1)
            routineArrayList.add(new Routine(this.name, this.exerciseArray));
        else
            routineArrayList.set(this.position, new Routine(this.name, this.exerciseArray));
        SharedPreferences preferences = getSharedPreferences("sharedpref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(routineArrayList);
        editor.putString(DATA_KEY,json);
        editor.apply();

        int text = R.string.ToastSave;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(CreateRoutineActivity.this, text, duration);
        toast.show();
    }

    public void onItemClick(AdapterView l, View view, final int position, long id){
        final int p = position;
        Exercise currentExercise = exerciseArray.get(p);
        AlertDialog.Builder exerciseBuilder = new AlertDialog.Builder(CreateRoutineActivity.this);
        LayoutInflater inflater = CreateRoutineActivity.this.getLayoutInflater();
        final View v = inflater.inflate(R.layout.add_exercise_dialog_with_delete, null);
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

        final AlertDialog exerciseDialog = exerciseBuilder.create();
        exerciseDialog.show();

        final Button deleteButton = v.findViewById(R.id.delete_exercise);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exerciseArray.remove(position);
                globalAdapter.notifyDataSetChanged();
                updateProgressBar();
                disableButtonCheck();
                exerciseDialog.dismiss();
            }
        });
    }

    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.routines_menu,m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        int id = mi.getItemId();
        switch(id) {
            case R.id.action_about:
                AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View v = inflater.inflate(R.layout.exercises_about_dialog, null);
                aboutDialogBuilder.setView(v);
                aboutDialogBuilder.setPositiveButton(R.string.DialogClose,null);
                AlertDialog aboutDialog = aboutDialogBuilder.create();
                aboutDialog.show();
                break;
        }
        return true;
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
            View v = inflater.inflate(R.layout.list_item,null);
            TextView name = v.findViewById(R.id.list_item_text);
            name.setText(getItem(position));
            return v;
        }

    }
}
