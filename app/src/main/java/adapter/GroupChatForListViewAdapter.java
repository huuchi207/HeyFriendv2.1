package adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import common.CommonMethod;
import de.hdodenhof.circleimageview.CircleImageView;
import object.GroupChatData;


public class GroupChatForListViewAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    ArrayList<GroupChatData> data;
    String currentUid;

    public GroupChatForListViewAdapter(Activity context, int layout, ArrayList<GroupChatData> data, String currentUid) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.currentUid = currentUid;


    }

    public ArrayList<GroupChatData> getData() {
        return data;
    }

    public void setData(ArrayList<GroupChatData> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public GroupChatData getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final viewHolder viewHolder;
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(layout, null);
            viewHolder = new viewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (viewHolder) convertView.getTag();

        //put data
        GroupChatData groupChatData = data.get(position);
        Map<String, String> names = new HashMap<>();
        for (GroupChatData.Participant participant : groupChatData.getParticipants()) {
            String uid = participant.getUid();
            String name = participant.getName();
            names.put(uid, name);

            if (participant.getUid().equals(currentUid)) {
                if (!participant.isStatus()) {
                    viewHolder.tvLastMessage.setTypeface(Typeface.DEFAULT_BOLD);
                }
            }
        }
        viewHolder.tvTime.setText(CommonMethod.diffTime(context, groupChatData.getLastMessage().getTime()));
        String senderUid = groupChatData.getLastMessage().getSenderUid();
        if (senderUid.equals(currentUid))
            viewHolder.tvLastMessage.setText(context.getString(R.string.txt_you) + ": " + groupChatData.getLastMessage().getContent());
        else {
            viewHolder.tvLastMessage.setText(names.get(senderUid) + ": " + groupChatData.getLastMessage().getContent());
            if (!groupChatData.getLastMessage().isStatus()) {
                viewHolder.tvLastMessage.setTypeface(Typeface.DEFAULT_BOLD);
            }
        }
        viewHolder.tvName.setText(groupChatData.getName());
        for (GroupChatData.Participant participant : groupChatData.getParticipants())
            if (participant.getUid().equals(groupChatData.getLastMessage().getSenderUid()))
                Glide.with(context).load(participant.getPhotoUrl()).into(viewHolder.civAvatar);

//        Log.e("group chat", "sfsd");
        return convertView;
    }

    static class viewHolder {
        View convertView;
        CircleImageView civAvatar;
        TextView tvName;
        TextView tvLastMessage;
        TextView tvTime;

        public viewHolder(View convertView) {
            this.convertView = convertView;
            civAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
            tvName = (TextView) convertView.findViewById(R.id.tvSenderName);
            tvLastMessage = (TextView) convertView.findViewById(R.id.tvLastMessage);
            tvTime = (TextView) convertView.findViewById(R.id.tvDate);

        }
    }
}
