package senta.nilesh.autocalc.transporter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.dto.ItemDTO;
import senta.nilesh.autocalc.dto.NotificationDTO;
import senta.nilesh.autocalc.dto.UserProfileDTO;
import senta.nilesh.autocalc.dto.VersionDTO;
import senta.nilesh.autocalc.dto.WaterBottleDTO;
import senta.nilesh.autocalc.listeners.FirebaseItemListChangeListener;
import senta.nilesh.autocalc.listeners.FirebaseWaterBottleItemListChangeListener;
import senta.nilesh.autocalc.listeners.TransactionInsertListener;
import senta.nilesh.autocalc.listeners.VersionInfoRetrived;
import senta.nilesh.autocalc.utils.AppPref;
import senta.nilesh.autocalc.utils.AppUtils;

import static senta.nilesh.autocalc.transporter.Globle.FIREBASE_FILE_HOST;

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
        AppUtils.hideKeyboard(context);
        final Firebase fbLogin = Globle.FIREBASE_HOME.child("/User");
        fbLogin.child(profile.getUserName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    listener.onComplete(new FirebaseError(1002, "User not exists"), fbLogin);
                } else {
                    UserProfileDTO data = dataSnapshot.getValue(UserProfileDTO.class);
                    if (profile.getPassword().equals(data.getPassword())) {
                        AppPref.get(context).saveLoginDTO(data);
                        listener.onComplete(null, fbLogin);
                    } else
                        listener.onComplete(new FirebaseError(1002, "Wrong password...!"), fbLogin);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onComplete(firebaseError, fbLogin);
            }
        });
    }

    public static void getContactString(final Context context, final Firebase.CompletionListener listener) {
        final Firebase fbGetContacts = Globle.FIREBASE_HOME.child("/Contact/Data");
        fbGetContacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    listener.onComplete(new FirebaseError(1002, "No Contacts"), fbGetContacts);
                } else {
                    Log.e(getClass().getName(), "" + dataSnapshot.getValue());
                    AppPref.get(context).saveContactString(dataSnapshot.getValue().toString());
                    listener.onComplete(null, fbGetContacts);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onComplete(firebaseError, fbGetContacts);
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

    public static void insertWaterBottle(final WaterBottleDTO waterBottleDTO, final TransactionInsertListener transctionListener) {
        final Firebase fbInsTransaction = Globle.FIREBASE_HOME.child("/Waterbottle").child(String.valueOf(System.currentTimeMillis()));
        fbInsTransaction.setValue(waterBottleDTO, new Firebase.CompletionListener() {
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

    public static void removeWaterNodebyKey(String key) {
        Globle.FIREBASE_HOME.child("/Waterbottle").child(key).removeValue();
    }

    public static void getAllRecords(final Context context, final FirebaseItemListChangeListener listerner) {
        final Firebase fbGetAllRecords = Globle.FIREBASE_HOME.child("/Transaction");
        fbGetAllRecords.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ObservableArrayList<ItemDTO> list = new ObservableArrayList<ItemDTO>();
                UserProfileDTO user = AppPref.get(context).getUserProfileDTO();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    try {
                        ItemDTO dto = itemSnapshot.getValue(ItemDTO.class);
                        dto.setKey(itemSnapshot.getKey());
                        if (dto.getUsersIncluded().contains(user.getUserName()) || dto.getPayBy().contains(user.getUserName()))
                            list.add(0, dto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                listerner.onItemChange(list);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void getAllWaterBottlesRecords(final FirebaseWaterBottleItemListChangeListener listerner) {
        final Firebase fbGetAllRecords = Globle.FIREBASE_HOME.child("/Waterbottle");
        fbGetAllRecords.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ObservableArrayList<WaterBottleDTO> list = new ObservableArrayList<WaterBottleDTO>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    try {
                        WaterBottleDTO dto = itemSnapshot.getValue(WaterBottleDTO.class);
                        dto.setKey(itemSnapshot.getKey());
                        list.add(0, dto);
                        itemSnapshot.getKey();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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

                for (UserProfileDTO profile : list) {
                    if (users.contains(profile.getUserName()) && !profile.getUserName().equals(notification.getData().getUserName())) {
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
                            myURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                            myURLConnection.setRequestProperty("Accept", "*/*");
                            myURLConnection.setDoInput(true);
                            myURLConnection.setDoOutput(true);
                            JSONObject jo = new JSONObject(new Gson().toJson(notification));
                            DataOutputStream wr = new DataOutputStream(myURLConnection.getOutputStream());
                            wr.write(jo.toString().getBytes("UTF-8"));
                            wr.flush();
                            wr.close();
                            int i = myURLConnection.getResponseCode();
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

    public static void getUserList(final Context context) {
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
                AppPref.get(context).saveUserList(list);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void getVersionUpadteInfo(final VersionInfoRetrived listener) {
        final Firebase fbGetAllRecords = Globle.FIREBASE_HOME.child("/Version");
        fbGetAllRecords.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                VersionDTO version = null;
                version = dataSnapshot.getValue(VersionDTO.class);

                if (listener != null)
                    listener.onVersionInfoRetrived(version);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    static File file;

    public static void getAllFiles(final Context context) {
        final ProgressDialog pd = ProgressDialog.show(context, null, "Please wait...");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(FIREBASE_FILE_HOST + "/ahmedabad.apk");

        File downloadDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + context.getString(R.string.app_name));
        if (!downloadDir.exists()){
            boolean isfileCreatd = downloadDir.mkdirs();
            if (!isfileCreatd){
                if (pd!=null && pd.isShowing())
                    pd.dismiss();
                return;
            }
        }
        file = new File(downloadDir + File.separator + "ahmedabad.apk");

        storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }
}
