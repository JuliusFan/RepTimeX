package com.example.reptimex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RoutinesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    protected final static String ACTIVITY_NAME = "RoutinesActivity";
    protected final static String DATA_KEY = "routineArray";
    protected final static String DELETE_KEY = "delete";
    public static ArrayList<Routine> routineArrayList;
    RoutineAdapter globalAdapter;
    private ProgressBar progressBar;
    private final int MAX_SIZE = 10;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);
        this.setTitle(R.string.title_activity_routines);

        loadData();

        Toolbar toolbar = findViewById(R.id.routines_toolbar);
        setSupportActionBar(toolbar);

        final ListView listView = findViewById(R.id.routines_list);
        listView.setOnItemClickListener(this);
        this.globalAdapter = new RoutineAdapter(this);
        listView.setAdapter(this.globalAdapter);

        addButton = findViewById(R.id.button_add);
        disableButtonCheck();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int text = R.string.ToastCreateRoutine;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(RoutinesActivity.this, text, duration);
                toast.show();
                Intent intent = new Intent(RoutinesActivity.this, CreateRoutineActivity.class);
                startActivityForResult(intent,10);
            }
        });


        final Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                showSnackBar(RoutinesActivity.this, R.string.SnackbarSaveData);
            }
        });

        this.progressBar = findViewById(R.id.routines_bar);
        updateProgressBar();
    }

    private void saveData(){
        SharedPreferences preferences = getSharedPreferences("sharedpref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(routineArrayList);
        editor.putString(DATA_KEY,json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedpref",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(DATA_KEY,null);
        Type type = new TypeToken<ArrayList<Routine>>() {}.getType();
        routineArrayList = gson.fromJson(json, type);
        if (routineArrayList == null)
            routineArrayList = new ArrayList<>();
    }

    public void onItemClick(AdapterView l, View v, final int position, long id){
        Intent intent = new Intent(this, CreateRoutineActivity.class);
        intent.putExtra("position",position);
        startActivityForResult(intent,10);
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
                final View v = inflater.inflate(R.layout.routines_about_dialog, null);
                aboutDialogBuilder.setView(v);
                aboutDialogBuilder.setPositiveButton(R.string.DialogClose,null);
                AlertDialog aboutDialog = aboutDialogBuilder.create();
                aboutDialog.show();
                break;
        }
        return true;
    }

    public void showSnackBar(Activity activity, int stringID){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, stringID, Snackbar.LENGTH_SHORT).show();
    }

    private void updateProgressBar(){
        this.progressBar.setProgress(routineArrayList.size() * 100 / MAX_SIZE);
    }

    private void disableButtonCheck(){
        if(routineArrayList.size()==MAX_SIZE)
            addButton.setClickable(false);
        else
            addButton.setClickable(true);
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"onDestroy() called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (data != null) {
            int position = data.getIntExtra(DELETE_KEY, -1);
            if (position != -1)
                routineArrayList.remove(position);
        }
        updateProgressBar();
        disableButtonCheck();
        this.globalAdapter.notifyDataSetChanged();
    }

    private class RoutineAdapter extends ArrayAdapter<String> {

        private RoutineAdapter(Context context){
            super(context, 0);
        }

        public int getCount(){
            return routineArrayList.size();
        }

        public String getItem(int position){
            return routineArrayList.get(position).toString();
        }

        public View getView(int position, View convertView, ViewGroup Parent){
            LayoutInflater inflater = RoutinesActivity.this.getLayoutInflater();
            View v = inflater.inflate(R.layout.list_item,null);
            TextView name = v.findViewById(R.id.list_item_text);
            name.setText(getItem(position));
            return v;
        }
    }
}
