package com.lbb.christophemei.epicture;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyUploadsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uploads);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<ListItem> listData = getListData();

        final ListView listView = (ListView) findViewById(R.id.custom_list);
        listView.setAdapter(new CustomListAdapter(this, listData));
    }

    private ArrayList<ListItem> getListData() {
        Log.d("DEBUGGG", "GETLISTDATAAAAAAAAA");
        ArrayList<ListItem> listMockData = new ArrayList<ListItem>();
        String gallery = "";

        ArrayList<String> images = new ArrayList<String>();
        ArrayList<String> headlines = new ArrayList<String>();
        ArrayList<String> favs = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();

        try {
            SharedPreferences settings = getSharedPreferences("pref", 0);
            String access_token = settings.getString("access_token", null);
            gallery = new CallAPI().execute("getMyUploads", access_token).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            JSONArray array = null;
            JSONObject data = new JSONObject(gallery);
            array = data.getJSONArray("data");
            Log.d("json", array.toString());
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject item = array.getJSONObject(i);
                if (item.has("type"))
                {
                    Log.d("link", item.getString("title") + " " + item.getString("link"));
                    images.add(item.getString("link"));
                    headlines.add(item.getString("title"));
                    favs.add(String.valueOf(item.getBoolean("favorite")));
                    ids.add(item.getString("id"));
                }
                else if (item.has("images"))
                {
                    JSONArray img = item.getJSONArray("images");
                    images.add(img.getJSONObject(0).getString("link"));
                    headlines.add(item.getString("title"));
                    favs.add(String.valueOf(item.getBoolean("favorite")));
                    ids.add(item.getString("id"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < images.size() ; i++) {
            ListItem newsData = new ListItem();
            newsData.setUrl(images.get(i));
            newsData.setHeadline(headlines.get(i));
            Log.d("fav", favs.get(i));
            if (favs.get(i) == "true")
                newsData.setFav(1);
            else
                newsData.setFav(0);
            newsData.setId(ids.get(i));
            newsData.image = null;

            listMockData.add(newsData);
        }

        return listMockData;
    }
}
