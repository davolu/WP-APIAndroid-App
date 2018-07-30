package davolusoft.davidoluyale.wordpressapp;

/**
 * Created by David Oluyale on 6/25/2018.
 */

public class PostAdapter {

    public String PostImageURL;
    public String PostTitle;
    public  String PostDescription;

    public String PostLink;

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


    public String getPostLink() {
        return PostLink;
    }

    public void setPostLink(String postLink) {
        PostLink = postLink;
    }

}
