package com.example.sophie.newsappupdate.utils;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.sophie.newsappupdate.data.NewsObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class JSONLoader extends AsyncTaskLoader<List<NewsObject>> {

    private boolean firstTry = true;
    private String dataStream;
    public List<NewsObject> jsonData;
    private String url;
    private String backupURL = "http://content.guardianapis.com/lifeandstyle?api-key=test";

    public JSONLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    public List<NewsObject> loadInBackground() {
        if (url == null) {
            return null;
        } else {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) (StringToURL(url)).openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(20000);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    dataStream = readFromStream(inputStream);
                } else {
                    Log.e("xyz", "makeHttpRequest error : " + urlConnection.getResponseCode());
                }

            } catch (IOException e) {

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                JSONParser parser = new JSONParser();
                parser.parseGuardianJson(getContext(), dataStream);
            }
            return jsonData;
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public URL StringToURL(String urlString) {
        try {
            URL myURL = new URL(urlString);
            return myURL;
        } catch (Exception e) {
            if (firstTry) {
                Log.e("xyz", e.toString());
                firstTry = false;
                try {
                    URL myURL = StringToURL(backupURL);
                    return myURL;
                } catch (Exception ee) {
                    Log.e("xyz", ee.toString());
                    return null;
                }
            } else {
                firstTry = true;
                return null;
            }
        }
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                response.append(line);
                line = reader.readLine();
            }
        }
        return response.toString();
    }
}

