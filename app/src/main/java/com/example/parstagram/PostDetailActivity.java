package com.example.parstagram;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.models.Post;
import com.parse.ParseFile;

public class PostDetailActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvLikeCount;
    private TextView tvCaptionUsername;
    private TextView tvCaption;
    private TextView tvPostedAt;
    private ImageView ivProfilePic;
    private ImageView ivPostPic;
    private ImageView ivLike;
    private ImageView ivComment;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        post = (Post) getIntent().getParcelableExtra(Post.class.getSimpleName());


        tvUsername = findViewById(R.id.tvDetailUsername);
        tvLikeCount = findViewById(R.id.tvDetailLikes);
        tvCaptionUsername = findViewById(R.id.tvDetailCaptionUsername);
        tvCaption = findViewById(R.id.tvDetailCaption);
        tvPostedAt = findViewById(R.id.tvDetailPostedAt);
        ivProfilePic = findViewById(R.id.ivDetailPfp);
        ivPostPic = findViewById(R.id.ivDetailPostPic);
        ivLike = findViewById(R.id.ivDetailLike);
        ivComment = findViewById(R.id.ivDetailComment);

        //bind data to view elements
        tvCaption.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        tvCaptionUsername.setText(post.getUser().getUsername());
        tvLikeCount.setText((new Integer(0).toString()) + " likes");
        tvPostedAt.setText(Post.getRelativeTimeAgo(post.getCreatedAt().toString()));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop());
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(post.getImage().getUrl()).into(ivPostPic);
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(65));
        ParseFile pfp_url = post.getUser().getParseFile("profileImage");
        if (pfp_url != null){
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(pfp_url.getUrl()).into(ivProfilePic);
        }
        else{
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getResources().getIdentifier("ic_baseline_face_24", "drawable", getPackageName())).into(ivProfilePic);
        }

    }
}

