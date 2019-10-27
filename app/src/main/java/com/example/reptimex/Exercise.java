package com.example.reptimex;

public class Exercise {

    private String name;
    private int duration, breakDuration, weightDist;
    private String durationUnit, breakUnit, weightDistUnit;

    public Exercise(String name, int duration, String durationUnit, int breakDuration, String breakUnit, int weightDist, String weightDistUnit){
        this.name = name;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.breakDuration = breakDuration;
        this.breakUnit = breakUnit;
        this.weightDist = weightDist;
        this.weightDistUnit = weightDistUnit;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getBreakDuration(){
        return this.breakDuration;
    }

    public int getWeightDist(){
        return this.weightDist;
    }

    public String getDurationUnit(){
        return this.durationUnit;
    }

    public String getBreakUnit(){
        return this.breakUnit;
    }

    public String getWeightDistUnit() {
        return this.weightDistUnit;
    }
}
