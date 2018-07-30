package davolusoft.davidoluyale.wordpressapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by David Oluyale on 6/25/2018.
 */



public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>
{

    Context context;

    List<PostAdapter> dataAdapters;

    ImageLoader imageLoader;

    public PostRecyclerViewAdapter(List<PostAdapter> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.postcardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;

    }



    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position)
    {

        PostAdapter dataAdapterOBJ =  dataAdapters.get(position);

        imageLoader = ImageAdapter.getInstance(context).getImageLoader();

        imageLoader.get(dataAdapterOBJ.getPostImageURL(),
                ImageLoader.getImageListener(
                        Viewholder.PostImageURL,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );


        Viewholder.PostImageURL.setImageUrl(dataAdapterOBJ.getPostImageURL(), imageLoader);
       Viewholder.PostTitle.setText(dataAdapterOBJ.getPostTitle());
       Viewholder.PostDescription.setText(dataAdapterOBJ.getPostDescription());
       Viewholder.PostLink.setText(dataAdapterOBJ.getPostLink());




    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView PostTitle, PostDescription;
        public NetworkImageView PostImageURL ;
        public  EditText PostLink;

        public ViewHolder(View itemView)
        {

            super(itemView);

            PostTitle = (TextView) itemView.findViewById(R.id.postTitle1) ;

            PostImageURL = (NetworkImageView) itemView.findViewById(R.id.postImageURL1) ;
           PostDescription = (TextView) itemView.findViewById(R.id.postDescription1) ;
         PostLink = (EditText) itemView.findViewById(R.id.postlink1) ;

        }
    }
}