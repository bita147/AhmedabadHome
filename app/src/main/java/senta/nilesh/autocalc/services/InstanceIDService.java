package senta.nilesh.autocalc.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import senta.nilesh.autocalc.utils.AppPref;

/**
 * Created by Nilesh Senta on 26-05-2016.
 */
public class InstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken);
        AppPref.get(this).saveRegistrationToken(refreshedToken);
    }
}
