package com.example.reptimex;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class TrainingActivity extends AppCompatActivity {
    public static int routineNum;

    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        Intent intent = getIntent();
        this.routineNum = intent.getIntExtra("index",0);


    }

    private void setupViewPager(ViewPager viewPager){
        SectionsStatePagerAdapter adapter  = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(),"fragment1");
        adapter.addFragment(new Fragment2(),"fragment2");
        viewPager.setAdapter(adapter);

    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }
}