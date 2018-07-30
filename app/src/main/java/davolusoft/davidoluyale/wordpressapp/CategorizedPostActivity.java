package davolusoft.davidoluyale.wordpressapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategorizedPostActivity extends AppCompatActivity
{


    List<PostAdapter> ListOfdataAdapter;


    RecyclerView recyclerView;

    TextView txtv;



    JsonArrayRequest RequestOfJSonArray;

    RequestQueue requestQueue;

    View view;
    public  static  String BASE_URL="";
    int RecyclerViewItemPosition;

    RecyclerView.LayoutManager layoutManagerOfrecyclerView;

    RecyclerView.Adapter recyclerViewadapter;

    ArrayList<String> ImageTitleNameArrayListForClick;
    ArrayList<String> ImageTitleNameArrayListForClickContent;
    ArrayList<String> ImageTitleNameArrayListForClickImage;
    ArrayList<String> ImageTitleNameArrayListForClickURL;


    public static ProgressDialog dialog;
    String catid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorized_post);



        MobileAds.initialize(this,
                "ca-app-pub-5673517581572853~8389090714");

        AdView mAdView = (AdView) findViewById(R.id.adViewcpost);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Intent getPost = getIntent();

        String caten = getPost.getStringExtra("category_name");
          catid = getPost.getStringExtra("category_id");


        setTitle(caten);
        // add back arrow to toolbar
        if (getSupportActionBar() != null)
        {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        ImageTitleNameArrayListForClick = new ArrayList<>();
        ImageTitleNameArrayListForClickContent = new ArrayList<>();
        ImageTitleNameArrayListForClickImage = new ArrayList<>();
        ImageTitleNameArrayListForClickURL = new ArrayList<>();


        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.postcategoryrecyclerview_id);

   //    txtv = (TextView) findViewById(R.id.textView);


        recyclerView.setHasFixedSize(true);


        layoutManagerOfrecyclerView = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);
        String siteurl = ((MyApplication) this.getApplication()).getsiteUrl();

        if(isNumeric(catid))
        {
            BASE_URL ="http://"+siteurl+"/wp-json/wp/v2/posts?categories="+catid+"&_embed=&per_page=30&page=1" ;

        }
        else
        {
            BASE_URL ="http://"+siteurl+"/wp-json/wp/v2/posts?search="+catid+"&_embed=&per_page=30&page=1" ;

        }
           //"https://techpoint.ng/wp-json/wp/v2/posts?filter[cat]="+caten+"&per_page=10&page=1";

        JSON_HTTP_CALL();

        // Implementing Click Listener on RecyclerView.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(CategorizedPostActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                view = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if ((view != null) && gestureDetector.onTouchEvent(motionEvent)) {

                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(view);

                    // Showing RecyclerView Clicked Item value using Toast.
                    //     Toast.makeText(getActivity(), ImageTitleNameArrayListForClick.get(RecyclerViewItemPosition), Toast.LENGTH_LONG).show();

                    //     String de[] =   ImageTitleNameArrayListForClick.get(RecyclerViewItemPosition).split("[k]") ;

                    //tic

                    Intent ReadPostActivityIntent = new Intent(CategorizedPostActivity.this,ReadPostActivity.class);
                    ReadPostActivityIntent.putExtra("title", ImageTitleNameArrayListForClick.get(RecyclerViewItemPosition));
                    ReadPostActivityIntent.putExtra("image", ImageTitleNameArrayListForClickImage.get(RecyclerViewItemPosition));
                    ReadPostActivityIntent.putExtra("pcontent", ImageTitleNameArrayListForClickContent.get(RecyclerViewItemPosition));
                    ReadPostActivityIntent.putExtra("purl", ImageTitleNameArrayListForClickURL.get(RecyclerViewItemPosition));


                    startActivity(ReadPostActivityIntent);



                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



    }


    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    public void JSON_HTTP_CALL() {



        //"https://www.panafri.com/app/api.php?Read_myproducts="+userID;

        dialog = ProgressDialog.show(this, "",
                "Fetching Posts ...", true);


        RequestOfJSonArray = new JsonArrayRequest(BASE_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //             Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();

                        ParseJSonResponse(response);
                        dialog.dismiss();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //    dialog.dismiss();
                        try {

                            if (error instanceof TimeoutError) {
                                //Time out error
                                dialog.dismiss();

                            }else if(error instanceof NoConnectionError){
                                //net work error
                                dialog.dismiss();
                            } else if (error instanceof AuthFailureError) {
                                //error
                                dialog.dismiss();
                            } else if (error instanceof ServerError) {
                                //Erroor
                                dialog.dismiss();
                            } else if (error instanceof NetworkError) {
                                //Error
                                dialog.dismiss();

                            } else if (error instanceof ParseError) {
                                //Error
                                dialog.dismiss();

                            }else{
                                //Error
                                dialog.dismiss();
                            }
                            //End


                        } catch (Exception e) {


                        }

                    }
                });


        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(RequestOfJSonArray);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {

          //

            if(isNumeric(catid))
            {
                finish(); // close this activity and return to preview activity (if there is any)

            }
            else
            {
               // Toast.makeText(this, "HOME", Toast.LENGTH_LONG).show();
                Intent MainI = new Intent(CategorizedPostActivity.this, MainActivity.class);
                startActivity(MainI);
            }


            }
            else
        {
           // Toast.makeText(this, "OTHER", Toast.LENGTH_LONG).show();

        }

        return super.onOptionsItemSelected(item);

    }



    public void ParseJSonResponse(JSONArray array) {



        for(int i = 0; i<array.length(); i++) {

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

                GetDataAdapter2.setPostDescription(  Html.fromHtml( getExcerpt.getString("rendered") ).toString()   );

                GetDataAdapter2.setPostImageURL(getImg3.getString("source_url").replace("https","http"));

                ImageTitleNameArrayListForClick.add( getT.getString("rendered"));

                ImageTitleNameArrayListForClickImage.add(getImg3.getString("source_url").replace("https","http"));

                ImageTitleNameArrayListForClickContent.add(getContent.getString("rendered"));
                ImageTitleNameArrayListForClickURL.add(json.getString("link"));

            }
            catch (JSONException e) {

                e.printStackTrace();
            }

            ListOfdataAdapter.add(GetDataAdapter2);
        }

        recyclerViewadapter = new PostRecyclerViewAdapter(ListOfdataAdapter, this);

        recyclerView.setAdapter(recyclerViewadapter);
        dialog.dismiss();

        //ends
    }



}
