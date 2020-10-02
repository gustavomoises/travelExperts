package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;

public class Reward implements Serializable {
    private int RewardId;
    private String RwdName;
    private String RwdDesc;

    public Reward() {
    }

    public Reward(int rewardId, String rwdName, String rwdDesc) {
        RewardId = rewardId;
        RwdName = rwdName;
        RwdDesc = rwdDesc;
    }

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

    @Override
    public String toString() {
        return  RwdName;
    }
}
