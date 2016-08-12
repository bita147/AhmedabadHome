package senta.nilesh.autocalc.dto;

import java.io.Serializable;

/**
 * Created by "Nilesh Senta" on 4/6/2016.
 */
public class ItemDTO implements Serializable {
    private String buyDate;
    private String userName;
    private String itemDesc;
    private String Key;
    private String payBy;
    private float amt;
    private String usersIncluded;
    private Boolean isOfflineRecord;


    public ItemDTO() {
    }

    public Boolean getOfflineRecord() {
        return isOfflineRecord;
    }

    public void setOfflineRecord(Boolean offlineRecord) {
        isOfflineRecord = offlineRecord;
    }

    public String getPayBy() {
        return payBy;
    }

    public void setPayBy(String payBy) {
        this.payBy = payBy;
    }

    public String getKey() {
        return Key;
    }

    public String getUsersIncluded() {
        return usersIncluded;
    }

    public void setUsersIncluded(String usersIncluded) {
        this.usersIncluded = usersIncluded;
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

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }
}
