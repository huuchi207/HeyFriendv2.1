package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chi.heyfriendv21.R;

import java.util.ArrayList;
import java.util.List;

import adapter.TimelineRecyclerViewAdapter;
import object.News;



public class TimelineFragment extends Fragment {
    private List<News> news;
    TimelineRecyclerViewAdapter timelineRecyclerViewAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        news = new ArrayList<>();
        RecyclerView rvTimeline = (RecyclerView) view.findViewById(R.id.rvTimeline_InTimelineFragment);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTimeline.setHasFixedSize(true);
        timelineRecyclerViewAdapter = new TimelineRecyclerViewAdapter(news, getActivity());
        rvTimeline.setAdapter(timelineRecyclerViewAdapter);

        buildSample();
        return view;

    }

    private void buildSample() {
        news.clear();
        for (int i = 0; i < 10; i++) {
            news.add(new News("", 12232, " ", ""));
        }
        timelineRecyclerViewAdapter.notifyDataSetChanged();

    }

}
