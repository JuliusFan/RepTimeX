package com.example.reptimex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);
        this.setTitle(R.string.all_routines);

        loadData();

        final ListView listView = findViewById(R.id.routines_list);
        listView.setOnItemClickListener(this);
        this.globalAdapter = new RoutineAdapter(this);
        listView.setAdapter(this.globalAdapter);

        final Button addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoutinesActivity.this, CreateRoutineActivity.class);
                startActivity(intent);
            }
        });

        final Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                RoutinesActivity.this.finish();
            }
        });
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
