package huimei.es;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月13日
 * author：huangzhenjie
 * @version 1.0
 */
public class TestIndex {

    public static void main(String[] args) {
        try {
            TransportClient client = EsClientFactory.getTransportClient();
            // 线程安全
            GetResponse response = client.prepareGet("customer", "external", "1").setOperationThreaded(false)
                    .get();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
