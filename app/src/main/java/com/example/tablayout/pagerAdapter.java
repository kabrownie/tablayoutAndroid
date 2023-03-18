package com.example.tablayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tablayout.segments.Student;
import com.example.tablayout.segments.course;
import com.example.tablayout.segments.summary;

import java.util.ArrayList;

public class pagerAdapter extends FragmentStateAdapter {
     public pagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
switch (position){
    case 0:
        return new Student();
    case 1:
        return new course();
    case 2:
        return new summary();


}
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
