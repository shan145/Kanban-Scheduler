package com.example.kanbanscheduler.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {
    int mNumOfTabs;

    public PagerAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle, int numOfTabs) {
        super(fragmentManager, lifecycle);
        this.mNumOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return null;
    }

    @Override
    public int getItemCount() {
        return mNumOfTabs;
    }
}
