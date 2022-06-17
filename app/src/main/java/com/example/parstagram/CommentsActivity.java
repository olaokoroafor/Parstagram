package com.example.parstagram;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.adapters.CommentsAdapter;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    public static final String TAG = "Comments Activity";
    private Post post;
    RecyclerView rvComments;
    List<Comment> comments;
    CommentsAdapter adapter;
    private ImageView ivProfilePic;
    private ImageView ivCommentSubmit;
    private EditText etCommentBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        post = (Post) getIntent().getParcelableExtra(Post.class.getSimpleName());
        rvComments = findViewById(R.id.rvComments);
        String ID = post.getObjectId();
        Log.e(TAG, ID );
        //init the list of tweets and the adapter
        comments = new ArrayList<Comment>();
        adapter = new CommentsAdapter(this, comments);

        // recycler view set up: layout manage and the adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setAdapter(adapter);

        ivProfilePic = findViewById(R.id.ivCommenterPfp);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(65));
        ParseFile pfp_url = ParseUser.getCurrentUser().getParseFile("profileImage");
        if (pfp_url != null){
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(pfp_url.getUrl()).into(ivProfilePic);
        }
        else{
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getResources().getIdentifier("ic_baseline_face_24", "drawable", getPackageName())).into(ivProfilePic);
        }

        etCommentBody = findViewById(R.id.etCommentBody);
        ivCommentSubmit = findViewById(R.id.ivCommentSubmit);
        ivCommentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment new_comment = new Comment();
                new_comment.setBody(etCommentBody.getText().toString());
                new_comment.setUser(ParseUser.getCurrentUser());
                new_comment.setPost(post);
                new_comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Error saving post: " + e);
                            //Toast.makeText(this, "Error while saving", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.i(TAG, "Post save was successful!");
                            int len = comments.size();
                            comments.add(new_comment);
                            etCommentBody.setText("");
                        }
                    }
                });
            }
        });


        queryComments();


    }

    private void queryComments() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);

        query.whereEqualTo("post", post);
        // include data referred by user key
        query.include(Comment.USER_KEY);
        query.include(Comment.POST_KEY);
        // order posts by creation date (newest first)
        query.addAscendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> db_comments, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Comment comment : db_comments) {
                    Log.i(TAG, "Comment: " + comment.getBody() + ", username: " + comment.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                comments.addAll(db_comments);
                adapter.notifyDataSetChanged();
            }
        });
    }
}