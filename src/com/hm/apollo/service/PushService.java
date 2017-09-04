package com.hm.apollo.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;


import com.hm.apollo.framework.model.response.HMResponse;
import com.hm.apollo.framework.utils.Signature;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class PushService {
    public String get(String url, String serviceName) {

        HttpGet httpGet = new HttpGet(url + serviceName);
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        String re = "";
        try {
            HttpResponse response = httpClient.execute(httpGet);
            re = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            return re;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return "";
    }

    public String getWithAuthorization(String path, String serviceName, String querys) {
        String url = path + serviceName;
        if (querys != null) {
            url += querys;
        }
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json; charset=UTF-8");
        String method = "GET";
        Long timestamp = 1469686159l;
        setHeader(method,serviceName,timestamp+"",httpGet);
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        String re = "";
        try {
            HttpResponse response = httpClient.execute(httpGet);
            re = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            System.out.println("res  ===>" + re);
            return re;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return "";
    }

    public Object postWithAuthorization(String path, String serviceName, String data) {
        String url = path + serviceName;
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        String method = "POST";
        setHeader(method,serviceName,System.currentTimeMillis()+"",post);
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        String re = "";
        try {
            post.setEntity(new StringEntity(data, Charset.forName("UTF-8")));
            HttpResponse response = httpClient.execute(post);
            re = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            HMResponse response1 = JSON.parseObject(re, HMResponse.class);

            return response1.getBody().toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return re;
    }

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) {
        byte[] data = new byte[0];
        try {
            data = encryptKey.getBytes(ENCODING);

            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(MAC_NAME);
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);

            byte[] text = encryptText.getBytes(ENCODING);
            return mac.doFinal(text);
        } catch (Exception e) {
            throw new RuntimeException("加密失败");
        }
        //完成 Mac 操作

    }

    public static void main(String[] args) {
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        Map<String, String> params = Maps.newHashMap();
        params.put("doctorName", "zhangsan");
        params.put("password", "96e79218965eb72c92a549dd5a330112");
        params.put("loginStatus", "1");
        HttpPost loginPost = new HttpPost("http://localhost:8080/huimei/chronic/login/login");
        loginPost.setHeader("Content-Type", "application/json; charset=UTF-8");

        loginPost.setEntity(new StringEntity(JSON.toJSONString(params), Charset.forName("UTF-8")));
        try {
            HttpResponse loginResponse = httpClient.execute(loginPost);// 登录
            String loginRes = EntityUtils.toString(loginResponse.getEntity(), Charset.forName("UTF-8"));
            System.out.println("登录 res  =====================" + loginRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Logger logger = LogManager.getLogger(PushService.class);


    public String push(String url, String servicename, Object req) {
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        try {
            // 先登录
            //			this.login(url, httpClient);

            String json = JSON.toJSONString(req);
            System.out.println("req------------------>>> " + json);
            HttpPost post = new HttpPost(url + servicename);
            post.setHeader("Content-Type", "application/json; charset=UTF-8");
            post.setEntity(new StringEntity(json, Charset.forName("UTF-8")));
            long start = System.currentTimeMillis();
            HttpResponse response = httpClient.execute(post);// 业务接口
            long end = System.currentTimeMillis();
            String re = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            return re;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public void login(String url, HttpClient httpClient) {
        Map<String, String> params = Maps.newHashMap();
        params.put("doctorName", "惠每");
        params.put("password", "413f98f448a05365873eae33e54efc58");
        //		params.put("doctorName", "zhangsan");
        //		params.put("password", "96e79218965eb72c92a549dd5a330112");
        params.put("loginStatus", "1");
        HttpPost loginPost = new HttpPost(url + "/huimei/chronic/login/login");
        loginPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        loginPost.setEntity(new StringEntity(JSON.toJSONString(params), Charset.forName("UTF-8")));
        try {
            HttpResponse loginResponse = httpClient.execute(loginPost);// 登录
            String loginRes = EntityUtils.toString(loginResponse.getEntity(), Charset.forName("UTF-8"));
            System.out.println("登录 res  =====================" + loginRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setHeader(String method, String serviceName, String timestamp, HttpRequestBase httpRequestBase){
        String accessKey = "XxSawiGLH13v3lTo";
        String secretKey = "82ce745a-51ae-4b6e-9122-dadadaaa7988";
        String stringToSign = method + "|" + serviceName + "|" + timestamp;
        //  String encodeHmac= Signature.calculateRFCHMAC(secretKey,stringToSign);
        String encodeHmac = Signature.calculateRFCHMAC_Hex(secretKey, stringToSign);

        String sign = "HM " + accessKey + ":" + encodeHmac;
        httpRequestBase.setHeader("X-HM-Timestamp", timestamp.toString());
        httpRequestBase.setHeader("Authorization", sign);


    }
}
