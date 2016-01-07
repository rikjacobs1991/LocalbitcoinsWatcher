package com.mycompany.rik.localbitcoinswatcher.localbitcoinsapi.marketinfo;

import com.google.gson.Gson;

import java.sql.Date;

/**
 * Created by Rik on 7-1-2016.
 */
public class SellingInfo {
    public Data data;

    public static SellingInfo createSellingInfoFromJson(String json) {

        return new Gson().fromJson(json, SellingInfo.class);
    }

    public class Data {
        public Ad[] ad_list;

    }
}



