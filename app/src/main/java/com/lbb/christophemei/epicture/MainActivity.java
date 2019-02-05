package com.lbb.christophemei.epicture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ListView listView = null;
    ArrayList<ListItem> listData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences settings = getSharedPreferences("pref", 0);
        boolean logged = settings.getBoolean("logged", false);

        if (!logged)
        {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        }

        viralOrder();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        // Handle item selection
        switch (menu.getItemId()) {
            case R.id.viralOrder:
                viralOrder();
                return true;
            case R.id.topOrder:
                topOrder();
                return true;
            case R.id.userOrder:
                userOrder();
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    public void viralOrder()
    {
        listData = getListData("viral");

        listView = (ListView) findViewById(R.id.custom_list);
        listView.setAdapter(new CustomListAdapter(this, listData));
    }

    public void topOrder()
    {
        listData = getListData("top");

        listView = (ListView) findViewById(R.id.custom_list);
        listView.setAdapter(new CustomListAdapter(this, listData));
    }

    public void userOrder()
    {
        listData = getListData("time");

        listView = (ListView) findViewById(R.id.custom_list);
        listView.setAdapter(new CustomListAdapter(this, listData));
    }

    public void uploadPicture(View view)
    {
//        UploadActivity uploadActivity = new UploadActivity();
        Intent intent = new Intent(getBaseContext(), UploadActivity.class);
        startActivity(intent);
    }

    public void myFavorites(View v)
    {
        Intent intent = new Intent(getBaseContext(), FavoriteActivity.class);
        startActivity(intent);
    }

    public void searchPictures(View v)
    {
        Intent intent = new Intent(getBaseContext(), SearchActivity.class);
        startActivity(intent);
    }

    public void myUploadPictures(View v)
    {
        Intent intent = new Intent(getBaseContext(), MyUploadsActivity.class);
        startActivity(intent);
    }

    public void favButtonOnClick(View v)
    {

    }

    private ArrayList<ListItem> getListData(String order) {
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
            gallery = new CallAPI().execute("getGallery", order, access_token).get();
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
            newsData.first = i;
            listMockData.add(newsData);
        }

        return listMockData;
    }
}
