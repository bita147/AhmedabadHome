package senta.nilesh.autocalc.listeners;

import android.databinding.ObservableArrayList;

import senta.nilesh.autocalc.dto.ItemDTO;

/**
 * Created by Zeitech on 4/13/2016.
 */
public interface FirebaseItemListChangeListener {
    void onItemChange(ObservableArrayList<ItemDTO> itemList);
}
