package com.rami.service;


import com.rami.vo.ClientVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by Rami Stefanidis on 6/25/2017.
 */
@Service
public class KafkaMicroserviceRestService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeaders jsonHttpHeaders;

    @Value("${microserviceName.kafkaProducer}")
    private String kafkaProducerMicroserviceName;


    public void insertClient(final ClientVO clientVO) {
        LOG.info("Begin sendToKafkaProducerMicroservice. clientVO = {} ",clientVO);
        final String kafkaProducerMicroserviceBaseUrl = getBaseUrl(kafkaProducerMicroserviceName);
        final String kafkaProducerInsertClientFullUrl = kafkaProducerMicroserviceBaseUrl + "/insertClient";
        final HttpEntity entity = new HttpEntity(clientVO,jsonHttpHeaders);

        restTemplate.exchange(kafkaProducerInsertClientFullUrl, HttpMethod.POST,entity, String.class);

        LOG.info("Completed sendToKafkaProducerMicroservice. clientVO = {} ",clientVO);
    }

    private String getBaseUrl(final String microServiceName) {
        LOG.info("Begin getUrl. microServiceName = {}",microServiceName);
        final List<ServiceInstance> serviceInstancesList=discoveryClient.getInstances(microServiceName);
        String url = "";

        if(CollectionUtils.isEmpty(serviceInstancesList)) {
            LOG.error("instances list size = {}",serviceInstancesList.size());
            throw new RuntimeException("No instances found for microServiceName = "+microServiceName);
        } else {
            final ServiceInstance serviceInstance=serviceInstancesList.get(0);
            url = serviceInstance.getUri().toString();
        }

        LOG.info("Completed getUrl. microServiceName = {} , url = {}",microServiceName,url);

        return url;
    }


}
