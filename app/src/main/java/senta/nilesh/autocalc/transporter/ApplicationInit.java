package senta.nilesh.autocalc.transporter;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by "Nilesh Senta" on 4/7/2016.
 */
public class ApplicationInit  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
