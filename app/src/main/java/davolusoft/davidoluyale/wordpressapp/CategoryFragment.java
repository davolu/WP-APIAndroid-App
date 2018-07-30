package davolusoft.davidoluyale.wordpressapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    public   String BASE_URL = null;
    public static ProgressDialog dialog;

    JsonArrayRequest RequestOfJSonArray;

    RequestQueue requestQueue;
    ListView listView;
    ArrayList<String> idList;

    List<String> your_array_list, ar2;
    public CategoryFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_category, container, false);
      //  ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
        //        R.layout.listv, mobileArray);

        String siteurl = ((MyApplication) getActivity().getApplication()).getsiteUrl();
        BASE_URL = "http://"+siteurl+"/wp-json/wp/v2/categories";

        MobileAds.initialize(getActivity(),
                "ca-app-pub-5673517581572853~8389090714");

        AdView mAdView = root.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        GET_CATEGORY();

          listView = (ListView) root.findViewById(R.id.cateogry);

        return  root;
    }



    public void GET_CATEGORY() {



  //   dialog = ProgressDialog.show(getActivity(), "",
             //  "Fetching Category...", true);


// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =BASE_URL;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        // Instanciating an array list (you don't need to do this,
                        // you already have yours).
                        your_array_list = new ArrayList<String>();
                        ar2 = new ArrayList<String>();

                        // This is the array adapter, it takes the context of the activity as a
                        // first parameter, the type of list view as a second parameter and your
                        // array as a third parameter.
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_list_item_1,
                                your_array_list );



                        for(int i = 0; i<response.length(); i++)
                        {
                            JSONObject json = null;

                            try {

                         JSONArray       res = new JSONArray(response.toString());
                                json = res.getJSONObject(i);
                                your_array_list.add(json.getString("name"));
                                ar2.add(json.getString("id"));

                                // idList.add(json.getString("id"));
                            }  catch (JSONException e)
                            {

                                e.printStackTrace();
                            }
                        }
                        //   Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                        // ListView setOnItemClickListener function apply here.

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                // TODO Auto-generated method stub
                                //  Toast.makeText(getActivity(), your_array_list.get(position), Toast.LENGTH_SHORT).show();
                                Intent catp = new Intent(getActivity(), CategorizedPostActivity.class);
                                catp.putExtra("category_name", your_array_list.get(position));
                                catp.putExtra("category_id", ar2.get(position));


                                startActivity(catp);

                            }
                        });

                        listView.setAdapter(arrayAdapter);
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                //mTextView.setText("That didn't work!");
           //      Toast.makeText(getActivity(), "The error is: "+error.toString(), Toast.LENGTH_LONG).show();

               // dialog.dismiss();

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


/*
        RequestOfJSonArray = new JsonArrayRequest(BASE_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
//Toast.makeText(getActivity(),"the category"+ response.toString(),Toast.LENGTH_LONG).show();

                        // Instanciating an array list (you don't need to do this,
                        // you already have yours).
                        your_array_list = new ArrayList<String>();
                        ar2 = new ArrayList<String>();

                        // This is the array adapter, it takes the context of the activity as a
                        // first parameter, the type of list view as a second parameter and your
                        // array as a third parameter.
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_list_item_1,
                                your_array_list );



                        for(int i = 0; i<response.length(); i++)
                        {
                            JSONObject json = null;

                            try {

                                json = response.getJSONObject(i);
                                your_array_list.add(json.getString("name"));
                                ar2.add(json.getString("id"));

                                // idList.add(json.getString("id"));
                            }  catch (JSONException e)
                            {

                                e.printStackTrace();
                            }
                        }
                        //   Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                        // ListView setOnItemClickListener function apply here.

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                // TODO Auto-generated method stub
                              //  Toast.makeText(getActivity(), your_array_list.get(position), Toast.LENGTH_SHORT).show();
                                Intent catp = new Intent(getActivity(), CategorizedPostActivity.class);
                                catp.putExtra("category_name", your_array_list.get(position));
                              catp.putExtra("category_id", ar2.get(position));


                                startActivity(catp);

                            }
                        });

                        listView.setAdapter(arrayAdapter);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //    dialog.dismiss();

                    }
                });


        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(RequestOfJSonArray);
*/

    }







}
