package com.example.parstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.R;
import com.example.parstagram.adapters.ProfilePostsAdapter;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final int RESULT_OK = 0;
    public String photoFileName = "profile_photo.jpg";
    private File photoFile;
    private RecyclerView rvPosts;
    public static final String TAG = "Profile Fragment";
    private ProfilePostsAdapter adapter;
    public List<Post> allPosts;
    private ImageView ivPfp;
    private ImageView ivAddPfp;
    private TextView tvUsername;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvProfilePosts);
        ivPfp = view.findViewById(R.id.ivProfilePfp);
        tvUsername = view.findViewById(R.id.tvprofileUsername);
        ivAddPfp = view.findViewById(R.id.ivAddPfp);
        ivAddPfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
                ParseUser user = ParseUser.getCurrentUser();
                user.put("profileImage", new ParseFile(photoFile));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Error saving post: " + e);
                            Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.i(TAG, "Post save was successful!");
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(100));
                            Glide.with(getContext()).applyDefaultRequestOptions(requestOptions).load(user.getParseFile("profileImage").getUrl()).into(ivPfp);
                        }
                    }
                });
            }
        });

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(90));
        ParseFile pfp_url = ParseUser.getCurrentUser().getParseFile("profileImage");
        if (pfp_url != null){
            Glide.with(getContext()).applyDefaultRequestOptions(requestOptions).load(pfp_url.getUrl()).into(ivPfp);
        }
        else{
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getResources().getIdentifier("ic_baseline_face_24", "drawable", getActivity().getPackageName())).into(ivPfp);
        }

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());

        allPosts = new ArrayList<Post>();

        rvPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new ProfilePostsAdapter(getContext(), allPosts);
        rvPosts.setAdapter(adapter);
        queryUserPosts();



    }

    private void queryUserPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        // include data referred by user key
        query.include(Post.USER_KEY);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            /*
            if (resultCode != RESULT_OK) { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            } else {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostPic.setImageBitmap(takenImage);
            }*/
            // by this point we have the camera photo on disk
            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            // RESIZE BITMAP, see section below
            // Load the taken image into a preview
            ivPfp.setImageBitmap(takenImage);

        }
    }
}