package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewpager;
    private int[] images = {R.drawable.viewpager0, R.drawable.viewpager1, R.drawable.viewpager2, R.drawable.viewpager3};
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign variable
        viewpager = findViewById(R.id.viewpager);
        // initialize main adapter
        adapter = new ViewPagerAdapter(images);

        // set adapter on viewPager
        viewpager.setAdapter(adapter);

        // set clip padding
        viewpager.setClipToPadding(false);
        // set clip children
        viewpager.setClipChildren(false);
        // set page limit
        viewpager.setOffscreenPageLimit(3);
        // set default start position
        viewpager.getChildAt(0);

        // viewpager.getChildAt(viewpager.getItemDecorationCount())

        ImageButton btn_to_landing_page = findViewById(R.id.btn_next);
        btn_to_landing_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage = viewpager.getCurrentItem();

                if(currentPage < 3) {
                    viewpager.setCurrentItem(currentPage + 1);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Landing_page.class);
                    startActivity(intent);
                }
            }
        });
    }
}