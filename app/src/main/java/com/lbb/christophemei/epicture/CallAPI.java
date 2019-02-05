package com.lbb.christophemei.epicture;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created by Christophe Mei on 06/02/2018.
 */

public class CallAPI extends AsyncTask<String, String, String> {

    String CLIENT_ID = "7d0143a9a4b64fb";

    public CallAPI(){
        //set context variables if required
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {


        //.\emulator.exe -avd "Nexus_5X_API_27_x86" -dns-server 8.8.8.8,8.8.4.4

        switch (params[0])
        {
            case "uploadImage":
                uploadImageRequest(params);
                break;
            case "getGallery":
                return getGalleryRequest(params);
            case "getGallerySearch":
                return getGallerySearchRequest(params);
            case "getMyUploads":
                return getMyUploadsRequest(params);
            case "getMyFav":
                return getFavRequest(params);
            case "fav":
                FavImageRequest(params);
                break;
            default:
                break;

        }

        return params[0];
    }

    private String getMyUploadsRequest(String... params)
    {
        String urlString = "https://api.imgur.com/3/account/me/images";
        String access_token = params[1];

        try {

            HttpURLConnection connection = null;

            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + access_token);

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Log.d("debug", response.toString());

            return (response.toString());
        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();


        }
        return "";
    }

    private String getGallerySearchRequest(String... params)
    {
        String urlString = "https://api.imgur.com/3/gallery/search/viral/month/0?q=" + params[1];

        try {

            HttpURLConnection connection = null;

            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Log.d("debug", response.toString());

            return (response.toString());
        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();


        }
        return "";
    }

    private String getFavRequest(String... params)
    {
        String urlString = "https://api.imgur.com/3/account/" + params[1] + "/gallery_favorites/0/newest";
        String access_token = params[2];

        try {

            HttpURLConnection connection = null;

            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            connection.setRequestProperty("Authorization", "Bearer " + access_token);

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Log.d("debug", response.toString());

            return (response.toString());
        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();


        }
        return "";
    }

    private String getGalleryRequest(String... params)
    {
        String urlString = "https://api.imgur.com/3/gallery/hot/" + params[1] + "/day/0";
        String access_token = params[2];

        try {

            HttpURLConnection connection = null;

            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            connection.setRequestProperty("Authorization", "Bearer " + access_token);

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Log.d("debug", response.toString());

            return (response.toString());
        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();


        }
        return "";
    }

    private void uploadImageRequest(String... params)
    {
        String urlString = params[1];
        String image = params[2];
        String access_token = params[3];

        try {

            HttpURLConnection connection = null;

            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            //connection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            connection.setRequestProperty("Authorization", "Bearer " + access_token);

            //connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            connection.setRequestProperty("Content-Type", "multipart/form-data");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(image);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Log.d("debug", response.toString());

            JSONObject data = new JSONObject(response.toString()).getJSONObject("data");
            Log.d("link", data.getString("link"));

        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();


        }

    }

    private void FavImageRequest(String... params)
    {
        String urlString = "https://api.imgur.com/3/image/" + params[1] + "/favorite";
        String access_token = params[2];

        Log.d("fav", access_token);
        try {

            HttpURLConnection connection = null;

            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + access_token);

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();


        }

    }

}