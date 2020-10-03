//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Applicationpackage com.example.travelexperts.BusinessLayer;

package com.example.travelexperts.BusinessLayer;
import java.io.Serializable;


public class Reward implements Serializable {
    private int RewardId;
    private String RwdName;
    private String RwdDesc;

    //Constructor no arguments
    public Reward() {
    }

    //Constructor 3 arguments
    public Reward(int rewardId, String rwdName, String rwdDesc) {
        RewardId = rewardId;
        RwdName = rwdName;
        RwdDesc = rwdDesc;
    }

    //Getters and Setters
    public int getRewardId() {
        return RewardId;
    }

    public void setRewardId(int rewardId) {
        RewardId = rewardId;
    }

    public String getRwdName() {
        return RwdName;
    }

    public void setRwdName(String rwdName) {
        RwdName = rwdName;
    }

    public String getRwdDesc() {
        return RwdDesc;
    }

    public void setRwdDesc(String rwdDesc) {
        RwdDesc = rwdDesc;
    }

    //toString Method
    @Override
    public String toString() {
        return  RwdName;
    }
}
