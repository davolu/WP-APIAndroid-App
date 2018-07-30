package davolusoft.davidoluyale.wordpressapp;

/**
 * Created by David Oluyale on 6/29/2018.
 */

public class CatPostAdapter {



    public String PostImageURL;
    public String PostTitle;
    public  String PostDescription;

    public String getPostImageURL() {
        return PostImageURL;
    }

    public void setPostImageURL(String imageServerUrl) {
        this.PostImageURL = imageServerUrl;
    }

    public String getPostTitle() {
        return PostTitle;
    }

    public void setPostTitle(String postTitle) {
        PostTitle = postTitle;
    }

    public String getPostDescription() {
        return PostDescription;
    }

    public void setPostDescription(String postDescription) {
        PostDescription = postDescription;
    }
}
