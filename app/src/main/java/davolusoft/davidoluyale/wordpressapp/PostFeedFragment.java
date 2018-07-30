package davolusoft.davidoluyale.wordpressapp;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;

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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_WORLD_READABLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFeedFragment extends Fragment
{

    List<PostAdapter> ListOfdataAdapter;


    RecyclerView recyclerView;

    public   String BASE_URL = null;
    //"https://www.panafri.com/app/api.php";


    TextView txtv;



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

    public PostFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {


// get
        String siteurl = ((MyApplication) getActivity().getApplication()).getsiteUrl();
        BASE_URL = "http://"+siteurl+"/wp-json/wp/v2/posts?_embed=&per_page=25&page=1";

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post_feed, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.postrecyclerview_id1);


        getActivity().startService(new Intent(getActivity(), NewPostNotification.class));

        MobileAds.initialize(getActivity(),
                "ca-app-pub-5673517581572853~8389090714");

        AdView mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        ImageTitleNameArrayListForClick = new ArrayList<>();
        ImageTitleNameArrayListForClickContent = new ArrayList<>();
        ImageTitleNameArrayListForClickImage = new ArrayList<>();
        ImageTitleNameArrayListForClickURL = new ArrayList<>();

        ListOfdataAdapter = new ArrayList<>();


        txtv = (TextView) rootView.findViewById(R.id.appEMAIL);


        recyclerView.setHasFixedSize(true);

        layoutManagerOfrecyclerView = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

      JSON_HTTP_CALL();



  /*    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
      {



//Toast.makeText(getActivity(), "scrolling", Toast.LENGTH_LONG).show();

      });
*/
        // Implementing Click Listener on RecyclerView.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {


            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent)
                {

                    return true;


                }


            }
            );

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                view = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if ((view != null) && gestureDetector.onTouchEvent(motionEvent)) {

                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(view);

                    // Showing RecyclerView Clicked Item value using Toast.
                    //     Toast.makeText(getActivity(), ImageTitleNameArrayListForClick.get(RecyclerViewItemPosition), Toast.LENGTH_LONG).show();

                    //String de[] =   ImageTitleNameArrayListForClick.get(RecyclerViewItemPosition).split("[k]") ;



                    //tic tack toe

                     Intent ReadPostActivityIntent = new Intent(getActivity(),ReadPostActivity.class);
                    ReadPostActivityIntent.putExtra("title", ImageTitleNameArrayListForClick.get(RecyclerViewItemPosition));
                    ReadPostActivityIntent.putExtra("image", ImageTitleNameArrayListForClickImage.get(RecyclerViewItemPosition));
                    ReadPostActivityIntent.putExtra("pcontent", ImageTitleNameArrayListForClickContent.get(RecyclerViewItemPosition));
               ReadPostActivityIntent.putExtra("purl", ImageTitleNameArrayListForClickURL.get(RecyclerViewItemPosition));
                    ReadPostActivityIntent.putExtra("purl",ImageTitleNameArrayListForClickURL.get(RecyclerViewItemPosition));


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



        return rootView;
    }



    public void JSON_HTTP_CALL()
    {


         //"https://www.panafri.com/app/api.php?Read_myproducts="+userID;

        dialog = ProgressDialog.show(getActivity(), "",
                "Fetching Posts ...", true);


// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =BASE_URL;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                      dialog.dismiss();
                      //  Toast.makeText(getActivity(), "The response  is "+response.toString(), Toast.LENGTH_LONG).show();

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
           //    Toast.makeText(getActivity(), "The error is: "+error.toString(), Toast.LENGTH_LONG).show();

                dialog.dismiss();

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




            }

            catch (JSONException e)
            {

                e.printStackTrace();
            }

             //if(GetDataAdapter2 != null) {
                 ListOfdataAdapter.add(GetDataAdapter2);
             //}

        }

        recyclerViewadapter = new PostRecyclerViewAdapter(ListOfdataAdapter, getActivity());


             recyclerView.setAdapter(recyclerViewadapter);

        dialog.dismiss();

        //ends

    }





}