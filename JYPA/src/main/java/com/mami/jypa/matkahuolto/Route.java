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

public class Route {
    // TODO oikea tyypitis aikoihin yms.
    private String departure = "";
    private String arrival = "";
    private double distance = 0; // kilometers
    private int time = 0; // minutes

    private List<Move> moves = new ArrayList<Move>();

    /**
     * Set route total time in minutes.
     * @param time in minutes.
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return route total time in minutes
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets distance in meters.
     * @param distance in meters.
     */
    public void setDistance(int distance) {
        this.distance = (double) ((int) distance / 10) / 100;
    }

    /**
     * @return distance in kilometers.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Set departure time.
     * @param departure time.
     */
    public void setDeparture(String departure) {
        this.departure = departure;
    }

    /**
     * @return departune time.
     */
    public String getDeparture() {
        return departure;
    }

    /**
     * Set arrival time.
     * @param arrival time.
     */
    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    /**
     * @return arrival time.
     */
    public String getArrival() {
        return arrival;
    }

    /**
     * Add new move in route.
     * See Route.
     * @param move which is added in route.
     */
    public void addMove(Move move) {
        moves.add(move);
    }

    /**
     * Get route movements.
     * @return list of movements in route.
     */
    public List<Move> getMoves() {
        return moves;
    }

    /**
     * @return "DepartureTime - ArrivalTime"
     */
    public String toString() {
        return getDeparture() + " - " + getArrival();
    }
}