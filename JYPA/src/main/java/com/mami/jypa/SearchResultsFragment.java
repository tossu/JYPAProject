package com.mami.jypa;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mami.jypa.matkahuolto.Location;
import com.mami.jypa.matkahuolto.Move;
import com.mami.jypa.matkahuolto.Route;
import com.mami.jypa.matkahuolto.Search;
import com.mami.jypa.matkahuolto.SearchValues;

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
public class SearchResultsFragment extends ListFragment {
    private Search search = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getActivity().getIntent().getExtras();
        Location from = Location.getLocation(getActivity(), bundle.getString("FROM"));
        Location to = Location.getLocation(getActivity(), bundle.getString("TO"));
        String time = bundle.getString("TIME");
        String date = bundle.getString("DATE");

        search = new Search() {
            @Override
            protected void onPostExecute(List<Route> routes) {
                setRoutes(routes);
            }
        };
        search.execute(new SearchValues(from, to, date, time));
    }


    /**
     * Called when view is created.
     * Hides Horizontal scrollBar.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setVerticalScrollBarEnabled(false);
    }


    /**
     * Will cancel search before fragment
     * is detached from Activity.
     */
    @Override
    public void onDetach() {
        search.cancel(true);
        super.onDetach();
    }


    /**
     * Is called when search is finished.
     *
     * @param routes routes that search fetched.
     */
    public void setRoutes(List<Route> routes) {
        if (routes.size() > 0) {
            RouteAdapter adapter = new RouteAdapter(getActivity(), routes);
            setListAdapter(adapter);
        } else {
            AlertDialog dialogNoResults = new AlertDialog.Builder(getActivity()).create();
            dialogNoResults.setTitle(getString(R.string.noRoutesTitle));
            dialogNoResults.setMessage(getString(R.string.noRoutesText));
            dialogNoResults.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().finish();
                        }
                    });
            dialogNoResults.show();
        }
    }


    /**
     * Turns route objects to views.
     */
    public class RouteAdapter extends ArrayAdapter<Route> {
        private final Context context;
        private final List<Route> routes;

        public RouteAdapter(Context context, List<Route> routes) {
            super(context, R.layout.route, routes);
            this.context = context;
            this.routes = routes;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.route, parent, false);
            }

            if (routes.size() == 0) {
                return view;
            }

            Route route = routes.get(position);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(route.toString());

            TextView info = (TextView) view.findViewById(R.id.info);
            info.setText(String.format("%smin %.2fkm", route.getTime(),
                    route.getDistance()));

            LinearLayout[] layouts = {
                    (LinearLayout) view.findViewById(R.id.linearLayout0),
                    (LinearLayout) view.findViewById(R.id.linearLayout1),
                    (LinearLayout) view.findViewById(R.id.linearLayout2),
                    (LinearLayout) view.findViewById(R.id.linearLayout3),
                    (LinearLayout) view.findViewById(R.id.linearLayout4),
                    (LinearLayout) view.findViewById(R.id.linearLayout5),};

            Button[] buttons = {
                    (Button) view.findViewById(R.id.button0),
                    (Button) view.findViewById(R.id.button1),
                    (Button) view.findViewById(R.id.button2),
                    (Button) view.findViewById(R.id.button3),
                    (Button) view.findViewById(R.id.button4),
                    (Button) view.findViewById(R.id.button5),};

            TextView[] texts = {
                    (TextView) view.findViewById(R.id.textView0),
                    (TextView) view.findViewById(R.id.textView1),
                    (TextView) view.findViewById(R.id.textView2),
                    (TextView) view.findViewById(R.id.textView3),
                    (TextView) view.findViewById(R.id.textView4),
                    (TextView) view.findViewById(R.id.textView5),};

            List<Move> moves = route.getMoves();
            for (int n = 0; n < moves.size(); n++) {
                Move move = moves.get(n);
                if (move.getType() == Move.Type.BUS) {
                    buttons[n].setText(move.getBusCode());
                }

                layouts[n].setVisibility(View.VISIBLE);
                texts[n].setText(move.toString());
            }

            return view;
        }
    }
}
