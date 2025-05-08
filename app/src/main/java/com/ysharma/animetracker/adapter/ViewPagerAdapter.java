//Created by Yashica Sharma
package com.ysharma.animetracker.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.ysharma.animetracker.fragment.CompletedFragment;
import com.ysharma.animetracker.fragment.WatchingFragment;
import com.ysharma.animetracker.fragment.WatchlistFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new WatchlistFragment();
            case 1: return new WatchingFragment();
            case 2: return new CompletedFragment();
            default: return new WatchlistFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
