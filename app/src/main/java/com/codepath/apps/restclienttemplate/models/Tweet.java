package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel
public class Tweet {
    private static final String TAG = "Tweet";
    public String body;
    public String createdAt;
    public User user;
    public String media = "";
    public String likes;
    public Boolean liked = false;
    public Boolean rt = false;
    public String id;

    public Tweet(){ }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.likes = jsonObject.getString("favorite_count");
        tweet.liked = jsonObject.getBoolean("favorited");
        tweet.id = jsonObject.getString("id_str");
        tweet.rt = jsonObject.getBoolean("retweeted");
        try{
            JSONArray media = jsonObject.getJSONObject("entities").getJSONArray("media");
            Log.i(TAG,media.toString());
            tweet.media = media.getJSONObject(0).getString("media_url_https");
            Log.i(TAG,tweet.media);
        }catch(JSONException e){

        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length();i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
