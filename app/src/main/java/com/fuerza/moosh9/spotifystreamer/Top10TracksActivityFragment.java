package com.fuerza.moosh9.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class Top10TracksActivityFragment extends Fragment {

    private final String LOG_TAG = Top10TracksActivityFragment.class.getSimpleName();
    private CustomListViewAdapter spotifyAdapter;

    String artist;


    public Top10TracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //create rootView up here instead of just returning it so that adapter can be
        //attached to list view
        View rootView= inflater.inflate(R.layout.fragment_top10_tracks, container, false);

        //set title appropriately
        CharSequence appTitle= getString(R.string.main_app_title);





        //Create arrayAdapter to populate artist list

        //first pack resource Views in array to put in adapter
        int[] idArray= new int[3];
        idArray[0]= R.id.track_name;
        idArray[1]= R.id.track_artist_image;
        idArray[2]= R.id.album_name;



        spotifyAdapter=
                new CustomListViewAdapter(getActivity(),
                        R.layout.list_item_track,
                        idArray,
                        new ArrayList<RowItem>() );

        //code that actually attaches adapter to ListView
        ListView listView= (ListView) rootView.findViewById(R.id.track_search_listView);
        listView.setAdapter(spotifyAdapter);
        listView.setOnItemClickListener((new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO launch player
            }
        }));

        return rootView;

    }


    @Override
    public void onStart() {



        //  called via intent.  Inspect the intent for artist name.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            artist = intent.getStringExtra(Intent.EXTRA_TEXT);



        }

        super.onStart();
        searchTopTracks();

    }

    //search Spotify for top tracks of artist selected in previous activity
    private void searchTopTracks() {
        //requires internet search so must create a background thread
        SearchTrackTask searchTask= new SearchTrackTask();
        //begin background thread search
        searchTask.execute(artist);
    }


    public class SearchTrackTask extends AsyncTask<String,Void,ArrayList<String[]>> {

        @Override
        protected ArrayList<String[]> doInBackground(String... params) {

            return generateSpotifyData();

        }

        private ArrayList<String[]> generateSpotifyData() {


            //temporary sample data generation1
            ArrayList<String> trackNames= new ArrayList<>();
            for ( int i=1; i<10; i++) {
                trackNames.add("Super Awesome track "+i);
            }



            //temporary sample data generation1
            ArrayList<String> albumNames= new ArrayList<>();
            for ( int i=1; i<10; i++) {
                albumNames.add("Hidden Gem Album " + i);
            }

            ArrayList<String[]> trackData= zipStringArrayList(trackNames,albumNames);


          return trackData;
        }

        //TODO delete this method after getting data from Spottify. Doesn't work anyway...
        private ArrayList<String[]> zipStringArrayList(ArrayList<String> trackNames, ArrayList<String> albumNames) {

            ArrayList<String[]> zippedArrayList= new ArrayList<>();
            String[] list= new String[2];

            Array[] container= new Array[10];

            for (int i=1; i<trackNames.size(); i++ ) {

                list[0]= trackNames.get(i);
                list[1]= albumNames.get(i);


                zippedArrayList.add(list);
            }

            System.out.println(zippedArrayList.get(0).length == 2);

            return zippedArrayList;
        }


        @Override
        protected void onPostExecute(ArrayList<String[]> trackData) {
            if (trackData != null) {
                spotifyAdapter.clear();
                spotifyAdapter.addAll(trackData,10);

            }
        }
    }


}
