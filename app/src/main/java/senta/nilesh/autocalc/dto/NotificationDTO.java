package senta.nilesh.autocalc.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nilesh Senta on 11-07-2016.
 */

public class NotificationDTO implements Serializable {

    private ItemDTO data;
    private List<String> registration_ids;

    public ItemDTO getData() {
        return data;
    }

    public void setData(ItemDTO data) {
        this.data = data;
    }

    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }


}
