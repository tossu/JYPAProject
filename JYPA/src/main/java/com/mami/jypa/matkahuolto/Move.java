package com.mami.jypa.matkahuolto;

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

public class Move {
    public static enum Type {BUS, WALK};

    private Type type;
    private int distance, time;
    private String busCode;
    private List<GeoPoint> points;

    // Initialization
    {
        type = Type.BUS;
        time = 0; // minutes
        distance = 0; // meters
        busCode = "";
        points = new ArrayList<GeoPoint>();
    }

    /**
     * Sets distance in meters.
     * @param distance in meters
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * @return distance in meters.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Set time it takes to make move.
     * @param time in minutes
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * How long movement takes.
     * @return time in minutes
     */
    public int getTime() {
        return time;
    }

    /**
     * Add GeoPoint in movement.
     * One move is multiple points.
     * @param geoPoint points added in move.
     */
    public void addPoint(GeoPoint geoPoint) {
        points.add(geoPoint);
    }

    /**
     * @return list of points move got.
     */
    public List<GeoPoint> getPoints() {
        return points;
    }

    /**
     * Set movement type.
     * Look static types.
     * @param type BUS = false, WALK = true
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns movement type
     *
     * @return Type.BUS or Type.Walk
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets move bus code.
     * @param busCode move bus code.
     */
    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    /**
     * @return movements bus code.
     */
    public String getBusCode() {
        return busCode;
    }

    /**
     * @return "TIMEmin DISTANCEm"
     */
    public String toString() {
        return getTime() + "min " + getDistance() + "m";
    }
}