package com.mami.jypa;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/*
 This file is part of JYPA.

 JYPA is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 JYPA is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with JYPA.  If not, see <http://www.gnu.org/licenses/>.

 */

public class SearchResults extends FragmentActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    StatePager statePager;
    ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresults);

        statePager = new StatePager(getFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(statePager);
    }
}


class StatePager extends FragmentStatePagerAdapter {

    public StatePager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        //return MapFragment.newInstance();
        return new SearchResultsFragment();
    }

    @Override
    public int getCount() {
        return 1;
    }
}