package com.example.miniprojet.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.miniprojet.Fragments.DetailsFragment;
import com.example.miniprojet.Fragments.FilesFragment;
import com.example.miniprojet.Fragments.SignaturesFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {

        private int numOfTabs;

        public MyPagerAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs = numOfTabs;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    DetailsFragment detailsFragment = new DetailsFragment();
                    return detailsFragment;
                case 1:
                    FilesFragment filesFragment = new FilesFragment();
                    return filesFragment;
                case 2:
                    SignaturesFragment signaturesFragment = new SignaturesFragment();
                    return signaturesFragment;
                default:
                    return null;
            }

        }
        @Override
        public int getCount() {
            return numOfTabs;
        }
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Details";
                case 1:
                    return "Files";
                case 2:
                    return "Signatures";
            }
            return null;
        }
    }

