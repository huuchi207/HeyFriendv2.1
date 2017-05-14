package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chi.heyfriendv21.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.TimelineRecyclerViewAdapter;
import common.Constant;
import object.Post;



public class TimelineFragment extends Fragment {
    private static final String TAG = "TimelineFragment";
    private List<Post> posts;
    TimelineRecyclerViewAdapter timelineRecyclerViewAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        posts = new ArrayList<>();
        RecyclerView rvTimeline = (RecyclerView) view.findViewById(R.id.rvTimeline_InTimelineFragment);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTimeline.setHasFixedSize(true);
        timelineRecyclerViewAdapter = new TimelineRecyclerViewAdapter(posts, getActivity());
        rvTimeline.setAdapter(timelineRecyclerViewAdapter);

//        buildSample();
        getDataFromServer();
        return view;

    }

//    private void buildSample() {
//        posts.clear();
//        for (int i = 0; i < 10; i++) {
//            posts.add(new Post("", 12232, " ", "" ,"", null));
//        }
//        timelineRecyclerViewAdapter.notifyDataSetChanged();
//
//    }
    private void getDataFromServer(){
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                DataSnapshot friendUidsDs = dataSnapshot.child("friends");
                DataSnapshot timelinesDs = dataSnapshot.child("timeline");
                DataSnapshot myTimeline = timelinesDs.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ArrayList<String> like;
                //get my timeline
                if (myTimeline!= null){
                    for (DataSnapshot postDs : myTimeline.getChildren()){
                        Post post = new Post();
                        if (postDs.hasChild("content"))
                            post.setContent(postDs.child("content").getValue().toString());
                        if (postDs.hasChild("name"))
                            post.setName(postDs.child("name").getValue().toString());
                        if(postDs.hasChild("time"))
                            post.setTime((long) postDs.child("time").getValue());
                        if(postDs.hasChild("urlAvatar"))
                            post.setUrlAvatar(postDs.child("urlAvatar").getValue().toString());
                        if(postDs.hasChild("urlImage"))
                            post.setUrlImage(postDs.child("urlImage").getValue().toString());
                        post.setFirebaseUrl("timeline/"+FirebaseAuth.getInstance().getCurrentUser().getUid()
                                +"/"+postDs.getKey());

                        if (postDs.hasChild("likes")){
                            like = new ArrayList<String>();
                            for(DataSnapshot likeDs : postDs.child("likes").getChildren()){
                                like.add(likeDs.getKey());
                            }
                            post.setLikes(like);
                        }

                        if (postDs.hasChild("comments")){
                            post.setCountOfComment(postDs.child("comments").getChildrenCount());
                        }
                        else post.setCountOfComment(0);
                        posts.add(post);
                        Log.e(TAG,"setFirebaseUrl: "+ post.getFirebaseUrl());
                    }
                    timelineRecyclerViewAdapter.notifyDataSetChanged();
                }
                //get friend's timeline
                if (friendUidsDs==null) return;
                for(DataSnapshot friendUidDs: friendUidsDs.getChildren()){
                    String userUid = friendUidDs.getKey();
                    DataSnapshot friendTimeline = timelinesDs.child(userUid);
                    if (friendTimeline!= null){
                        for (DataSnapshot postDs : friendTimeline.getChildren()){
                            Post post = new Post();
                            if (postDs.hasChild("content"))
                                post.setContent(postDs.child("content").getValue().toString());
                            if (postDs.hasChild("name"))
                                post.setName(postDs.child("name").getValue().toString());
                            if(postDs.hasChild("time"))
                                post.setTime((long) postDs.child("time").getValue());
                            if(postDs.hasChild("urlAvatar"))
                                post.setUrlAvatar(postDs.child("urlAvatar").getValue().toString());
                            if(postDs.hasChild("urlImage"))
                                post.setUrlImage(postDs.child("urlImage").getValue().toString());
                            post.setFirebaseUrl("timeline/"+userUid
                                    +"/"+postDs.getKey());
                            if (postDs.hasChild("likes")){
                                like = new ArrayList<String>();
                                for(DataSnapshot likeDs : postDs.child("likes").getChildren()){
                                    like.add(likeDs.getKey());
                                }
                                post.setLikes(like);
                            }

                            if (postDs.hasChild("comments")){
                                post.setCountOfComment(postDs.child("comments").getChildrenCount());
                            }
                            else post.setCountOfComment(0);
                            posts.add(post);
                            Log.e(TAG,"post: "+ post);
                        }
                        timelineRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
