package com.mami.jypa.matkahuolto;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
 * Search terms which are given to Search while doing search.
 * new Search().execute(new SearchValues(fro,to,date,time)
 */
public class SearchValues {
    private Location from, to;
    private String date, time;
    private String city;

    // parameter initialization
    {
        city = "Jyväskylä";
    }


    /**
     * SearchValue constructor.
     * You should only use valid date and time formats.
     *
     * @param from location
     * @param to location
     * @param date 20/05/2013
     * @param time 23:59
     */
    public SearchValues(Location from, Location to, String date, String time) {
        // TODO: Should take DateTime instead Strings
        this.from = from;
        this.to = to;

        this.date = String.format("%s%s%s", date.substring(0, 2), date.substring(3, 5), date.substring(6, 10));
        this.time = String.format("%s%s", time.substring(0, 2), time.substring(3, 5));
    }

    public SearchValues(Location from, Location to) {
        this.from = from;
        this.to = to;

        Calendar cal = Calendar.getInstance();
        this.date = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        this.time = new SimpleDateFormat("HHmm").format(cal.getTime());
    }

    Location getFrom() {
        return from;
    }

    Location getTo() {
        return to;
    }

    String getTime() {
        return time;
    }

    String getDate() {
        return date;
    }

    /**
     * City name.
     * If not set default value is "Jyväskylä".
     * @return city name.
     */
    String getCity() {
        return this.city;
    }

    /**
     * City name which is used searchs.
     * @param city name.
     */
    void setCity(String city) {
        this.city = city;
    }
}