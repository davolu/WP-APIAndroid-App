package davolusoft.davidoluyale.wordpressapp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public   String BASE_URL = null;

    RecyclerView recyclerView;

    TextView appTITLE, appEMAIL;
    SearchView searchView;


    TextView txtv;

    List<PostAdapter> ListOfdataAdapter;


    JsonArrayRequest RequestOfJSonArray;

    RequestQueue requestQueue;

    View view;


    int RecyclerViewItemPosition;

    RecyclerView.LayoutManager layoutManagerOfrecyclerView;

    RecyclerView.Adapter recyclerViewadapter;

    ArrayList<String> ImageTitleNameArrayListForClick;
    ArrayList<String> ImageTitleNameArrayListForClickContent;
    ArrayList<String> ImageTitleNameArrayListForClickImage;
    ArrayList<String> ImageTitleNameArrayListForClickURL;

    public static ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // TextView appTITLE = (TextView) findViewById(R.id.appTITLE);
      //  TextView appEMAIL = (TextView) findViewById(R.id.appEMAIL);

        // toolbar.setLogo(R.drawable.tech);


         setSupportActionBar(toolbar);

         ListOfdataAdapter = new ArrayList<>();

        ImageTitleNameArrayListForClick = new ArrayList<>();
        ImageTitleNameArrayListForClickContent = new ArrayList<>();
        ImageTitleNameArrayListForClickImage = new ArrayList<>();
        ImageTitleNameArrayListForClickURL = new ArrayList<>();

        txtv = (TextView) findViewById(R.id.appEMAIL);


     //  recyclerView.setHasFixedSize(true);

        //layoutManagerOfrecyclerView = new LinearLayoutManager(MainActivity.this);

      // recyclerView.setLayoutManager(layoutManagerOfrecyclerView);



/************************START SETTINGS***************/
        ((MyApplication) this.getApplication()).setsiteUrl("remioluyale.com");
        ((MyApplication) this.getApplication()).setSiteLabel("Remi Oluyale");



      //  appTITLE.setText("RemiOluyae");
      //  appEMAIL.setText("remioluyale@gmail.com");
      //3. change logo
        //4. change color value
        //5. change email and appname nav
        //6. change appname in string
//

        //show modal on demo version
        /*
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("DEMO Version !!!!")
                .setMessage("This is an unsigned DEMO VERSION for your app. To get a signed version and to remove this " +
                        "modal and activate the ads. Get the paid version for just N10,000 only. Click OK or outside this modal to close modal")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
                */
        //end show modal on demo version

        /************************END SETTINGS****************/









        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
        //    return true;

            String url = "Post title"; //link to contact page
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));

            startActivity(i);

        }
*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //some operation
                    return true;
                }
            });
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //some operation
                }
            });
            EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchPlate.setHint("Search");
            View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            // use this method for search process
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // use this method when query submitted
                  //Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();


                     searchHTTP(query);

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // use this method for auto complete search process
                    return false;
                }
            });
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        }

        return super.onCreateOptionsMenu(menu);
    }



public void  searchHTTP(final String query)
{




    Intent catp = new Intent(this, CategorizedPostActivity.class);
    catp.putExtra("category_name", "Results for "+query);
    catp.putExtra("category_id", query);


    startActivity(catp);
    /*

    String siteurl = ((MyApplication) this.getApplication()).getsiteUrl();
    BASE_URL = "http://"+siteurl+"/wp-json/wp/v2/posts?_embed=&per_page=25&page=1&search="+query;



    //"https://www.panafri.com/app/api.php?Read_myproducts="+userID;

    //dialog = ProgressDialog.show(MainActivity.this, "",
      //      "Getting Search Result.", true);


// Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
    String url =BASE_URL;

// Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                   // dialog.dismiss();
 // Toast.makeText(MainActivity.this, "The response  is "+response.toString()+" for query: "+query, Toast.LENGTH_LONG).show();

                    JSONArray json = null;
                    try {
                        json = new JSONArray(response.toString());
                        // Toast.makeText(getActivity(), "The response is: "+json.toString(), Toast.LENGTH_LONG).show();

                       ParseJSonResponse(json);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    //  mTextView.setText("Response is: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError error)
        {

            //mTextView.setText("That didn't work!");
//                Toast.makeText(MainActivity.this, "The error is: "+error.toString(), Toast.LENGTH_LONG).show();

          //  dialog.dismiss();

        }
    });


    stringRequest.setRetryPolicy(new RetryPolicy() {
        @Override
        public int getCurrentTimeout() {
            return 50000;
        }

        @Override
        public int getCurrentRetryCount() {
            return 50000;
        }

        @Override
        public void retry(VolleyError error) throws VolleyError {

        }
    });

    // Add the request to the RequestQueue.
    queue.add(stringRequest);

    */


}

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String siteurl = ((MyApplication) this.getApplication()).getsiteUrl();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent MainIntent = new Intent(this, MainActivity.class);
            startActivity(MainIntent);
        }

        else if (id == R.id.nav_about) {

            String url = "http://"+siteurl; //link to about page
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));

            startActivity(i);

        }

        else if (id == R.id.nav_contact)
        {
            String url ="http://"+siteurl; //link to contact page
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));

            startActivity(i);
        }

        /*
        else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Download our app via the playstore ";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Great App");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share App via"));

        }
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void ParseJSonResponse(JSONArray array)
    {




        for(int i = 0; i<array.length(); i++)
        {

            PostAdapter GetDataAdapter2 = new PostAdapter();

            JSONObject json = null;

            try
            {


                json = array.getJSONObject(i);

                JSONObject getT = json.getJSONObject("title"); //extract the title

                JSONObject getExcerpt = json.getJSONObject("excerpt"); //extract the title

                JSONObject getContent = json.getJSONObject("content"); //extract the title

                //extract the images
                JSONArray getImg1 = json.getJSONObject("_embedded").getJSONArray("wp:featuredmedia");
                JSONObject getImg2 = getImg1.getJSONObject(0).getJSONObject("media_details");
                JSONObject getImg3 = getImg2.getJSONObject("sizes").getJSONObject("thumbnail");


                GetDataAdapter2.setPostTitle(  Html.fromHtml(getT.getString("rendered")).toString() );

                GetDataAdapter2.setPostDescription(  Html.fromHtml( getExcerpt.getString("rendered") ).toString()  );

                GetDataAdapter2.setPostImageURL(getImg3.getString("source_url").replace("https","http"));

                GetDataAdapter2.setPostLink(getT.getString("rendered"));

                ImageTitleNameArrayListForClick.add( getT.getString("rendered"));

                ImageTitleNameArrayListForClickImage.add(getImg3.getString("source_url").replace("https","http"));

                ImageTitleNameArrayListForClickContent.add(getContent.getString("rendered"));

                ImageTitleNameArrayListForClickURL.add(json.getString("link"));


                Toast.makeText(MainActivity.this, "DONE", Toast.LENGTH_LONG).show();


            }

            catch (JSONException e)
            {

                e.printStackTrace();
            }

            //if(GetDataAdapter2 != null) {
           ListOfdataAdapter.add(GetDataAdapter2);
            //}

        }

//       recyclerViewadapter = new PostRecyclerViewAdapter(ListOfdataAdapter, this);


     //   recyclerView.setAdapter(recyclerViewadapter);

      //  dialog.dismiss();

        //ends

    }



}
