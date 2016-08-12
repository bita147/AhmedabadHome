package senta.nilesh.autocalc.listeners;

import android.view.View;

import senta.nilesh.autocalc.dto.ItemDTO;

/**
 * Created by Nilesh Senta on 26-05-2016.
 */
public interface  ListOperationListener  {
    void onDeleteItemClick(ItemDTO item);
    void onItemClick(View view, ItemDTO item);
}
