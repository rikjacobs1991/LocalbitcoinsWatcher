package com.mycompany.rik.localbitcoinswatcher.localbitcoinsapi;

import android.os.SystemClock;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Rik on 6-1-2016.
 */

/*
    source: https://localbitcoins.com/api-docs/#toc2

    Calculating signature

    In HMAC, a message is signed with secret using some hashing algorithm to get signature. Message is formed by concatenating these four strings:
    1.Nonce. A 63 bit positive integer, for example unix timestamp as milliseconds.
    2.HMAC authentication key. This is the first one of a key/secret pair.
    3.Relative path, for example /api/wallet/.
    4.GET or POST parameters in their URL encoded format, for example foo=bar&baz=quux.
    Secret is got from Apps Dashboard along with the key. Hashing algorithm is SHA256.

    A Python example of this can be seen below:
      message = str(nonce) + hmac_auth_key + relative_path + get_or_post_params_urlencoded
      signature = hmac.new(hmac_auth_secret, msg=message, digestmod=hashlib.sha256).hexdigest().upper()

 */

public class LocalbitcoinsApiRequest {
    LocalbitcoinsApiConfig apiConfig;
    long nonce;
    String relativePath;
    String requestParams;
    String message;
    String hmac;
    LocalbitcoinsApiRequestTask.RequestType requestType;

    private LocalbitcoinsApiRequest(LocalbitcoinsApiConfig apiConfig, String relativePath, String requestParams, LocalbitcoinsApiRequestTask.RequestType requestType){
        this.apiConfig = apiConfig;
        this.relativePath = relativePath;
        this.requestParams = requestParams;
        this.requestType = requestType;
    }

    public static LocalbitcoinsApiRequest createLocalbitcoinsApiRequest(LocalbitcoinsApiConfig apiConfig, String relativePath, String requestParams, LocalbitcoinsApiRequestTask.RequestType requestType) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        LocalbitcoinsApiRequest request = new LocalbitcoinsApiRequest(apiConfig, relativePath, requestParams, requestType);
        long nonce = createNonce();
        String message = nonce + apiConfig.key + relativePath + URLEncoder.encode(requestParams, "UTF-8");
        request.nonce = nonce;
        request.message = message;
        request.hmac = createHMACSHA265(message, apiConfig.secret);

        return request;
    }

    private static String createHMACSHA265(String message, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        final Charset asciiCs = Charset.forName("US-ASCII");
        final Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        final SecretKeySpec secret_key = new javax.crypto.spec.SecretKeySpec(asciiCs.encode(secret).array(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        final byte[] mac_data = sha256_HMAC.doFinal(asciiCs.encode(message).array());
        String result = "";
        for (final byte element : mac_data)
        {
            result += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }

    private static long createNonce(){
        return System.currentTimeMillis();
    }
}
