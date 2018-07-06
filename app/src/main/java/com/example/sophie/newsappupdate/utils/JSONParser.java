package com.example.sophie.newsappupdate.utils;

import android.content.Context;
import android.util.Log;

import com.example.sophie.newsappupdate.data.NewsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {

    public static ArrayList<NewsObject> testHistory = new ArrayList<>();
    Context context;
    String title = "";
    String articleAuthor = "";
    String text = "";
    String date = "";
    String url = "";

    public void parseGuardianJson(Context context, String firstJson) {

        this.context = context;

        try {
            JSONObject firstObject = new JSONObject(firstJson);
            JSONObject response = firstObject.optJSONObject("response");
            JSONArray resultJSON = response.optJSONArray("results");
            //get object from each array element
            for (int i = 0; i < resultJSON.length(); i++) {
                JSONObject finalValues = resultJSON.getJSONObject(i);
                title = finalValues.getString("webTitle");

                //get other values
                text = finalValues.getString("sectionId");
                date = finalValues.getString("webPublicationDate");
                url = finalValues.getString("webUrl");
                //get author
                try {
                    JSONArray getAuthor = finalValues.optJSONArray("tags");
                    JSONObject Author = getAuthor.getJSONObject(0);
                    articleAuthor = Author.getString("webTitle");
                } catch (Exception e) {
                    articleAuthor = "unknown author";
                    Log.d("xyz", e.toString());
                }

                /**
                 * found out it always has a date but kept the possibility of not having it in the rest of the code for
                 * the purpose of fulfilling all the criteria
                 */

                //add all my info into the app

                if (articleAuthor.length() > 0) {
                    testHistory.add((new NewsObject(title, text, articleAuthor, date, url)));
                } else {
                    testHistory.add((new NewsObject(title, text, date, 2, url)));
                }
            }
        } catch (JSONException e) {
            Log.d("xyz", e.toString());
        }
        articleAuthor = "unknown author";
    }

}
