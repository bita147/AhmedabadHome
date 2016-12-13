package senta.nilesh.autocalc.transporter;

import com.firebase.client.Firebase;

/**
 * Created by "Nilesh Senta" on 4/7/2016.
 */
public class Globle {

    public static final String FIREBASE_HOST = "https://ahmedabad-home.firebaseio.com";
    public static final String FIREBASE_FILE_HOST = "gs://ahmedabad-home.appspot.com";

    public static final Firebase FIREBASE_HOME = new Firebase(Globle.FIREBASE_HOST);
    public static final Firebase FIREBASE_GET_USER_LIST = new Firebase(FIREBASE_HOST + "/User");


}
