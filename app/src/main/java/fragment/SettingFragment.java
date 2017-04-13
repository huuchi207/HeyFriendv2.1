
package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;
import com.chi.heyfriendv21.activity.LoginActivity;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import common.CommonMethod;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import service.OnStopAppService;
import service.ReceiveMessageService;

/**
 * Created by huuchi207 on 09/05/2016.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    Button tvInviteFriendFacebook, tvInviteFriendTwitter, tvInviteFriendViaSMS, btLogout;
    TextView tvUserName;
    CircleImageView civAvatar;
    FirebaseUser firebaseUser = CommonMethod.getCurrentFirebaseUser();
    private static final int REQUEST_INVITE = 1;
    private static final String TWITTER_KEY = "1rvHQJPjgVu4dFfJ2KvWVAUpO";
    private static final String TWITTER_SECRET = "rwIOzrOYu7MRDF8jXY5o3uFTFsXtrdiYBi4AcZ05zg1upeexzr";
    public SettingFragment(){

//        Log.e("===photoUrl", photoUrl);
//        Log.e("===username", username);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        tvInviteFriendFacebook= (Button) view.findViewById(R.id.tvInviteFriendFacebook);
        tvInviteFriendTwitter= (Button) view.findViewById(R.id.tvInviteFriendTwitter);
        tvInviteFriendViaSMS= (Button) view.findViewById(R.id.tvInviteFriendSMS);
        tvUserName= (TextView) view.findViewById(R.id.tvUserName);
        civAvatar= (CircleImageView) view.findViewById(R.id.profile_image);
        btLogout = (Button) view.findViewById(R.id.btLogout);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getActivity(), new Twitter(authConfig));
        if (firebaseUser!= null){
            tvUserName.setText(firebaseUser.getDisplayName());
            Glide.with(view.getContext()).load(firebaseUser.getPhotoUrl()).into(civAvatar);
        }
//
//        tvUserName.setText(currentUser.getName());
//
//
//
        tvInviteFriendViaSMS.setOnClickListener(this);
        tvInviteFriendTwitter.setOnClickListener(this);
        tvInviteFriendViaSMS.setOnClickListener(this);
        btLogout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
//        CommonMethod.showAnimation(v, getActivity());

        if (v== tvInviteFriendFacebook){
            Toast.makeText(getActivity(), getString(R.string.txt_coming_soon), Toast.LENGTH_SHORT ).show();
        }
        else if (v== tvInviteFriendTwitter){
            Toast.makeText(getActivity(),getString(R.string.txt_coming_soon), Toast.LENGTH_SHORT ).show();
        }
        else  if (v== tvInviteFriendViaSMS){
//            Toast.makeText(getActivity(),"Invite friend via SMS", Toast.LENGTH_SHORT ).show();
            sendSMSInvitation();

        }
        else if (v==btLogout){
            getActivity().stopService(new Intent(getActivity(), OnStopAppService.class));
            getActivity().stopService(new Intent(getActivity(), ReceiveMessageService.class));
//            getActivity().stopService(new Intent(getActivity(), LocationService.class));
            //log out firebase
            FirebaseAuth.getInstance().signOut();
            //log out facebook
            LoginManager.getInstance().logOut();
            //log out twitter
            Twitter.getSessionManager().clearActiveSession();
            Twitter.logOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();

        }

    }
    private void sendSMSInvitation() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.txt_hey_friend_app))
                .setMessage(getString(R.string.txt_invitation_sms))
                .setCallToActionText(getString(R.string.txt_call_to_action))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

}
