package com.maps.sammy0310.jsonparse;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jasonparse.models.MovieNodel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by sammy on 11-10-2016.
 */

public class MovieAdapter extends ArrayAdapter {

    private List<MovieNodel> movielist;
    private int resource;
    private LayoutInflater inflate;
    public MovieAdapter(Context context, int resource, List<MovieNodel> objects) {
        super(context, resource, objects);
        movielist = objects;
        this.resource = resource;
        inflate = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflate.inflate(resource, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.icon);
            holder.movie = (TextView) convertView.findViewById(R.id.movie);
            holder.tagline = (TextView) convertView.findViewById(R.id.tagline);
            holder.story = (TextView) convertView.findViewById(R.id.story);
            holder.year = (TextView) convertView.findViewById(R.id.year);
            holder.duration = (TextView) convertView.findViewById(R.id.duration);
            holder.director = (TextView) convertView.findViewById(R.id.director);
            holder.cast = (TextView) convertView.findViewById(R.id.cast);
            holder.rate = (RatingBar) convertView.findViewById(R.id.rating);
            convertView.setTag(holder);
        }else{

            holder = (ViewHolder)  convertView.getTag();
        }


        final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progressBar);


        // Then later, when you want to display image
        ImageLoader.getInstance().displayImage(movielist.get(position).getImage(), holder.iv, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progress.setVisibility(View.GONE);
            }
        }); // Default options will be used


        holder.movie.setText(movielist.get(position).getMovie());
        holder.tagline.setText(movielist.get(position).getTagline());
        holder.story.setText(movielist.get(position).getStory());

        //rating
        holder.rate.setRating((float) (movielist.get(position).getRating()/2));
        holder.year.setText("Year: " + movielist.get(position).getYear());
        holder.duration.setText("Duration :"+movielist.get(position).getDuration());
        holder.director.setText(movielist.get(position).getDirector());
        StringBuffer str = new StringBuffer();
        for(MovieNodel.Cast castL : movielist.get(position).getCast()){
            str.append(castL.getName() +", ");
        }


        holder.cast.setText(str);

        return convertView;
    }



}
