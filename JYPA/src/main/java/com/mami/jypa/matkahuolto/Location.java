package com.mami.jypa.matkahuolto;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
 * Location is structure class for bus stop.
 */
public class Location {

    private String name;
    private int x, y;

    /**
     * @param name Name of the location.
     * @param x Location x position.
     * @param y Location y position.
     */
    public Location(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    /**
     * @return name of the location.
     */
    public String getName() {
        return name;
    }

    /**
     * @return location x position.
     */
    public int getX() {
        return x;
    }

    /**
     * @return location y position.
     */
    public int getY() {
        return y;
    }

    /**
     * @return location name.
     */
    public String toString() {
        //return String.format("%s (%d,%d)", name, x, y);
        return name;
    }


    public boolean equals(Object obj) {
        if (obj instanceof Location)
            return ((Location) obj).getName().equals(name);
        return false;
    }


    /**
     * Reads all positions from /assets/stops.xml and returns them.
     *
     * @param context
     * @return list of available locations
     */
    public static List<Location> getLocations(Context context) {
        List<Location> locations = new ArrayList<Location>();

        BufferedReader br = null;
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open("stops.xml"));
            br = new BufferedReader(isr);
            String row, name = "";
            int x = 0, y = 0;
            int mapX = 0, mapY = 0;
            while ((row = br.readLine()) != null) {
                if (row.contains("<name>")) {
                    name = row.substring(row.indexOf(">") + 1, row.lastIndexOf("<"));
                } else if (row.contains("<x>")) {
                    //x = (int) Double.parseDouble(row.substring(row.indexOf(">")+1, row.lastIndexOf("<")));
                    x = Integer.parseInt(row.substring(row.indexOf(">") + 1, row.lastIndexOf(".")));
                } else if (row.contains("<y>")) {
                    //y = (int) Double.parseDouble(row.substring(row.indexOf(">")+1, row.lastIndexOf("<")));
                    y = Integer.parseInt(row.substring(row.indexOf(">") + 1, row.lastIndexOf(".")));
                } else if (row.contains("<mapx>")) {
                    mapX = Integer.parseInt(row.substring(row.indexOf(">") + 1, row.lastIndexOf("<")));
                } else if (row.contains("<mapy>")) {
                    mapY = Integer.parseInt(row.substring(row.indexOf(">") + 1, row.lastIndexOf("<")));
                } else if (row.contains("</busstop>")) {
                    locations.add(new Location(name, x, y));
                    name = "";
                    x = 0;
                    y = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return locations;
    }


    /**
     * Find location by name.
     *
     * @param context
     * @param name of the location
     * @return location or null
     */
    public static Location getLocation(Context context, String name) {
        List<Location> locations = getLocations(context);
        for (Location location : locations) {
            if (location.getName().equals(name)) {
                return location;
            }
        }
        return null;
    }
}
