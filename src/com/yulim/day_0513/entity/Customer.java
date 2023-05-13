package com.yulim.day_0513.entity;

public class Customer {
    public Customer(String account_mgr) {
        super();
        this.account_mgr = account_mgr;
    }

    private String account_mgr;

    public String getAccount_mgr() {
        return account_mgr;
    }

    public void setAccount_mgr(String account_mgr) {
        this.account_mgr = account_mgr;
    }


}
