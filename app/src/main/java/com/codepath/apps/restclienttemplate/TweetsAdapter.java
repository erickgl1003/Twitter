package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.ComposeFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{


    Context context;
    List<Tweet> tweets;
    Boolean liked = false;
    Boolean rted = false;
    //Bind values based on position of the element

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    //For each row inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);


        return new ViewHolder(view);
    }
    //Bind values based on position of the element
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //Get data at position
        Tweet tweet = tweets.get(position);

        //Bind the tweet with the viewholder
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }
    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }




    //Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfileImage;
        ImageView ivMedia;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvHandler;
        TextView tvTime;
        TextView tvLikes;
        ImageButton btnLike;
        ImageButton btnComment;
        ImageButton btnRt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvHandler = itemView.findViewById(R.id.tvHandler);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnRt = itemView.findViewById(R.id.btnRt);
            btnComment = itemView.findViewById(R.id.btnComment);
        }

        public void bind(final Tweet tweet) {
            ParseRelativeDate prd = new ParseRelativeDate();
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.name);
            tvHandler.setText("@"+tweet.user.screenName);
            tvTime.setText(" · " + prd.getRelativeTimeAgo(tweet.createdAt));


            if(Integer.parseInt(tweet.likes) > 0)
                tvLikes.setText(tweet.likes);
            else tvLikes.setText("");


            if(tweet.liked) {
                Log.i("Likeeee",tweet.liked.toString() + " should be true " + tweet.id);
                btnLike.setImageResource(R.drawable.ic_vector_heart);
                btnLike.setImageTintList(context.getResources().getColorStateList(R.color.medium_red));
            }
            else{
                btnLike.setImageTintList(context.getResources().getColorStateList(R.color.inline_action_retweet_pressed));
                btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
            }
            if(tweet.rt) {
                btnRt.setImageTintList(context.getResources().getColorStateList(R.color.medium_green));
                btnRt.setClickable(false);
            }
            else{
                btnRt.setImageTintList(context.getResources().getColorStateList(R.color.inline_action_retweet_pressed));
                btnRt.setClickable(true);
            }



            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fr = ((AppCompatActivity)context).getSupportFragmentManager();
                    //tweet.user.screenName,Integer.parseInt(tweet.id)
                    Bundle bundle = new Bundle();
                    bundle.putString("username",tweet.user.screenName);
                    bundle.putString("id",tweet.id);
                    ComposeFragment frag = ComposeFragment.newInstance();
                    frag.setArguments(bundle);
                    frag.show(fr,"ComposeFragment");
                }
            });

            btnRt.setOnClickListener(new View.OnClickListener() {
                TwitterClient client = TwitterApp.getRestClient(context);
                @Override
                public void onClick(View v) {
                    client.rt(tweet.id,new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {

                            btnRt.setImageTintList(context.getResources().getColorStateList(R.color.medium_green));
                            Log.i("rtAdapter", json.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                        }
                    });
                }
            });
            Log.i("Likeeee",tweet.liked.toString() + " " + tweet.id);

            btnLike.setOnClickListener(new View.OnClickListener() {
                TwitterClient client = TwitterApp.getRestClient(context);

                @Override
                public void onClick(View v) {
                    if(btnLike.getImageTintList() == context.getResources().getColorStateList(R.color.inline_action_retweet_pressed))
                        liked = false;
                    else liked = true;
                    Log.i("Likeeee",liked.toString() + " " + tweet.id);
                    client.like(tweet.id, liked, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i("Likeeee",liked.toString());
                            if(liked) {
                                btnLike.setImageTintList(context.getResources().getColorStateList(R.color.inline_action_retweet_pressed));
                                btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                                if(tvLikes.getText().toString().length() > 0 ) {
                                    if (Integer.parseInt(tvLikes.getText().toString()) - 1 == 0)
                                        tvLikes.setText("");
                                    else
                                        tvLikes.setText(String.valueOf(Integer.parseInt(tvLikes.getText().toString()) - 1));
                                }
                            }
                            else{
                                btnLike.setImageTintList(context.getResources().getColorStateList(R.color.medium_red));
                                btnLike.setImageResource(R.drawable.ic_vector_heart);
                                if(tvLikes.getText().toString().length() > 0 )
                                    tvLikes.setText(String.valueOf(Integer.parseInt(tvLikes.getText().toString())+1));
                                else
                                    tvLikes.setText("1");
                            }
                            Log.i("rtAdapter", json.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.i("Likeeee",liked.toString() + " " + tweet.id);
                        }
                    });
                }
            });



            int radius = 60; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop
            Glide.with(context).load(tweet.user.profileImageUrl).centerCrop().transform(new RoundedCornersTransformation(radius, margin)).into(ivProfileImage);
            if(tweet.media != ""){
                radius = 30;
                margin = 10;
                ivMedia.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.media).centerCrop().transform(new RoundedCornersTransformation(radius, margin)).into(ivMedia);
            }
            else{
                ivMedia.setVisibility(View.GONE);
            }
        }

    }

}
