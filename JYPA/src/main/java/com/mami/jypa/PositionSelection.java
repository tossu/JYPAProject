package com.mami.jypa;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mami.jypa.matkahuolto.Location;

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

/**
 * Activity that takes care of busstop (position) selection.
 * TODO: Näppäimistö kaappaa back buttonin ja sulkee näppäimistön activityn sijaan.
 */
public class PositionSelection extends ListActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<Location> adapter = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_activated_1, Location.getLocations(this));
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(this);
        setListAdapter(adapter);
    }

    /**
     * Called when windows focus change (it is displayed).
     * Shows keyboard.
     *
     * @param hasFocus
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getListView().post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMgr.toggleSoftInput(0, 0);
                }
            });
        }
    }

    /**
     * Is called when user clicks item in list.
     * Sets clicked item in activity result and ends activity.
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Location location = (Location) getListAdapter().getItem(i);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("POSITION", location.toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
