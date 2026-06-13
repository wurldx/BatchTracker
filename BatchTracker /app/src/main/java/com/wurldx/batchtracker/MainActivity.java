package com.wurldx.batchtracker;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private TextView tab0, tab1, tab2;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tab0 = findViewById(R.id.tab0);
        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0: return new TodayFragment();
                    case 1: return new WeekFragment();
                    case 2: return new CalendarFragment();
                    default: return new TodayFragment();
                }
            }
            @Override
            public int getCount() { return 3; }
        });

        tab0.setOnClickListener(v -> viewPager.setCurrentItem(0));
        tab1.setOnClickListener(v -> viewPager.setCurrentItem(1));
        tab2.setOnClickListener(v -> viewPager.setCurrentItem(2));

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tab0.setTextColor(position == 0 ? 0xFF0047AB : 0xFF555555);
                tab1.setTextColor(position == 1 ? 0xFF0047AB : 0xFF555555);
                tab2.setTextColor(position == 2 ? 0xFF0047AB : 0xFF555555);
            }
        });
    }
}