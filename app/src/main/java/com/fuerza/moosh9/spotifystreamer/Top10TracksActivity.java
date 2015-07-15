package com.fuerza.moosh9.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class Top10TracksActivity extends ActionBarActivity {
    private final String LOG_TAG = Top10TracksActivity.class.getSimpleName();
    String artist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleAndSubtitle();


        setContentView(R.layout.activity_top10_tracks);
    }

    private void setTitleAndSubtitle() {
        //set title and subtitle for this activity

        //  called via intent.  Inspect the intent for artist name.
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            artist = intent.getStringExtra(Intent.EXTRA_TITLE);

        }

        setTitle(R.string.tracks_title);
        ActionBar ab= getSupportActionBar();
        ab.setSubtitle(artist);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top10_tracks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
