package com.lbb.christophemei.epicture;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbb.christophemei.epicture.DownloadImage;
import com.lbb.christophemei.epicture.ListItem;
import com.lbb.christophemei.epicture.R;

/**
 * Created by Christophe Mei on 08/02/2018.
 */

public class CustomListAdapter extends BaseAdapter {

    Context mContext;

    private ArrayList<ListItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context context, ArrayList<ListItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.image_layout, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            holder.favButton = (Button) convertView.findViewById(R.id.buttonfav);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.position = position;

        final ListItem newsItem = listData.get(position);
        holder.headlineView.setText(newsItem.getHeadline());
        Log.d("position", newsItem.getHeadline() + " " + position);
        if (holder.imageView != null && newsItem.image != null)
        {
            Log.d("image", "not null");
            holder.imageView.setColorFilter(0);
            holder.imageView.setImageBitmap(newsItem.image);
            notifyDataSetChanged();
        }
        else if (holder.imageView != null) {
            holder.imageView.setColorFilter(mContext.getResources().getColor(R.color.colorPrimaryDark));
            new DownloadImage(holder.imageView, newsItem.getId(), listData, position).execute(newsItem.getUrl());
            newsItem.first = 1;
            notifyDataSetChanged();
        }

        if (newsItem.getFav() == 0)
            holder.favButton.setBackgroundResource(R.drawable.heart);
        else
            holder.favButton.setBackgroundResource(R.drawable.heartred);


        final Button fav = (Button) convertView.findViewById(R.id.buttonfav);
        fav.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (newsItem.getFav() == 1) {
                    newsItem.setFav(0);
                    SharedPreferences settings = mContext.getSharedPreferences("pref", 0);
                    String access_token = settings.getString("access_token", null);
                    new CallAPI().execute("fav", newsItem.getId(), access_token);
                }
                else {
                    newsItem.setFav(1);
                    SharedPreferences settings = mContext.getSharedPreferences("pref", 0);
                    String access_token = settings.getString("access_token", null);
                    new CallAPI().execute("fav", newsItem.getId(), access_token);
                }
                notifyDataSetChanged();
                //holder.id;
                //db.deleteTask(task.taskId);
                //((MainActivity)mycontext).displayTasks();
            }
        });
        notifyDataSetChanged();
        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        ImageView imageView;
        Button favButton;
        int position;
    }
}
