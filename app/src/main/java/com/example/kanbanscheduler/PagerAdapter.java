package com.example.kanbanscheduler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int NumOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mNumOfTabs = NumOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return new TodoFragment();
            case 1: return new ProgFragment();
            case 2: return new DoneFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
