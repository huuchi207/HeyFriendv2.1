package com.chi.heyfriendv21.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;

import adapter.CommentRecyclerViewAdapter;
import adapter.PrivateMessagesForRecyclerViewAdapter;
import common.CommentViewHolder;
import common.CommonMethod;
import common.Constant;
import common.MessageViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;
import object.Comment;
import object.Message;
import object.Post;

/**
 * Created by huuchi207 on 12/05/2017.
 */

public class PostActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "PostActivity";
    private TextView tvName, tvContent, tvTime;
    private CircleImageView civAvatar;
    private ImageView ivImage;
    private RecyclerView rvComment;
    private Post currentPost;

    private CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    private TextView tvCountOfComment, tvCountOfLike;
    private LikeButton btLike;
    private ImageView btSend;
    private EditText etComment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_post);
        getDataIntent();
        initView();
        setData();
        listenDataChangingOnServer();
    }

    private void setData() {
        tvName.setText(currentPost.getName());
        tvTime.setText(CommonMethod.diffTime(this, currentPost.getTime()));
        tvContent.setText(currentPost.getContent());
        Glide.with(this).load(currentPost.getUrlAvatar()).into(civAvatar);
        Glide.with(this).load(currentPost.getUrlImage()).into(ivImage);
        tvCountOfComment.setText(currentPost.getCountOfComment()+" "+ getString(R.string.txt_comments));
        tvCountOfLike.setText(currentPost.getCountOfLike()+" "+ getString(R.string.txt_likes));
        btLike.setLiked(false);
        if (currentPost.getLikes()!= null){
            for (String uid : currentPost.getLikes()) {
                if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    btLike.setLiked(true);
                    break;
                }
            }
        }

    }

    private void getDataIntent() {
        Intent intent = getIntent();
        if (intent!= null){
            currentPost = (Post) intent.getSerializableExtra("data");
        }
    }

    private void initView() {
        tvName = (TextView) findViewById(R.id.tvName_ActivityPost);
        tvContent = (TextView) findViewById(R.id.tvContent_ActivityPost);
        tvTime = (TextView) findViewById(R.id.tvTime_ActivityPost);
        civAvatar = (CircleImageView) findViewById(R.id.civAvatar_ActivityPost);
        ivImage = (ImageView) findViewById(R.id.ivImage_ActivityPost);
        rvComment = (RecyclerView) findViewById(R.id.rv_list_comment_ActivityPost);
        tvCountOfLike = (TextView) findViewById(R.id.tvCountOfLike_ActivityPost);
        tvCountOfComment = (TextView) findViewById(R.id.tvCountOfComment_ActivityPost);
        btLike = (LikeButton) findViewById(R.id.btLike_ActivityPost);
        btSend = (ImageView) findViewById(R.id.ivSend_ActivityPost);
        etComment = (EditText) findViewById(R.id.etComment_ActivityPost);
        btSend.setOnClickListener(this);
        btLike.setOnClickListener(this);



        commentRecyclerViewAdapter = new
                CommentRecyclerViewAdapter(Comment.class,
                R.layout.item_comment_list,
                CommentViewHolder.class,
                FirebaseDatabase.getInstance().getReference(currentPost.getFirebaseUrl()+"/comments"),
                PostActivity.this, this);

        commentRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = commentRecyclerViewAdapter.getItemCount();
                int lastVisiblePosition =linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    rvComment.scrollToPosition(positionStart);
                }
            }
        });
        linearLayoutManager= new LinearLayoutManager(this);
        rvComment.setLayoutManager(linearLayoutManager);
        rvComment.hasFixedSize();
        rvComment.setAdapter(commentRecyclerViewAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view==btLike){
            if (btLike.isLiked()){
                Log.e(TAG, "btLike.isLiked");
                btLike.setLiked(false);
                FirebaseDatabase.getInstance().getReference(currentPost.getFirebaseUrl()+"/likes/"+
                FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
            }
            else {
                Log.e(TAG, "btLike.isnotLiked");
                btLike.setLiked(true);
                FirebaseDatabase.getInstance().getReference(currentPost.getFirebaseUrl()+"/likes/"+
                        FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
            }
        }
        else if (view == btSend){
            String comment = etComment.getText().toString().trim();
            if (comment.equals("")){
                Toast.makeText(PostActivity.this, getString(R.string.txt_write_something), Toast.LENGTH_SHORT).show();
            }
            else {
                FirebaseDatabase.getInstance().getReference(currentPost.getFirebaseUrl()+"/comments")
                        .push()
                        .setValue
                                (new Comment(comment,
                                        CommonMethod.getCurrentTime(),
                                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                        FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString(),
                                        FirebaseAuth.getInstance().getCurrentUser().getUid()
                        ));
                etComment.setText("");
            }
        }
    }
    private void listenDataChangingOnServer(){
        FirebaseDatabase.getInstance().getReference(currentPost.getFirebaseUrl()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("comments")){
                    tvCountOfComment.setText(dataSnapshot.child("comments").getChildrenCount()+" "+getString(R.string.txt_comments));
                }
                if (dataSnapshot.hasChild("likes")){
                    Log.e(TAG, "dataSnapshot.hasChild(\"likes\")){"+ dataSnapshot.child("likes").getChildrenCount());
                    tvCountOfLike.setText(dataSnapshot.child("likes").getChildrenCount()+" "+ getString(R.string.txt_likes));
                    for(DataSnapshot likesDs: dataSnapshot.child("likes").getChildren()){
                        if (likesDs.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            btLike.setLiked(true);
                            break;
                        }
                    }
                }
                else tvCountOfLike.setText("0 likes");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
