package com.fuerza.moosh9.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private CustomListViewAdapter spotifyAdapter;

    private String artist;
    List<RowItem> rowItems;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        //create rootView up here instead of just returning it so that adapter can be
        //attached to list view
        View rootView= inflater.inflate(R.layout.fragment_main, container, false);

        //Create arrayAdapter to populate artist list

        //first pack resource Views in array to put in adapter
        int[] idArray= new int[3];
        idArray[0]= R.id.artist_name;
        idArray[1]= R.id.artist_image;



        spotifyAdapter=
                new CustomListViewAdapter(getActivity(),
                        R.layout.list_item_artist,
                        idArray,
                        new ArrayList<RowItem>() );

        //code that actually attaches adapter to ListView
        ListView listView= (ListView) rootView.findViewById(R.id.artist_search_listView);
        listView.setAdapter(spotifyAdapter);
        listView.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Top10TracksActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist); //add artist name so next activity can search tracks
                startActivity(intent);
            }
        }));
        return rootView;
    }




    @Override
    public void onStart() {
        super.onStart();
        searchWhenReady();



    }

    private void searchWhenReady() {
        //get text from text field
        final EditText editText = (EditText) getActivity().findViewById(R.id.artist_search_field);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    artist = editText.getText().toString();
                    handled = true;

                    //use text to search for artist
                    searchArtists();
                    //hides keyboard so user can see results
                    hideKeyboard();


                }
                return handled;
            }
        });
    }

    private void hideKeyboard() {
        //hides keyboard
        //this code copied from here: http://stackoverflow.com/questions/2342620/how-to-hide-keyboard-after-typing-in-edittext-in-android
        InputMethodManager inputManager =
                (InputMethodManager) getActivity().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //search for artists from Spotify
    private void searchArtists() {
        //requires internet search so must create a background thread



        SearchArtistTask searchTask= new SearchArtistTask();
        //begin background thread search
        searchTask.execute(artist);



    }


    public class SearchArtistTask extends AsyncTask<String,Void,ArrayList<String>> {

        private final String LOG_TAG = SearchArtistTask.class.getSimpleName();

        //Gives instructions for what to do in background thread
        @Override
        protected ArrayList<String> doInBackground(String... params) {

            if (params.length == 0) {
                System.out.println("There was no artist to get data on!");
                return null;
            }else {



                String artist = params[0];
                return generateSpotifyData(artist);
            }

        }


        //actually does work of interacting with Spotify servers
        private ArrayList<String> generateSpotifyData(String artist) {


            String name= artist;

            //temporary sample data generation
            ArrayList<String> data= new ArrayList<>();
            for ( int i=1; i<30; i++) {
                data.add(name + " option "+i);
            }

            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null) {
                spotifyAdapter.clear();
                spotifyAdapter.addAll(result);

            }
        }


    }

}
