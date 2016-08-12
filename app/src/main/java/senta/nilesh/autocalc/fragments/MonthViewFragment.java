package senta.nilesh.autocalc.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import senta.nilesh.autocalc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthViewFragment extends Fragment {


    public MonthViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Firebase fb = new Firebase(Globle.FIREBASE_GET_USER_LIST);
//        fb.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.e("Count - ", "" + dataSnapshot.getChildrenCount());
//                if (dataSnapshot.getChildrenCount() <= 0) {
//                    return;
//                } else {
//                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        Map<String, Object> newPost = (Map<String, Object>) postSnapshot.getValue();
//
//                        Log.e("Value "+postSnapshot.getKey(), newPost.toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
////                if (pd != null && pd.isShowing())
////                    pd.dismiss();
//            }
//        });

        return inflater.inflate(R.layout.fragment_month_view, container, false);
    }

}
