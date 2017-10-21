package com.mami.jypa.matkahuolto;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
 * Matkahuolto api parser.
 * Routet saadaan ulos getRoutes() metodilla.
 */
class XMLParser extends DefaultHandler {
    // TODO kun Route ja Move luokissa oikeat tyypit niin täällä annetaan oikeat tyypit pyöristettynä
    private List<Route> routes = new ArrayList<Route>();
    private Route route = null;
    private Move move = null;

    private boolean noArrival = true;

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {
        if (localName.equals("ROUTE")) {
            route = new Route();
        } else if (localName.equals("WALK")) {
            move = new Move();
            move.setType(Move.Type.WALK);
        } else if (localName.equals("LINE")) {
            move = new Move();
            move.setType(Move.Type.BUS);
            move.setBusCode(atts.getValue("code"));
        } else if ((localName.equals("STOP") || localName.equals("POINT"))
                && move != null) {
            // TODO Pitää convertoida oikeaan koordinaatti formaattiin
            int x = (int) Float.parseFloat(atts.getValue("x"));
            int y = (int) Float.parseFloat(atts.getValue("y"));

            move.addPoint(new GeoPoint(x, y));
        } else if (localName.equals("LENGTH")) {
            int time = (int) Float.parseFloat(atts.getValue("time"));
            int distance = (int) Float.parseFloat(atts.getValue("dist"));

            if (move == null) {
                route.setDistance(distance);
                route.setTime(time);
            } else {
                move.setTime(time);
                move.setDistance(distance);
            }
        } else if (localName.equals("ARRIVAL") && move == null) {
            if (noArrival) {
                route.setDeparture(reformatTime(atts.getValue("time")));
                noArrival = false;
            }
        } else if (localName.equals("DEPARTURE") && move == null) {
            route.setArrival(reformatTime(atts.getValue("time")));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (localName.equals("ROUTE")) {
            routes.add(route);
            route = null;
            noArrival = true;
        } else if (localName.equals("WALK") || localName.equals("LINE")) {
            route.addMove(move);
            move = null;
        }
    }

    public String reformatTime(String time) {
        return String.format("%s:%s", time.substring(0, 2), time.substring(2, 4));
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
