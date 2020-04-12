package com.xauv.tempck;

import java.util.List;

/**
 * 用户登录保存 token
 */
public class TemClassForWXLogin {
    private List<String> signedUpTokenList;

    public List<String> getSignedUpTokenList() {
        return signedUpTokenList;
    }

    public void setSignedUpTokenList(List<String> signedUpTokenList) {
        this.signedUpTokenList = signedUpTokenList;
    }

    public boolean signedUp(String token) {
        return signedUpTokenList.contains(token);
    }

    public void add(String token) {
        signedUpTokenList.add(token);
    }
}
