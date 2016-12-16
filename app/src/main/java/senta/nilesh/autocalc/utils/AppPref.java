package senta.nilesh.autocalc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableArrayList;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import senta.nilesh.autocalc.dto.ItemDTO;
import senta.nilesh.autocalc.dto.UserProfileDTO;

public class AppPref {
    private static AppPref INSTANCE;
    private Context context;
    private SharedPreferences sp;

    private AppPref(Context context) {
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static AppPref get(Context context) {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new AppPref(context);
    }

    // Clear Preferences
    public void clear() {
        sp.edit().clear().apply();
    }

    // Save Login
    public void saveLoginDTO(UserProfileDTO profileDTO) {
        sp.edit().putString("profile_dto", new Gson().toJson(profileDTO)).commit();
    }

    // get Login DTO
    public UserProfileDTO getUserProfileDTO() {
        try {
            return new Gson().fromJson(sp.getString("profile_dto", null), UserProfileDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveContactString(String contacts) {
        sp.edit().putString("contact_string", contacts).commit();
    }

    public String getContactString() {
        return sp.getString("contact_string", "");
    }

    public void saveRegistrationToken(String token) {
        sp.edit().putString("registration_token", token).commit();

    }

    public String getRegistrationToken() {
        return sp.getString("registration_token", null);
    }

    public void addOfflineList(ItemDTO item) {
        List<ItemDTO> list = new Gson().fromJson(sp.getString("offline_items", null), List.class);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(item);
        sp.edit().putString("offline_items", new Gson().toJson(list)).commit();
    }

    public void removeOfflineList(ItemDTO item) {
        List<ItemDTO> list = new Gson().fromJson(sp.getString("offline_items", null), List.class);
        if (list == null) {
            return;
        }
        for (ItemDTO i : list) {
            if (i.getBuyDate().equals(item.getBuyDate())) {
                list.remove(i);
                break;
            }
        }
        sp.edit().putString("offline_items", new Gson().toJson(list)).commit();
    }

    public void saveUserList(ObservableArrayList<UserProfileDTO> list) {
        sp.edit().putString("users_list", new Gson().toJson(list)).commit();
    }

    public ObservableArrayList<String> getUserList() {
        ObservableArrayList<UserProfileDTO> list = new Gson().fromJson(sp.getString("users_list", null), new TypeToken<ObservableArrayList<UserProfileDTO>>() {
        }.getType());
        ObservableArrayList<String> stringUser = new ObservableArrayList<>();
        if (list != null && list.size() > 0) {
            for (UserProfileDTO dto : list) {
                stringUser.add(dto.getUserName());
            }
        }
        Collections.sort(stringUser);
        return stringUser;
    }

    public ObservableArrayList<UserProfileDTO> getUserProfileList() {
        return new Gson().fromJson(sp.getString("users_list", null), new TypeToken<ObservableArrayList<UserProfileDTO>>() {
        }.getType());
    }

    public int getVersionCode() {
        return sp.getInt("app_version_code", 0);
    }

    public void saveVersionCode(Context  context) {
        Log.e("CURRENT  VEr Code", ""+ AppUtils.getVersionCode(context));
        sp.edit().putInt("app_version_code", AppUtils.getVersionCode(context)).commit();
    }
}
