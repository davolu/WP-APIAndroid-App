package davolusoft.davidoluyale.wordpressapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.internal.gmsg.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class NewPostNotification extends Service
{

    private Timer timer = new Timer();
    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences login_pref = null;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
       // Toast.makeText(this, "Starteed server", Toast.LENGTH_LONG).show();


        super.onCreate();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendRequestToServer();   //Your code here

            }
        }, 0, 10*60*1000);//10 Minutes
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

      public  void sendRequestToServer()
      {


// Instantiate the RequestQueue.
          RequestQueue queue = Volley.newRequestQueue(this);
          String siteurl = ((MyApplication) this.getApplication()).getsiteUrl();

          String url ="http://"+siteurl+"/wp-json/wp/v2/posts?_embed=&per_page=1&page=1";

// Request a string response from the provided URL.
          StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                  new Response.Listener<String>() {
                      @Override
                      public void onResponse(String response) {

                          JSONArray json = null;
                          try {
                              json = new JSONArray(response.toString());
                              // Toast.makeText(getActivity(), "The response is: "+json.toString(), Toast.LENGTH_LONG).show();

                              ParseJSonResponse(json);

                          } catch (JSONException e) {
                              e.printStackTrace();
                          }

                      }
                  }, new Response.ErrorListener()
          {
              @Override
              public void onErrorResponse(VolleyError error)
              {


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





//end function
      }



    public void ParseJSonResponse(JSONArray array)
    {

        String getid =null;
        String getT= null;
        String getExcerpt= null;
        JSONObject getContent= null;
        String getImg3 = null;
String thetitle="";
        for(int i = 0; i<array.length(); i++)
        {


            JSONObject json = null;

            try
            {


                json = array.getJSONObject(i);

                   getid = json.getString("id"); //extract the title
                  getT = json.getJSONObject("title").getString("rendered").toString(); //extract the title

                  getExcerpt = json.getJSONObject("excerpt").getString("rendered").toString(); //extract the title

                  getContent = json.getJSONObject("content"); //extract the title

                //extract the images
                JSONArray getImg1 = json.getJSONObject("_embedded").getJSONArray("wp:featuredmedia");
                JSONObject getImg2 = getImg1.getJSONObject(0).getJSONObject("media_details");
                  getImg3 = getImg2.getJSONObject("sizes").getJSONObject("thumbnail").getString("source_url");
              //  Toast.makeText(this, "The latest id:"+getid, Toast.LENGTH_LONG).show();


                thetitle =Html.fromHtml(getT).toString();

                //ends


            }

            catch (JSONException e)
            {

                e.printStackTrace();
            }

            break;

        }

        //if user is already logged in, redirect to dashboard
        SharedPreferences userDetails = NewPostNotification.this.getSharedPreferences("login_pref", MODE_PRIVATE);
        SharedPreferences.Editor edit = userDetails.edit();
        String lpid = userDetails.getString("latest_post_id", "");

        if(lpid.equals(getid))
        {

//Toast.makeText(this,"No new post foind", Toast.LENGTH_LONG).show();
        }
        else
        {

            //if id is new, store it and notify
            login_pref = NewPostNotification.this.getSharedPreferences("login_pref",
                    MODE_WORLD_READABLE);
            SharedPreferences.Editor login_pref_editor = login_pref.edit();
            login_pref_editor.putString("latest_post_id", getid );

            login_pref_editor.commit();


       //     Bitmap bitmap = getBitmapFromURL(getImg3);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo)
                            //  .setLargeIcon(bitmap)
                            .setContentTitle(Html.fromHtml(getT))
                            .setPriority(Notification.PRIORITY_MAX)
                            .setContentText(Html.fromHtml(getExcerpt ));


            int NOTIFICATION_ID = 12345;
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);


            //Intent targetIntent = new Intent(this, PostFeedFragment.class);
         // PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //PendingIntent contentIntent = PendingIntent.getBroadcast(this, 1,
              //      targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Intent myintent = new Intent(this, MainActivity.class);
            myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            int randomPIN = (int)(Math.random()*9000)+1000;
            PendingIntent conIntent = PendingIntent.getActivity(this, randomPIN,
                    myintent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(conIntent);



            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(NOTIFICATION_ID, builder.build());

        }



    }



    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}