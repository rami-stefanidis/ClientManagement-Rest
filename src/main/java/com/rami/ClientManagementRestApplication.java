package com.rami;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by Rami Stefanidis  on 6/25/2017.
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ClientManagementRestApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ClientManagementRestApplication.class, args);
    }
}
