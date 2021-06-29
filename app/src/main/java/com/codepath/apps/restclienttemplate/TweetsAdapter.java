package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{


    Context context;
    List<Tweet> tweets;
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvHandler = itemView.findViewById(R.id.tvHandler);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivMedia = itemView.findViewById(R.id.ivMedia);
        }

        public void bind(Tweet tweet) {
            ParseRelativeDate prd = new ParseRelativeDate();
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.name);
            tvHandler.setText("@"+tweet.user.screenName);
            tvTime.setText(" · " + prd.getRelativeTimeAgo(tweet.createdAt));


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
