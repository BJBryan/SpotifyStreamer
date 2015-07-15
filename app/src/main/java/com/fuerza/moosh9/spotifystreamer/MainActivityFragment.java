package com.fuerza.moosh9.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private CustomListViewAdapter spotifyAdapter;

    private String artist;


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


    public class SearchArtistTask extends AsyncTask<String,Void,ArrayList<String[]>> {
    //ASync task is an easy way of creating background threads.
        private final String LOG_TAG = SearchArtistTask.class.getSimpleName();

        //Gives instructions for what to do in background thread
        @Override
        protected ArrayList<String[]> doInBackground(String... params) {

            if (params.length == 0) {
                System.out.println("There was no artist to get data on!");
                return null;
            }else {

                String artist = params[0];
                return generateSpotifyData(artist);
            }

        }


        //actually does work of interacting with Spotify servers
        private ArrayList<String[]> generateSpotifyData(String artist) {


            String name= artist;




            /////////////////////////////////////////////////
            //temporary sample data generation
            ArrayList<String[]> data= new ArrayList<>();
            String[] fakeArtist= new String[3];
            for ( int i=1; i<30; i++) {
                fakeArtist[0]= name;
                fakeArtist[1]= "12fdre64581";
                data.add(fakeArtist);
            }

            if (true) {
                return data;
            }

            /////////////////////////////////////////////////


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String artistJsonStr = null;

           final String query_type= "artist";

            try {

                if (false) {
                    throw new IOException() {};
                }
                //TODO: internet stuff


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                //If the code didn't successfully get the Spotify data there's
                //no point in attempting to parse it.
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return parseSpotifyJson(artist);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            //This will only happen if there was an error getting or parsing Sdata
            return data;
        }

        private ArrayList<String[]> parseSpotifyJson(String artistJson)
                throws JSONException {

            //the ArrayList<String> that will ultimately be returned and its constituent artist containers
            ArrayList<String[]> artistArrayList= new ArrayList<>();
            String[] artistData= new String[3];

            //These are the names of the JSON objects that need to be extracted
            final String SPO_ITEMS= "items";
            final String SPO_NAME= "name";
            final String SPO_ID= "id";
            final String SPO_IMAGES= "images"; //ideally the 64 X 64 thumbnail images

            JSONObject spotifyJson= new JSONObject(artistJson);
            JSONArray artistArray= spotifyJson.getJSONArray(SPO_ITEMS);

            for(int i= 0; i < artistArray.length(); i++) {

                //Get data from artist object i in the list
                JSONObject suggestedArtist= artistArray.getJSONObject(i);

                //Get the string representing the artist name and add it to data
                artistData[0]= suggestedArtist.getString(SPO_NAME);


                //Get the string representing the Spotify ID
                artistData[1]= suggestedArtist.getString(SPO_ID);



                //Get image thumbnail is trickier because you need right size and none may even exist
                JSONArray artistImageArray= suggestedArtist.getJSONArray(SPO_IMAGES);
                //search through array to find smallest pictur--if any pictures exist
                if (artistImageArray.length() != 0) {

                    //store size of first image and its index for comparison

                    int smallestSize= Integer.parseInt(artistImageArray.getJSONObject(0).getString("height")) *
                            Integer.parseInt(artistImageArray.getJSONObject(0).getString("width"));
                    int smallestImageIndex= 0;

                    //compare other images to find smaller
                    int index= 0;
                    while (index < artistImageArray.length()) {
                        //calculate image size
                        int contenderImage= Integer.parseInt(artistImageArray.getJSONObject(index).getString("height"))
                                * Integer.parseInt(artistImageArray.getJSONObject(index).getString("width"));

                        //if smaller than switch with current image
                        if (contenderImage < smallestSize) {

                            smallestSize= contenderImage;
                            smallestImageIndex= index;

                        }


                        index += index;
                    }
                    //add image to data
                    artistData[2]= artistImageArray.getJSONObject(smallestImageIndex).getString("url");
                }

                artistArrayList.add(artistData);

            }


            return artistArrayList;
        }


        @Override
        protected void onPostExecute(ArrayList<String[]> result) {
            if (result != null) {
                spotifyAdapter.clear();
                spotifyAdapter.addAll(result);

            }
        }


    }

}
