package com.fuerza.moosh9.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moosh9 on 7/13/2015.
 *
 * Adapted from: http://theopentutorials.com/tutorials/android/listview/android-custom-listview-with-image-and-text-using-arrayadapter/
 */
public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;
    List<RowItem> rowItems;




    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    //TODO change the type later when spotify json reader is hooked up
    public void addAll(ArrayList<String> result) {
         rowItems= new ArrayList<RowItem>();
        for (int i=0; i < result.size(); i++) {
            RowItem item= new RowItem(result.get(i));
            this.add(item);
        }


    }


    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        //TextView txtTitle;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_artist, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.artist_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.artist_image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.getDesc());
       // holder.txtTitle.setText(rowItem.getTitle());
        //holder.imageView.setImageResource(rowItem.getImageId());

        return convertView;
    }



}
