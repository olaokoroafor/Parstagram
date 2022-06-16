package com.example.parstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.PostDetailActivity;
import com.example.parstagram.R;
import com.example.parstagram.models.Post;

import java.util.List;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.ViewHolder> {

    private List<Post> allPosts;
    private LayoutInflater mInflater;
    private Context context;
    private ImageView ivPostPic;

    // data is passed into the constructor
    public ProfilePostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.allPosts = posts;
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_post, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = allPosts.get(position);
        holder.bind(post);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return allPosts.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ivPostPic = itemView.findViewById(R.id.ivGridPic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Post post = allPosts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(Post.class.getSimpleName(), post);
                context.startActivity(intent);
            }

        }

        public void bind(Post post) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop());
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(post.getImage().getUrl()).into(ivPostPic);
        }
    }

}

