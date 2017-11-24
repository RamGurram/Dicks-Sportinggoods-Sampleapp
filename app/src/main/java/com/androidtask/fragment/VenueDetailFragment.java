package com.androidtask.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtask.R;
import com.androidtask.adapter.GalleryAdapter;
import com.androidtask.helper.Constants;
import com.androidtask.model.Venue;
import com.androidtask.model.VenueLocation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import static com.androidtask.adapter.VenuesListAdapter.DEFAULT_URL_IMAGE;
import static com.androidtask.helper.Constants.BUNDLE_VENUE_FAV;
import static com.androidtask.helper.Constants.BUNDLE_VENUE_FAV_ITEM;
import static com.androidtask.helper.Constants.PREFS_NAME;


public class VenueDetailFragment extends Fragment implements OnMapReadyCallback {
    public static final String TAG = VenueDetailFragment.class.getName();

    private Venue venue;

    private boolean isFavorite;
    private FloatingActionButton favoriteButton;

    private View ratincolour;
    private TextView rating;

    private TextView adressTextView;


    private RecyclerView galleryVenueARecylerView;
    private GalleryAdapter galleryAdapter;

    private TextView socialfb;
    private TextView socialtwitter;
    private TextView socialweb;

    private GoogleMap mMap;
    private SharedPreferences settings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            venue = intent.getParcelableExtra(Constants.INTENT_EXTRA_VENUE);
        }

        if (getArguments() != null) {
            venue = getArguments().getParcelable(Constants.BUNDLE_VENUE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            venue = savedInstanceState.getParcelable(Constants.BUNDLE_VENUE);
        }
        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        View view = inflater.inflate(R.layout.fragment_venue_detail, container, false);
        initToolbarAndFAB(view);
        initVenueDetails(view);
        initVenueImages(view);
        initAboutVenue(view);
        initVenueMapView(view);
        initVenueGallery(view);
        initWebLinks(view);

        return view;
    }

    private void initToolbarAndFAB(final View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(venue.getName());
        toolbar.inflateMenu(R.menu.menu_venue_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() != R.id.menu_item_share_venue) {
                    return false;
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, venue.getUrl());
                startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
                return true;
            }
        });

        favoriteButton = (FloatingActionButton) view.findViewById(R.id.button_favorite);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteTogglerTask();
            }
        });

        favoriteCheckerTask();
    }

    private void initVenueDetails(View view) {
        TextView title = (TextView) view.findViewById(R.id.text_view_title);
        if (TextUtils.isEmpty(venue.getDescription())) {
            title.setText(venue.getName());
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.title_text));
        } else {
            title.setText(venue.getDescription());
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.title_text1));

        }

        TextView isVerified = (TextView) view.findViewById(R.id.text_view_verified);
        isVerified.setText(venue.isVerified() ? "Verified" : "Not Verified");

        rating = (TextView) view.findViewById(R.id.text_view_rating);
        String ratingtxt = "Rating: " + Double.toString(venue.getRating());
        rating.setText(ratingtxt);

        ratincolour = (View) view.findViewById(R.id.view_ratingcolour);
        if (!TextUtils.isEmpty(venue.getRatingColor())) {
            ratincolour.setBackgroundColor(Color.parseColor("#" + venue.getRatingColor()));
        }
        TextView call = (TextView) view.findViewById(R.id.text_view_venue_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!venue.getContacts().isEmpty()) {
                    String phone = venue.getContacts().get(0).getPhone();
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                }
            }
        });


    }

    private void initVenueImages(View view) {
        ImageView backdropImage = (ImageView) view.findViewById(R.id.image_view_backdrop);
        ImageView posterImage = (ImageView) view.findViewById(R.id.image_view_poster);
        String url = DEFAULT_URL_IMAGE;
        if (!venue.getPhotos().isEmpty()) {
            url = venue.getPhotos().get(0).getUrl();
        }
        Glide.with(view.getContext())
                .load(url)
                .asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .placeholder(R.drawable.image_placeholder)
                .into(backdropImage);

        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .into(posterImage);
    }

    String addresstext;

    private void initAboutVenue(View view) {
        adressTextView = (TextView) view.findViewById(R.id.text_view_address);
        VenueLocation venueLocation = venue.getLocation();
        if (venueLocation != null) {
            addresstext = venueLocation.getAddress() + "\n" + venueLocation.getCity() + ", " + venueLocation.getState() + "\n" + venueLocation.getCountry() + ", " + venueLocation.getPostalCode();
        } else {
            addresstext = getActivity().getString(R.string.empty_error_short);
        }
        adressTextView.setText(addresstext);

    }

    private void initVenueMapView(View view) {
        view.findViewById(R.id.layLocation).setVisibility(View.VISIBLE);
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        final MapView mapView = (MapView) view.findViewById(R.id.mapView);
//        mapView.getMapAsync(this);

    }

    private void updateLocationUI() {
        if (mMap == null ) {
            return;
        }
        if(venue.getLocation()==null)
        {
            return;
        }


        LatLng latLng = new LatLng(venue.getLocation().getLatitude(),
                venue.getLocation().getLongitude());
        try {
            mMap.addMarker(new MarkerOptions().title(addresstext)
                    .position(latLng)
                    .snippet(venue.getDescription()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng
                    , 16f));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    private void initVenueGallery(View view) {
        TextView textView = (TextView) view.findViewById(R.id.text_view_see_more);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getString(R.string.toast));
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        galleryVenueARecylerView = (RecyclerView) view.findViewById(R.id.recycler_view_gallery_list);
        galleryVenueARecylerView.setLayoutManager(linearLayoutManager);
        galleryVenueARecylerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.spacing_small)));
        setupGalleryAdapter();
    }

    private void setupGalleryAdapter() {
        galleryVenueARecylerView.setHasFixedSize(true);
        galleryAdapter = new GalleryAdapter(getActivity(), venue.getPhotos());
        galleryVenueARecylerView.setAdapter(galleryAdapter);

        galleryAdapter.notifyDataSetChanged();

        galleryVenueARecylerView.setHasFixedSize(true);
    }

    private void initWebLinks(View view) {

        socialweb = (TextView) view.findViewById(R.id.text_view_website);
        socialfb = (TextView) view.findViewById(R.id.text_view_social_fb);
        socialtwitter = (TextView) view.findViewById(R.id.text_view_social_twitter);
        socialfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //need to implement opening in  app if installed
                String link = "https://www.facebook.com/";
                if (!venue.getContacts().isEmpty()) {
                    link = link + venue.getContacts().get(0).getFacebook();
                }
                openLinkChromeCustomTabs(link);

            }
        });

        socialtwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //need to implement opening in  app if installed
                String link = "https://www.twitter.com/";
                if (!venue.getContacts().isEmpty()) {
                    link = link + venue.getContacts().get(0).getTwitter();
                }
                openLinkChromeCustomTabs(link);
            }
        });

        socialweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLinkChromeCustomTabs(venue.getUrl());
            }
        });
    }

    private void openLinkChromeCustomTabs(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        builder.addDefaultShareMenuItem();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.BUNDLE_VENUE, venue);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();
    }


    private class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public SpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
                return;
            }
            outRect.left = spacing;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void favoriteCheckerTask() {
        isFavorite = venue.isFavorite();

        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.ic_star_white_24dp);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
        }

    }

    private void favoriteTogglerTask() {

        boolean isalreadyFav = settings.getBoolean(BUNDLE_VENUE_FAV, false);
    if(!isalreadyFav) {
        if (!isFavorite) {
            favoriteButton.setImageResource(R.drawable.ic_star_white_24dp);
            showToast(getString(R.string.venue_added));
            saveFavourite(true);
            isFavorite = true;
            getActivity().setResult(Activity.RESULT_OK);

        } else {
            removeFavouriteItem();

        }
    }else if(FavoriteVenueFragment.getFavoritedVenue(getActivity().getApplicationContext()).getId().equals(venue.getId())){
        removeFavouriteItem();
        getActivity().setResult(Constants.CODE_UNFAVORITE);

    }else {
        showToast(getActivity().getString(R.string.op_failed));
    }

    }

    private void removeFavouriteItem(){
        isFavorite = false;
        favoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
        showToast(getString(R.string.venue_removed));
        saveFavourite(false);
        getActivity().setResult(Activity.RESULT_CANCELED);
    }

    private void saveFavourite(boolean value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(BUNDLE_VENUE_FAV, value);
        if (value) {
            Gson gson = new Gson();
            String json = gson.toJson(venue);
            editor.putString(BUNDLE_VENUE_FAV_ITEM, json);
        } else {
            editor.putString(BUNDLE_VENUE_FAV_ITEM, "");
        }
        editor.commit();

    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
