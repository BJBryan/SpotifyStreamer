package com.fuerza.moosh9.spotifystreamer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class Top10TracksActivityFragment extends Fragment {

    private final String LOG_TAG = Top10TracksActivityFragment.class.getSimpleName();
    private ArrayAdapter<String> spotifyAdapter;



    public Top10TracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Create arrayAdapter to populate artist list
        spotifyAdapter=
                new ArrayAdapter<String>(getActivity(),//current context (this activity)
                        R.layout.list_item_track, //The name of the layout ID
                        R.id.List_item_track_linearview, //the id of the linearview to populate.
                        new ArrayList<String>());

        return inflater.inflate(R.layout.fragment_top10_tracks, container, false);
    }
}
