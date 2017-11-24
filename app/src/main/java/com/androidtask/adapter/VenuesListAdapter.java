package com.androidtask.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtask.R;
import com.androidtask.model.Venue;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class VenuesListAdapter extends RecyclerView.Adapter<VenuesListAdapter.ViewHolder> {
    private OnVenueClickListener onVenueClickListener;
    private Context context;
    private ArrayList<Venue> venues;

    public static final String DEFAULT_URL_IMAGE = "https://igx.4sqi.net/img/general/405x540/0AOYCVdPRp2SYP-uCZJq1KB98sxMoWM0rk9SWjc_egY.jpg";

    public VenuesListAdapter(Activity activity, ArrayList<Venue> venues) {
        this.onVenueClickListener = (OnVenueClickListener) activity;
        this.context = activity.getApplicationContext();
        this.venues = venues;
    }


    public void AddAll(List<Venue> venues) {
        this.venues.clear();
        this.venues.addAll(venues);

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_venue, parent, false);
        final ViewHolder viewHolder = new ViewHolder(cardView);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVenueClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    onVenueClickListener.onVenuItemClick(venues.get(position),position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String url = DEFAULT_URL_IMAGE;
        if(!venues.get(position).getPhotos().isEmpty()){
            url = venues.get(position).getPhotos().get(0).getUrl();
        }
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.poster);
        holder.title.setText(venues.get(position).getName());
        String rating = Double.toString(venues.get(position).getRating());
        holder.rating.setText(rating);
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public ImageView poster;
        public TextView title;
        public TextView rating;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            this.poster = (ImageView) cardView.findViewById(R.id.image_view_poster);
            this.title = (TextView) cardView.findViewById(R.id.text_view_title);
            this.rating = (TextView) cardView.findViewById(R.id.text_view_rating);
        }
    }

    public interface OnVenueClickListener {
        void onVenuItemClick(Venue venue, int position);

    }
}
