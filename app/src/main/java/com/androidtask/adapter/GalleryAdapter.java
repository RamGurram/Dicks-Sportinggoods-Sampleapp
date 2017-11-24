package com.androidtask.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidtask.R;
import com.androidtask.model.Venue;
import com.androidtask.model.VenueImage;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<VenueImage> images;

    private OnVenueClickListener listener;
    private Context context;

    public GalleryAdapter(FragmentActivity activity, ArrayList<VenueImage> photos) {
        this.context = activity;
        this.images = photos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_thumbnail, parent, false);
        final ViewHolder viewHolder = new ViewHolder(cardView);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    listener.onVenuItemClick(images.get(viewHolder.getAdapterPosition()))
                  openLinkChromeCustomTabs(images.get(viewHolder.getAdapterPosition()).getUrl());

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String url = VenuesListAdapter.DEFAULT_URL_IMAGE;
        if(!images.get(position).getUrl().isEmpty()){
            url = images.get(position).getUrl();
        }
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imageView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            this.imageView = (ImageView) cardView.findViewById(R.id.image_view_thumbnail);
        }
    }

    public interface OnVenueClickListener {
        void onVenueClick(Venue venue);
    }

    private void openLinkChromeCustomTabs(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        builder.addDefaultShareMenuItem();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
