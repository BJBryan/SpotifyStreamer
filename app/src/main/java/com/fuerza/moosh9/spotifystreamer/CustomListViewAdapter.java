package com.fuerza.moosh9.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moosh9 on 7/13/2015.
 *
 * Adapted from: http://theopentutorials.com/tutorials/android/listview/android-custom-listview-with-image-and-text-using-arrayadapter/
 */
public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;
    int resourceId;
    int[] viewIds;
    List<RowItem> rowItems;




    public CustomListViewAdapter(Context context, int mainLayoutId,int[] viewIds,
                                 List<RowItem> items) {
        super(context, mainLayoutId, items);
        this.context = context;
        this.resourceId= mainLayoutId;
        this.viewIds= viewIds;
    }

    //TODO fill in
    public void addAll(ArrayList<String[]> artistData) {

        for (int i=0; i < artistData.size(); i++) {
            RowItem item= new RowItem(artistData.get(i)[0],"",artistData.get(i)[2]);
            this.add(item);
        }


    }

    //TODO change the type later when spotify json reader is hooked up
    public void addAll(ArrayList<String[]> trackData,int ignore) {
       // rowItems= new ArrayList<RowItem>();

        for (int i=0; i < trackData.size(); i++) {
            RowItem item= new RowItem(trackData.get(i)[0],trackData.get(i)[1]);
            this.add(item);
        }


    }


    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtDesc2;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(viewIds[0]);
            holder.imageView = (ImageView) convertView.findViewById(viewIds[1]);

            if (viewIds[2] != 0) {
                holder.txtDesc2= (TextView) convertView.findViewById(viewIds[2]);
            }

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.getDesc());
        //Picasso automatically handles images
        Picasso.with(context).load(rowItem.getImage()).into(holder.imageView);


        if (viewIds[2] != 0) {
            holder.txtDesc2.setText(rowItem.getDesc2());
        }

        return convertView;
    }



}
