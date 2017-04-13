package view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chi.heyfriendv21.R;
import com.tokenautocomplete.TokenCompleteTextView;

import object.Tag;


/**
 * Created by root on 12/11/2016.
 */

public class ContactCompletionView extends TokenCompleteTextView<Tag> {
    public ContactCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        allowDuplicates(false);
    }

    @Override
    protected View getViewForObject(Tag object) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.tag_token_list_participant, (ViewGroup) getParent(), false);
        view.setText(object.toString());
        return view;
    }



    @Override
    protected Tag defaultObject(String completionText) {
//            //Stupid simple example of guessing if we have an email or not
//            int index = completionText.indexOf('@');
//            if (index == -1) {
//                return new Tag(completionText, completionText.replace(" ", "") + "@example.com");
//            } else {
//                return new Person(completionText.substring(0, index), completionText);
//            }
        return new Tag("null");
    }
}