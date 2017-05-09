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

        buildSample();
        getDataFromServer();
        return view;

    }

    private void buildSample() {
        posts.clear();
        for (int i = 0; i < 10; i++) {
            posts.add(new Post("", 12232, " ", ""));
        }
        timelineRecyclerViewAdapter.notifyDataSetChanged();

    }
    private void getDataFromServer(){
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                DataSnapshot friendUidsDs = dataSnapshot.child("friends");
                DataSnapshot timelinesDs = dataSnapshot.child("timeline");
                DataSnapshot myTimeline = timelinesDs.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                //get my timeline
                if (myTimeline!= null){
                    for (DataSnapshot postDs : myTimeline.getChildren()){
                        Post post = postDs.getValue(Post.class);
                        posts.add(post);
                        Log.e(TAG,"post: "+ post);
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
                            Post post = postDs.getValue(Post.class);
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
