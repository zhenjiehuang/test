package huimei.es;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * @author lipeng
 * @date 2016/11/23
 */
public class EsClientFactory {
    private static TransportClient client;

    private EsClientFactory() {

    }

    public static TransportClient getTransportClient() {
        if (client != null) {
            return client;
        }
        String clusterName = "cdss-application";
        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                //如果设置client.transport.sniff为true，则表示客户端去嗅探整个cluster的状态，把集群中其它机器的ip地址加到客户端中，这样做的好处是一般你不用手动设置集群里所有集群的ip到连接客户端，它会自动帮你添加，并且自动发现新加入集群的机器。
//                .put("client.transport.sniff", true)
                .build();
        client = new PreBuiltTransportClient(settings);
        String transportAddress = "10.46.74.95:9300";

        String[] ipPortPairs = transportAddress.split(",");
        for (String ipPortPair : ipPortPairs) {
            String[] ipPort = ipPortPair.split(":");
            String ip = ipPort[0];
            Integer port = Integer.valueOf(ipPort[1]);
            try {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
            } catch (UnknownHostException e) {
                e.printStackTrace();

            }
        }


        return client;
    }

}
