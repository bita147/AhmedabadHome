package senta.nilesh.autocalc.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.transporter.ServicesAPI;
import senta.nilesh.autocalc.utils.AppPref;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthViewFragment extends Fragment {

    private Context context;
    private TextView tvContacts;

    public MonthViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) context).setTitle("Contacts");
        View rootView = inflater.inflate(R.layout.fragment_month_view, container, false);
        tvContacts = (TextView) rootView.findViewById(R.id.tv_contacts);
        tvContacts.setText(AppPref.get(context).getContactString());
        loadDetails();
        return rootView;
    }

    private void loadDetails() {
        ServicesAPI.getContactString(context, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                tvContacts.setText(AppPref.get(context).getContactString());
            }
        });
    }
}
