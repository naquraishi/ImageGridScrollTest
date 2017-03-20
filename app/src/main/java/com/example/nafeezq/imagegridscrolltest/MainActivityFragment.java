package com.example.nafeezq.imagegridscrolltest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a GridView.
 */
public class MainActivityFragment extends android.support.v4.app.Fragment{

    //Creating ArrayLists for storing Poster images and title/synopsis/rating/release date Strings

    private ArrayList<Bitmap> mPoster = new ArrayList<>();
    private ArrayList<Bitmap> masterPoster = new ArrayList<>();

    private GridView gridView;



    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
//        gridView.setOnTouchListener(null);

        //Executing FetchPosterTask AsyncTask
        FetchPosterTask fetch = new FetchPosterTask();
        fetch.execute();


        //Setting up GridView onclickLister with intents to Movie Detail Activity class

        return rootView;

    }




    //Creating CustomGridAdapter to Populate GridView with Bitmaps

    public class CustomGridAdapter extends BaseAdapter {

        public FetchPosterTask mContext;
        public ArrayList<Bitmap> mThumbIds;

        // Constructor
        public CustomGridAdapter(FetchPosterTask posterTask, ArrayList<Bitmap> mPoster) {
            this.mContext = posterTask;
            this.mThumbIds = mPoster;

        }


        @Override
        public int getCount() {
            return mThumbIds.size();
        }

        @Override
        public Object getItem(int position) {
            return mThumbIds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView = null;
            if(convertView==null){

                imageView = new ImageView(getActivity());

            }else{
                imageView = (ImageView)convertView;
            }

            imageView.setImageBitmap(mThumbIds.get(position));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            return imageView;
        }

    }


    public class FetchPosterTask extends AsyncTask<String, Void, ArrayList<String>> {

        //ArrayList to hold complete URL Strings for different Posters from MovieDB
        private ArrayList<String> urlStringArray = new ArrayList<>();





        public ArrayList<String> getMoviePostersFromJson(String imageLocationStr) throws JSONException {


//            Map<Double,List<String>> popularDetailMap = new TreeMap<>();
//            Map<Double,List<String>> rateDetailMap = new TreeMap<>();


            //Defining Constants to parse Movie DB JSON Data

            final String MOVIEDB_BACKDROP_PATH = "file_path";
            final String MOVIEDB_BACKDROP_ARRAY = "backdrops";
            final String MOVIEDB_POSTER_PATH = "file_path";
            final String MOVIEDB_POSTER_ARRAY = "posters";




            JSONObject imagesMovies = new JSONObject(imageLocationStr);
            JSONArray backdropArray = imagesMovies.getJSONArray(MOVIEDB_BACKDROP_ARRAY);
            JSONArray posterArray = imagesMovies.getJSONArray(MOVIEDB_POSTER_ARRAY);


            for (int i = 0; i < backdropArray.length(); i++) {

                JSONObject movie = backdropArray.getJSONObject(i);

                String backdropImagePath = movie.getString(MOVIEDB_BACKDROP_PATH);

                URL posterUrl = null;
                try {
                    posterUrl = new URL("http://image.tmdb.org/t/p/w185/" + backdropImagePath);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String urlString = posterUrl.toString();
                urlStringArray.add(urlString);

            }


            for (int i = 0; i < posterArray.length(); i++) {

                JSONObject movie = posterArray.getJSONObject(i);

                String posterPath = movie.getString(MOVIEDB_POSTER_PATH);

                URL posterUrl = null;
                try {
                    posterUrl = new URL("http://image.tmdb.org/t/p/w185/" + posterPath);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String urlString = posterUrl.toString();
                urlStringArray.add(urlString);

            }


            //URL String to populate GridView with Bitmaps in decreasing Popularity or Rating order

            for (int i = urlStringArray.size() - 1; i >= 0; i--) {

                URL urlToOpen = null;
                try {
                    urlToOpen = new URL(urlStringArray.get(i));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    if (urlToOpen != null) {
                        mPoster.add(BitmapFactory.decodeStream(urlToOpen.openConnection().getInputStream()));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            for (Bitmap poster: mPoster){

                masterPoster.add(poster);
            }


            return urlStringArray;
        }

        @Override
        protected ArrayList<String> doInBackground (String...prams){

            //key removed for GitHub upload

            String key = "ea273786df2099f639aa7b821830c57c";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;
            ArrayList<String> moviesJsonStrArray = new ArrayList<>();

            try {

                ArrayList<String> baseURLArray = new ArrayList<>();

                baseURLArray.add("https://api.themoviedb.org/3/movie/550/images?");
                baseURLArray.add("https://api.themoviedb.org/3/movie/551/images?");
                baseURLArray.add("https://api.themoviedb.org/3/movie/552/images?");
                baseURLArray.add("https://api.themoviedb.org/3/movie/553/images?");
                baseURLArray.add("https://api.themoviedb.org/3/movie/554/images?");
                baseURLArray.add("https://api.themoviedb.org/3/movie/555/images?");
                baseURLArray.add("https://api.themoviedb.org/3/movie/557/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/558/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/559/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/560/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/561/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/562/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/563/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/564/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/565/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/567/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/568/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/570/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/571/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/572/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/573/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/574/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/575/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/576/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/577/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/578/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/579/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/580/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/581/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/582/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/583/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/584/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/585/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/586/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/587/images?");
//                baseURLArray.add("https://api.themoviedb.org/3/movie/588/images?");

                final String APIKEY_PARAM = "api_key";

                for (String baseURLString : baseURLArray) {


                    Uri builtUri = Uri.parse(baseURLString).buildUpon()
                            .appendQueryParameter(APIKEY_PARAM, key)
                            .build();

                    URL url = new URL(builtUri.toString());

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    moviesJsonStr = buffer.toString();


                    moviesJsonStrArray.add(moviesJsonStr);

                }



            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {

                for (String jsonStr: moviesJsonStrArray){

                    getMoviePostersFromJson(jsonStr);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute (ArrayList result){




                CustomGridAdapter adapter = new CustomGridAdapter(this,masterPoster);
                gridView.setAdapter(adapter);


                adapter.notifyDataSetChanged();
                gridView.post( new Runnable() {
                    @Override
                    public void run() {
                        //call smooth scroll

                        Integer totalNumber = masterPoster.size();

                        Log.d("Total Downloaded", totalNumber.toString());

                        gridView.setSelection(0);
                        gridView.smoothScrollToPositionFromTop(3679,5,10000);

                    }
                });


            gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    // TODO Auto-generated method stub
                }

                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        Log.i("a", "scrolling stopped...");

                        gridView.setSelection(0);
                        gridView.smoothScrollToPositionFromTop(3679,5,600);
                    }

                }
            });

        }

    }

    }










