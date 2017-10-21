package com.mami.jypa;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
 * Android application main activity.
 */
public class JYPA extends Activity implements View.OnFocusChangeListener, View.OnClickListener {
    private static final int REQUEST_CODE = 1;

    private EditText editTextFrom, editTextTo, editTextTime, editTextDate;
    private Button buttonSearch, buttonTime, buttonDate, buttonNow;
    private SharedPreferences preferences;
    private Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        preferences = this.getSharedPreferences("com.mami.jypa", Context.MODE_PRIVATE);
        calendar = Calendar.getInstance();

        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        editTextFrom.setOnFocusChangeListener(this);
        editTextFrom.setOnClickListener(this);
        editTextFrom.setInputType(InputType.TYPE_NULL);
        editTextFrom.setSelected(false);
        editTextFrom.setText(preferences.getString("busStopFrom", ""));

        editTextTo = (EditText) findViewById(R.id.editTextTo);
        editTextTo.setOnFocusChangeListener(this);
        editTextTo.setOnClickListener(this);
        editTextTo.setInputType(InputType.TYPE_NULL);
        editTextTo.setText(preferences.getString("busStopTo", ""));

        editTextTime = (EditText) findViewById(R.id.editTextTime);

        buttonTime = (Button) findViewById(R.id.buttonTime);
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeSelection();
            }
        });

        editTextDate = (EditText) findViewById(R.id.editTextDate);

        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateSelection();
            }
        });

        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This should not happen without empty editTextFrom and editTextTo
                String from = editTextFrom.getText().toString();
                String to = editTextTo.getText().toString();
                preferences.edit().putString("busStopFrom", from).commit();
                preferences.edit().putString("busStopTo", to).commit();
                startSearch(from, to);
            }
        });

        buttonNow = (Button) findViewById(R.id.buttonNow);
        buttonNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                date.getTime();
                calendar.setTime(date);
                updateTimeAndDayBoxes();
            }
        });

        updateTimeAndDayBoxes();
        toggleSearchButton();
    }


    /**
     * Listener which is called when user sets date in DatePickerDialog (look openDateSelection)
     */
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateTimeAndDayBoxes();
        }
    };


    /**
     * Called when date (Päivä) button is clicked.
     */
    public void openDateSelection() {
        new DatePickerDialog(JYPA.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    /**
     * Listener which is called when user sets time in TimePickerDialog (look openTImeSelection)
     */
    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            updateTimeAndDayBoxes();
        }
    };


    /**
     * Called when time (Aika) button is clicked.
     */
    public void openTimeSelection() {
        new TimePickerDialog(JYPA.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }


    public void updateTimeAndDayBoxes() {
        final Date time = calendar.getTime();
        editTextDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(time));
        editTextTime.setText(new SimpleDateFormat("HH:mm").format(time));
    }


    /**
     * Disables search button when editTextFrom or editTextTo is empty.
     */
    public void toggleSearchButton() {
        String from = editTextFrom.getText().toString();
        String to = editTextTo.getText().toString();
        if (from.equals("") || to.equals("")) {
            buttonSearch.setEnabled(false);
        } else {
            buttonSearch.setEnabled(true);
        }
    }


    /**
     * Comes here from PositionSelection Activity.
     * Checks which editbox is focused and then set request value in it.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == Activity.RESULT_OK) {
                    String position = data.getStringExtra("POSITION");
                    if (editTextFrom.isFocused()) {
                        editTextFrom.setText(position);
                    } else {
                        editTextTo.setText(position);
                    }
                    toggleSearchButton();
                }
                break;
            }
        }
    }


    /**
     * Called when buttonSearch is clicked.
     * Opens SearchResultsFragment activity.
     *
     * @param from
     * @param to
     */
    public void startSearch(String from, String to) {
        Intent intent = new Intent(this, SearchResults.class);
        Bundle bundle = new Bundle();
        bundle.putString("FROM", from);
        bundle.putString("TO", to);

        String time = editTextTime.getText().toString();
        bundle.putString("TIME", time);

        String date = editTextDate.getText().toString();
        bundle.putString("DATE", date);

        intent.putExtras(bundle);
        startActivity(intent);
    }


    /**
     * Called when editbox is focused.
     * Opens PositionSelected Activity.
     *
     * @param view
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            Intent intent = new Intent(this, PositionSelection.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }


    /**
     * Called when editbox is clicked.
     * Opens PositionSelected Activity.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PositionSelection.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
