package com.mycompany.rik.localbitcoinswatcher.localbitcoinsapi;

import org.json.JSONObject;

/**
 * Created by Rik on 6-1-2016.
 */
public interface LocalbitcoinsApiListener {
    void JSONFetched(String json);
}
