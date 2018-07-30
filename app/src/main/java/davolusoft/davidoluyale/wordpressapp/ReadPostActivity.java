package davolusoft.davidoluyale.wordpressapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.InputStream;
import java.util.Random;

public class ReadPostActivity extends AppCompatActivity {

    ImageView postImage;
    TextView postTitle;
    WebView postContent;
    EditText postURL;
    SharedPreferences ads_toggle = null;

    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);

        postTitle = (TextView) findViewById(R.id.readposttitle);
        postContent = (WebView) findViewById(R.id.readpostcontent);
        postImage = (ImageView) findViewById(R.id.readpostimage);
        postURL = (EditText) findViewById(R.id.postlinkurl);

        MobileAds.initialize(this,
                "ca-app-pub-5673517581572853~8389090714");

        AdView mAdView = (AdView) findViewById(R.id.adView34);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        AdView mAdView2 = (AdView) findViewById(R.id.adView34b);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        /*
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                // Load the next interstitial.
              //  InterstitialAd.loadAd(new AdRequest.Builder().build());

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                          //      Log.i("tag", "This'll run 300 milliseconds later");
                             //   mInterstitialAd.show();
                            //show ads after 15 seconds of reading post.

                            }
                        },
                        15000);
            }

        });
*/
        //get post infos...

        Intent getPost = getIntent();

        String posttitle = getPost.getStringExtra("title");
        String postimage = getPost.getStringExtra("image");
        String postcontent = getPost.getStringExtra("pcontent");
        String purl = getPost.getStringExtra("purl");

       // Toast.makeText(ReadPostActivity.this, postimage, Toast.LENGTH_LONG).show();
       // getActionBar().setTitle(posttitle);
      //  getSupportActionBar().setTitle(posttitle);
     //   Toast.makeText(this, posttitle, Toast.LENGTH_LONG);

        postURL.setText(purl);
        postContent.getSettings().setJavaScriptEnabled(true);

      //   postContent.getSettings().setAppCacheEnabled(true);
        postContent.getSettings().setBuiltInZoomControls(true);

        postContent.loadDataWithBaseURL("", postcontent, "text/html", "UTF-8", "");




        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
        {

             postTitle.setText(Html.fromHtml(posttitle,Html.FROM_HTML_MODE_LEGACY));

               setTitle(postTitle.getText());

             // postContent.setText(Html.fromHtml(postcontent,Html.FROM_HTML_MODE_LEGACY));

                }
              else {

            //postContent.setText(Html.fromHtml(postcontent));
             postTitle.setText(Html.fromHtml(posttitle));
             setTitle(postTitle.getText());

                   }
               new DownloadImageTask((ImageView) findViewById(R.id.readpostimage))
                        .execute(postimage);



        // add back arrow to toolbar
        if (getSupportActionBar() != null)
        {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


    }



    public void shareNews(View view)
    {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = postURL.getText().toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Post");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Post"));

    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {




            finish(); // close this activity and return to preview activity (if there is any)
        }


         else if (item.getItemId() == R.id.action_settings) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = postURL.getText().toString();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Great App");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share App via"));

        //    return true;
/*
            String url = "http://www.remioluyale.com/contact"; //link to contact page
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));

            startActivity(i);

            */


        }
        else
        {

        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }





    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
