package davolusoft.davidoluyale.wordpressapp;

import android.app.Application;

/**
 * Created by David Oluyale on 7/5/2018.
 */
public class MyApplication extends Application {

    private String siteUrl;
    private String siteLabel;

    public String getSiteLabel() {
        return siteLabel;
    }

    public void setSiteLabel(String siteLabel) {
        this.siteLabel = siteLabel;
    }


    public String getsiteUrl() {
        return siteUrl;
    }

    public void setsiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }
}