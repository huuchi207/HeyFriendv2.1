package adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;

import java.util.ArrayList;

import common.CommonMethod;
import de.hdodenhof.circleimageview.CircleImageView;
import object.Friend;


/**
 * Created by huuchi207 on 17/10/2016.
 */

public class PrivateChatForListViewAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    ArrayList<Friend> data;
    String currentUid;
    public PrivateChatForListViewAdapter(Activity context, int layout, ArrayList<Friend> data, String currentUid) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.currentUid = currentUid;
    }

    public ArrayList<Friend> getData() {
        return data;
    }

    public void setData(ArrayList<Friend> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Friend getItem(int position) {
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
        Friend pcm= data.get(position);
        viewHolder.tvTime.setText(CommonMethod.diffTime(context, pcm.getLastMessage().getTime()));
        String senderUid= pcm.getLastMessage().getSenderUid();
        if (senderUid.equals(currentUid))
            viewHolder.tvLastMessage.setText(context.getString(R.string.txt_you)+": "+ pcm.getLastMessage().getContent());
        else {
            viewHolder.tvLastMessage.setText(pcm.getName()+": "+ pcm.getLastMessage().getContent());
            if (!pcm.getLastMessage().isStatus()){
                viewHolder.tvLastMessage.setTypeface(Typeface.DEFAULT_BOLD);
            }
        }
        viewHolder.tvName.setText(pcm.getName());
        Glide.with(context).load(pcm.getPhotoURL()).into(viewHolder.civAvatar);
        Log.e("private chat--", "dsdf");
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
            tvName= (TextView) convertView.findViewById(R.id.tvSenderName);
            tvLastMessage= (TextView) convertView.findViewById(R.id.tvLastMessage);
            tvTime= (TextView) convertView.findViewById(R.id.tvDate);

        }
    }


}