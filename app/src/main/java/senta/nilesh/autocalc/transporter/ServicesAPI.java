package senta.nilesh.autocalc.transporter;

import android.content.Context;
import android.databinding.ObservableArrayList;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import senta.nilesh.autocalc.dto.ItemDTO;
import senta.nilesh.autocalc.dto.NotificationDTO;
import senta.nilesh.autocalc.dto.UserProfileDTO;
import senta.nilesh.autocalc.listeners.FirebaseItemListChangeListener;
import senta.nilesh.autocalc.listeners.TransactionInsertListener;
import senta.nilesh.autocalc.utils.AppPref;

/**
 * Created by "Nilesh Senta" on 4/7/2016.
 */
public class ServicesAPI {

    public static void doRegister(final UserProfileDTO profile, final Firebase.CompletionListener listener) {
        final Firebase fbAddChildUsingCustomId = Globle.FIREBASE_HOME.child("/User").child(profile.getUserName());
        Globle.FIREBASE_HOME.child("/User").child(profile.getUserName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    fbAddChildUsingCustomId.setValue(profile, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            listener.onComplete(new FirebaseError(2002, "Register success"), firebase);
                        }
                    });
                } else
                    listener.onComplete(new FirebaseError(1001, "User exists"), fbAddChildUsingCustomId);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onComplete(firebaseError, fbAddChildUsingCustomId);
            }
        });
    }

    public static void doLogin(final Context context, final UserProfileDTO profile, final Firebase.CompletionListener listener) {
        final Firebase fbLogin = Globle.FIREBASE_HOME.child("/User");
        fbLogin.child(profile.getUserName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    listener.onComplete(new FirebaseError(1002, "User not exists"), fbLogin);
                } else {
                    UserProfileDTO data = dataSnapshot.getValue(UserProfileDTO.class);
                    AppPref.get(context).saveLoginDTO(dataSnapshot.getValue(UserProfileDTO.class));
                    listener.onComplete(null, fbLogin);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onComplete(firebaseError, fbLogin);
            }
        });
    }

    public static void insertTransation(final ItemDTO itemDTO, final TransactionInsertListener transctionListener) {
        final Firebase fbInsTransaction = Globle.FIREBASE_HOME.child("/Transaction").child(String.valueOf(System.currentTimeMillis()));
        fbInsTransaction.setValue(itemDTO, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError == null) {
                    transctionListener.onTransactionCompleted();
                }
            }
        });
    }

    public static void removeNodebyKey(String key) {
        Globle.FIREBASE_HOME.child("/Transaction").child(key).removeValue();
    }

    public static void getAllRecords(final FirebaseItemListChangeListener listerner) {
        final Firebase fbGetAllRecords = Globle.FIREBASE_HOME.child("/Transaction");
        fbGetAllRecords.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ObservableArrayList<ItemDTO> list = new ObservableArrayList<ItemDTO>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    try {
//                        Gson gson = new Gson();
//                        JsonReader reader = new JsonReader(new StringReader(itemSnapshot.getValue().toString()));
//                        reader.setLenient(true);
//                        list.add((ItemDTO) gson.fromJson(reader,ItemDTO.class));
//                        list.add(new Gson().fromJson(itemSnapshot.getValue().toString(),ItemDTO.class));
//                        JSONObject jObject = new JSONObject(itemSnapshot.getValue().toString());

                        ItemDTO dto = itemSnapshot.getValue(ItemDTO.class);
                        dto.setKey(itemSnapshot.getKey());
                        list.add(0, dto);
                        itemSnapshot.getKey();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    try {
//                        JSONObject object  = new JSONObject(new Gson().toJson(itemSnapshot.child("userName").toString()));
//                        Log.e("df", object.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    ItemDTO dto = new ItemDTO();
//                    dto.setUserName(itemSnapshot.child("").toString());
//                    dto.setBuyDate();
//                    dto.setItemDesc();
//                    dto.setAmt();

                }
                listerner.onItemChange(list);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void updateRegistrationToken(UserProfileDTO profile, final Firebase.CompletionListener listener) {
        final Firebase fbUpdateRegToken = Globle.FIREBASE_HOME.child("/User").child(profile.getUserName());
        fbUpdateRegToken.setValue(profile, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (listener != null)
                    listener.onComplete(firebaseError, firebase);
            }
        });
    }

    public static void sendNotification(final NotificationDTO notification) {
        final Firebase fbGetAllRecords = Globle.FIREBASE_HOME.child("/User");
        fbGetAllRecords.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ObservableArrayList<UserProfileDTO> list = new ObservableArrayList<UserProfileDTO>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    try {
                        UserProfileDTO dto = itemSnapshot.getValue(UserProfileDTO.class);
                        list.add(0, dto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                list.size();
                List<String> users = Arrays.asList(notification.getData().getUsersIncluded().split("\\s*,\\s*"));
                ArrayList<String> regList = new ArrayList<>();

                for (UserProfileDTO profile : list){
                    if (users.contains(profile.getUserName()) && !profile.getUserName().equals(notification.getData().getUserName())){
                        regList.add(profile.getRegisterToken());
                    }
                }
                notification.setRegistration_ids(regList);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        URL myURL = null;
                        try {
                            myURL = new URL("https://fcm.googleapis.com/fcm/send");
                            HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
                            myURLConnection.setRequestProperty("Authorization", "key=AIzaSyCwUXb8jvtNXJEcmzQTygAy2l-QVU67UVs");
                            myURLConnection.setRequestMethod("POST");
                            myURLConnection.setRequestProperty("Content-Type", "application/json");
                            myURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                            myURLConnection.setRequestProperty("Accept","*/*");
                            myURLConnection.setDoInput(true);
                            myURLConnection.setDoOutput(true);
                            JSONObject jo = new JSONObject(new Gson().toJson(notification));
                            DataOutputStream wr = new DataOutputStream(myURLConnection.getOutputStream());
                            wr.write(jo.toString().getBytes("UTF-8"));
                            wr.flush();
                            wr.close();
                            int i  = myURLConnection.getResponseCode();
                            InputStream response = myURLConnection.getInputStream();
                            myURLConnection.disconnect();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
