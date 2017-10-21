package com.mami.jypa.matkahuolto;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParserFactory;

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
 * Thread safe asyncronized search.
 */
public class Search extends AsyncTask<SearchValues, Void, List<Route>> {

    private HttpPost post;

    protected List<Route> doInBackground(SearchValues... values) {
        for (SearchValues searchValue : values) {

            Location from = searchValue.getFrom();
            Location to = searchValue.getTo();

            StringBuilder body = new StringBuilder("requestXml=<navici_request><ajax_request_object object_id=\"1\" service=\"RouteRequests\">");

            // date and time
            body.append("<get_route id=\"1\" language=\"fi\" TimeDirection=\"forward\" Date=\"");
            body.append(searchValue.getDate());
            body.append("\" Time=\"");
            body.append(searchValue.getTime());
            body.append("\" WalkSpeed=\"70\" MaxWalk=\"1500\" RoutingMethod=\"default\" ChangeMargin=\"3\" NumberRoutes=\"");
            body.append(3); // Only 3 search results
            body.append("\" ExcludedLines=\"\">");
            // from
            body.append("<location order=\"0\" x=\"");
            body.append(from.getX());
            body.append("\" y=\"");
            body.append(from.getY());
            body.append("\" city=\"");
            body.append(searchValue.getCity());
            body.append("\"/>");
            // to
            body.append("<location order=\"1\" x=\"");
            body.append(to.getX());
            body.append("\" y=\"");
            body.append(to.getY());
            body.append("\" city=\"");
            body.append(searchValue.getCity());
            body.append("\"/>");
            // end
            body.append("</get_route></ajax_request_object></navici_request>");

            try {

                final HttpClient client = new DefaultHttpClient();
                post = new HttpPost(
                        "http://jyvaskyla.matkahuolto.info/ajaxRequest.php");
                post.addHeader("Content-type",
                        "application/x-www-form-urlencoded; charset=UTF-8");
                post.setEntity(new StringEntity(body.toString(), "UTF-8"));
                HttpResponse response = client.execute(post);

                // XML
                XMLReader xr = SAXParserFactory
                        .newInstance()
                        .newSAXParser()
                        .getXMLReader();
                XMLParser handler = new XMLParser();
                xr.setContentHandler(handler);
                xr.parse(new InputSource(response.getEntity().getContent()));

                return handler.getRoutes();

            } catch (Exception e) {}
        }

        return new ArrayList<Route>();
    }

    @Override
    protected void onCancelled() {
        post.abort();
    }
}