package adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.chi.heyfriendv21.R;

import fragment.GroupChatFragment;
import fragment.PrivateChatFragment;



public class MessageFragmentPagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    Context context;


    public MessageFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context= context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new PrivateChatFragment();
        }
        return new GroupChatFragment();


    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        String tabTitles[] = new String[] {
                context.getString(R.string.txt_private_chat),
                context.getString(R.string.txt_group_chat) };
        return tabTitles[position];
    }
}