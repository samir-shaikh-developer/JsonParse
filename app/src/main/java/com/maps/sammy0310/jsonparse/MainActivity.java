package com.maps.sammy0310.jsonparse;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.jasonparse.models.MovieNodel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private GoogleApiClient client;
    private ListView lv;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading. Please wait...");
        lv = (ListView) findViewById(R.id.listView);


        // Create default options which will be used for every
  //displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
        .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public class JsonTask extends AsyncTask<String,String,List<MovieNodel>>{

        private List<MovieNodel> movieModelList;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<MovieNodel> doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                InputStream stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                String finalJson = buffer.toString();
                Log.w("final ", finalJson );
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("movies");

                Gson gson = new Gson();
                movieModelList = new ArrayList<>();
               for(int i=0 ; i < parentArray.length(); i++ ) {
                   JSONObject finalObj = parentArray.getJSONObject(i);
                   MovieNodel model = gson.fromJson(finalObj.toString(),MovieNodel.class);
//                   model.setMovie(finalObj.getString("movie"));
//                   model.setYear(finalObj.getInt("year"));
//                   model.setDirector(finalObj.getString("director"));
//                   model.setDuration(finalObj.getString("duration"));
//                   model.setImage(finalObj.getString("image"));
//                   model.setRating(finalObj.getDouble("rating"));
//                   model.setTagline(finalObj.getString("tagline"));
//                   model.setStory(finalObj.getString("story"));
//                   List<MovieNodel.Cast> castList = new ArrayList<>();

//                   for(int j=0;j<finalObj.getJSONArray("cast").length();j++){
//                        MovieNodel.Cast cast = new MovieNodel.Cast();
//                       cast.setName(finalObj.getJSONArray("cast").getJSONObject(j).getString("name"));
//                       castList.add(cast);
//                   }
//
//                   model.setCast(castList);
                   //adding final object
                   movieModelList.add(model);

               }
                return movieModelList;



                //return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;


        }



        @Override
        protected void onPostExecute(List<MovieNodel> s) {
            super.onPostExecute(s);
           progressDialog.dismiss();
            MovieAdapter adapter = new MovieAdapter(getApplicationContext(),R.layout.ow,s);
            lv.setAdapter(adapter);

        }
    }






    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.maps.sammy0310.jsonparse/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.maps.sammy0310.jsonparse/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();
        if(id == R.id.setting){

            new JsonTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
