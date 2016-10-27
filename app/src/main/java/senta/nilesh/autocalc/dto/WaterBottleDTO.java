package senta.nilesh.autocalc.dto;

import java.io.Serializable;

/**
 * Created by "Nilesh Senta" on 4/6/2016.
 */
public class WaterBottleDTO implements Serializable {
    private String buyDate;
    private String userName;
    private int bottleCount;
    private String Key;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getBottleCount() {
        return bottleCount;
    }

    public void setBottleCount(int bottleCount) {
        this.bottleCount = bottleCount;
    }
}
