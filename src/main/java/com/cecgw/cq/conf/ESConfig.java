package com.cecgw.cq.conf;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by lifuyi on 2018/11/20.
 */
@Configuration
public class ESConfig {


    private final static Logger logger = LoggerFactory.getLogger(ESConfig.class);

    @Value("${es.cluster.name}")
    private String esName;
    @Value("${es.idAddress}")
    private String esId;

    @Bean(name="client")
    public TransportClient createTransportClient() throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name",esName)
                .put("client.transport.sniff",true)
                .build();

        TransportClient client = new PreBuiltTransportClient(settings);

        String[] essplit = esId.split(",");
        for(int i=0;i<essplit.length;i++){
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(essplit[i]), 9300));
        }

        logger.info("elasticsearch client initialized !!!");
        return client;
    }


}
