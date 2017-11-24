package com.androidtask.fragment;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.androidtask.R;
import com.androidtask.adapter.VenuesListAdapter;
import com.androidtask.helper.Constants;
import com.androidtask.model.Venue;
import com.androidtask.view.EndlessScrollRecyclerView;

import java.util.ArrayList;


public class VenueListFragment extends Fragment {
    private ProgressBar progressBar;

    private EndlessScrollRecyclerView recyclerView;
    private VenuesListAdapter adapter;
    private ArrayList<Venue> venuesList = new ArrayList<>();

    private int totalPages = 1;
    private int page=1;
    private int position;

    private boolean isFavoritesFragment;


    public VenueListFragment() {}

    private final String TAG = VenueListFragment.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            venuesList = savedInstanceState.getParcelableArrayList(Constants.VENUE_LIST);
            page = Math.max(page, savedInstanceState.getInt(Constants.NEXT_PAGE));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Constants.DEBUG) Log.i(TAG, getClass().getName() + " onCreateView");
        View view = inflater.inflate(R.layout.fragment_venues_list, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        recyclerView = (EndlessScrollRecyclerView) view.findViewById(R.id.recycler_view_venue_list);
        if (venuesList.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            loadVenues();
        }
        setUpRecyclerView();

        Class<?> c = getClass();
        if (c.getName().endsWith("FavoriteVenueFragment")) {
            isFavoritesFragment = true;
        }


        return view;
    }

    private void setUpRecyclerView() {
        int spanCount = getSpanCount();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpacingItemDecoration(
                (int) getResources().getDimension(R.dimen.spacing_venue)));


        if (venuesList == null) {
            venuesList = new ArrayList<>();
            page = 1;
            position = 0;
        }
        setAdapter(venuesList);
    }
 private void setAdapter(ArrayList<Venue> venues){
     adapter = new VenuesListAdapter(getActivity(), venues);
     recyclerView.setAdapter(adapter);
     recyclerView.scrollToPosition(position);
 }

    private int getSpanCount() {
        int spanCount = 2;
        if (getActivity() != null) {
            int orientation = getActivity().getResources()
                    .getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                spanCount = 3;
            }
        }
        return spanCount;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (venuesList.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
            loadVenues();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.VENUE_LIST, venuesList);
        outState.putInt(Constants.NEXT_PAGE, page);
    }

    protected void loadVenues() {
        if (!isFavoritesFragment) {
            recyclerView.setIsLoading(true);

        } else {
            recyclerView.setState(Constants.LOADING_FAVORITES);
        }
    }

  private int setFavFromRestore(){
      Venue fav = FavoriteVenueFragment.getFavoritedVenue(getActivity().getApplicationContext());
      if(fav!=null){
          int length = venuesList.size();
         for(int i =0; i<length; i++){
             if(venuesList.get(i).getId().equals(fav.getId())){
                 return i;
             }

         }
      }
      return -1;
   }
    protected void addVenues(ArrayList<Venue> venues) {

        venuesList=venues;

        int position = setFavFromRestore();
             if(position!=-1){
                 Log.d(TAG,"VALUES");
                 Venue venue = venuesList.get(position);
                 venue.setFavorite(true);
                 venuesList.remove(position);
                 venuesList.add(0,venue);
             }
        adapter.AddAll(venuesList);

        //for future pagination loading
        updateList();
    }
  public void  setFavPositionAtZero(int position, boolean fav){

      if(!isFavoritesFragment) {
          Venue venue = venuesList.get(position);
          if(fav){
              venue.setFavorite(true);
              venuesList.remove(position);
              venuesList.add(0,venue);
          }else{
              venue.setFavorite(false);
          }
          adapter.AddAll(venuesList);
          recyclerView.scrollToPosition(position);
          adapter.notifyItemMoved(position, 0);
      }
  }

    private void updateList() {
        recyclerView.setIsLoading(false);
        if (page == 1) {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.loadingComplete();
            return;
        }
    }

    protected void retrievalError(int code) {
        recyclerView.setIsLoading(false);

         if(code==Constants.LOADING_FAVORITES){
             progressBar.setVisibility(View.INVISIBLE);
             recyclerView.setVisibility(View.INVISIBLE);
         }
        if (page == 1) {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        recyclerView.setState(code);
    }

    protected void addFavoriteVenue(ArrayList<Venue> venues) {
        this.venuesList.clear();
        if (venues == null) {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setState(Constants.NONE);
            return;
        }

        this.venuesList.addAll(venues);

        if(adapter!=null){
            adapter.AddAll(venuesList);
        }else{
            setAdapter(venuesList);
        }
        progressBar.setVisibility(View.INVISIBLE);
        if (venues.size() == 0) {
            recyclerView.setState(Constants.NONE);
        } else {
            recyclerView.setState(Constants.DONE);
        }
    }


    protected void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    private class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public SpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            outRect.bottom = spacing;
            outRect.right = spacing;


            int childAdapterPosition = parent.getChildAdapterPosition(view);
            int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            if (childAdapterPosition < spanCount) {
                outRect.top = spacing;
            }

            if (childAdapterPosition % spanCount == 0) {
                outRect.left = spacing;
            }
        }
    }
}
