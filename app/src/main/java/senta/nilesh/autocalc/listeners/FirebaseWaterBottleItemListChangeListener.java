package senta.nilesh.autocalc.listeners;

import android.databinding.ObservableArrayList;

import senta.nilesh.autocalc.dto.WaterBottleDTO;

public interface FirebaseWaterBottleItemListChangeListener {
    void onItemChange(ObservableArrayList<WaterBottleDTO> itemList);
}