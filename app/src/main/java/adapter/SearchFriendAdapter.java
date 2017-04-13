package adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import object.User;

/**
 * Created by root on 01/11/2016.
 */

public class SearchFriendAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    ArrayList<User> data;
    ArrayList<User> allUser;


    public SearchFriendAdapter(Activity context, int layout, ArrayList<User> allUser) {
        this.context = context;
        this.layout = layout;

        this.allUser = allUser;
        this.data = new ArrayList<>();
    }

    public ArrayList<User> getData() {
        return data;
    }

    public void setData(ArrayList<User> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public User getItem(int position) {
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
        final User user = data.get(position);

        Glide.with(context).load(user.getPhotoURL()).into(viewHolder.civAvatar);
        viewHolder.tvName.setText(user.getName());
//        viewHolder.btSendInvitation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.startAnimation(Constant.buttonClick);
////                Log.e("sdfsdffsdsd",user.getName());
//                SendInvitationDialogFragment sendInvitationDialogFragment=
//                        new SendInvitationDialogFragment(myUid, user.getUid(), user.getName(), myName, myPhotoUrl);
//                sendInvitationDialogFragment.show(context.getFragmentManager(),"");
//            }
//        });
//        viewHolder.btBlockUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.startAnimation(Constant.buttonClick);
//                Toast.makeText(context,"Incoming...", Toast.LENGTH_SHORT).show();
//            }
//        });
        return convertView;
    }
    public boolean filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        if (charText.equals("")){
            data.clear();
            data.addAll(allUser);
            notifyDataSetChanged();
            return false;
        }

        data.clear();
        for (User user: allUser){
            if (user.getName().toLowerCase(Locale.getDefault()).contains(charText))
            {
                data.add(user);
            }
        }
        notifyDataSetChanged();
        return true;
    }


    static class viewHolder {
        View convertView;
        CircleImageView civAvatar;
        TextView tvName;
        Button btSendInvitation, btBlockUser;

        public viewHolder(View convertView) {
            this.convertView = convertView;
            civAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
            tvName= (TextView) convertView.findViewById(R.id.tvName);
        }
    }
}
