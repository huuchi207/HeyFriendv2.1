package adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;

import java.util.ArrayList;

import common.CommonMethod;
import de.hdodenhof.circleimageview.CircleImageView;
import object.User;

/**
 * Created by Tran Truong on 11/8/2016.
 */

public class ListFriendAdapter extends BaseAdapter {
    private Activity context;
    int reslayout;
    ArrayList<User> users;
    public ListFriendAdapter(Activity context, int reslayout, ArrayList<User> users) {
        this.context = context;
        this.reslayout = reslayout;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int i) {
        if (users.isEmpty())
            return null;
        else return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final viewHolder viewHolder;
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(reslayout, null);
            viewHolder = new viewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (viewHolder) convertView.getTag();

        User users = this.users.get(position);
        viewHolder.fullname.setText(users.getName());
        viewHolder.latesttime.setText(CommonMethod.diffTimeForListFriend(context,users.getLastOnline()));
        Glide.with(context).load(users
                .getPhotoURL()).into(viewHolder.avatar);

        return convertView;
    }
    static class viewHolder {
        View convertView;
        CircleImageView avatar;
        TextView fullname;
        TextView latesttime;
        public viewHolder(View convertView) {
            this.convertView = convertView;
            avatar = (CircleImageView) convertView.findViewById(R.id.avatar_friend);
            fullname = (TextView) convertView.findViewById(R.id.fullname_friend);
            latesttime = (TextView) convertView.findViewById(R.id.timelatest);
        }
    }
}
