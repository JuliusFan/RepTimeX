package com.example.reptimex;

import java.util.ArrayList;

public class Routine  {

    private String name;
    private ArrayList<Exercise> exerciseArrayList;

    public Routine(String name, ArrayList<Exercise> exerciseArrayList){
        this.name = name;
        this.exerciseArrayList = exerciseArrayList;
    }
    @Override
    public String toString(){
        return this.name;
    }

    public ArrayList getExercises(){
        return this.exerciseArrayList;
    }

}
