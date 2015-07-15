package com.fuerza.moosh9.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


/**
 * A placeholder fragment containing a simple view.
 */
public class Top10TracksActivityFragment extends Fragment {

    private final String LOG_TAG = Top10TracksActivityFragment.class.getSimpleName();
    private CustomListViewAdapter spotifyAdapter;
    private Boolean resultsCheck;

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
            this.artist = intent.getStringExtra(Intent.EXTRA_TEXT);



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

    //this code is nearly completely redundant with other class. should be refactored
    public class SearchTrackTask extends AsyncTask<String,Void,ArrayList<String[]>> {
        //ASync task is an easy way of creating background threads.
        private final String LOG_TAG = SearchTrackTask.class.getSimpleName();

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


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String top10JsonStr = null;



            try {
                //construct the URL for the Spotify query
                //possible parameters are available at the spotify api page at
                // https://developer.spotify.com/web-api/search-item/
                final String SPOTIFY_BASE_URL=
                        "https://api.spotify.com/v1/artists/" + artist + "/top-tracks?country=US";


                URL url= new URL(SPOTIFY_BASE_URL);

                //Create the request to OpenWeatherMap and open the connections
                urlConnection= (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read the input stream into a String
                InputStream inputStream= urlConnection.getInputStream();
                StringBuffer buffer= new StringBuffer();
                if (inputStream == null) {
                    //nothing to do
                    return null;
                }
                reader= new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line= reader.readLine()) != null) {
                    //Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    //But it does make debugging a *lot* easier if you print out the completed
                    //buffer before debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    //Stream was empty. No point in parsing
                    return null;
                }

                top10JsonStr= buffer.toString();

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
                return parseSpotifyJson(top10JsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            //This will only happen if there was an error getting or parsing Sdata
            return null;
        }

        private ArrayList<String[]> parseSpotifyJson(String tracksJson)
                throws JSONException {

            //the ArrayList<String> that will ultimately be returned and its constituent artist containers
            ArrayList<String[]> trackArrayList= new ArrayList<>();


            //These are the names of the JSON objects that need to be extracted
            final String SPO_TRACKS= "tracks";
            final String SPO_NAME= "name"; //track name
            final String SPO_PREVIEWURL= "preview_url";

            final String SPO_ALBUM= "album"; //an Object which contains album name and images
            final String SPO_IMAGES= "images"; //ideally the 64 X 64 thumbnail images


            JSONArray spotifyJsonArray= (new JSONObject(tracksJson)).getJSONArray(SPO_TRACKS);

            if (spotifyJsonArray.length() == 0) {
                resultsCheck= false;
            }else {
                resultsCheck= true;
            }

            for(int i= 0; i < spotifyJsonArray.length(); i++) {

                //********Container must be defined INSIDE the loop otherwise it every entry will be an instance of the same container!
                //this causes a pesky bug where every member of the ArrayList is the same
                String[] trackData= new String[4];

                //open one of the track objects
                JSONObject track= spotifyJsonArray.getJSONObject(i);


                //Get the string representing the track name and add it to data
                trackData[0]= track.getString(SPO_NAME);


                //Get the string representing the preview url
                trackData[3]= track.getString(SPO_PREVIEWURL);

                //open album object
                JSONObject album= track.getJSONObject(SPO_ALBUM);

                //get album name
                trackData[1]= album.getString(SPO_NAME);

                //open images JSON object
                JSONArray albumImageArray= album.getJSONArray(SPO_IMAGES);

                //search through array to find smallest picture--if any pictures exist
                if (albumImageArray.length() != 0) {

                    //store size of first image and its index for comparison

                    int smallestSize= Integer.parseInt(albumImageArray.getJSONObject(0).getString("height")) *
                            Integer.parseInt(albumImageArray.getJSONObject(0).getString("width"));
                    int smallestImageIndex= 0;

                    //compare other images to find smaller
                    int index= 0;
                    while (index < albumImageArray.length()) {
                        //calculate image size
                        int contenderImage= Integer.parseInt(albumImageArray.getJSONObject(index).getString("height"))
                                * Integer.parseInt(albumImageArray.getJSONObject(index).getString("width"));

                        //if smaller than switch with current image
                        if (contenderImage < smallestSize) {

                            smallestSize= contenderImage;
                            smallestImageIndex= index;

                        }


                        index = index + 1;
                    }
                    //add image to data
                    trackData[2]= albumImageArray.getJSONObject(smallestImageIndex).getString("url");
                }

                trackArrayList.add(trackData);

            }


            return trackArrayList;
        }


        @Override
        protected void onPostExecute(ArrayList<String[]> result) {
            //throw up toast if no results
            if (resultsCheck != false) {
                spotifyAdapter.clear();
                spotifyAdapter.addAll(result);

            }else {
                Context context= getActivity().getApplicationContext();
                CharSequence text= "No top tracks in our database for that artist. Bummer.";
                int duration= Toast.LENGTH_LONG;

                Toast toast= Toast.makeText(context,text,duration);
                toast.show();

            }
        }

    }


}
