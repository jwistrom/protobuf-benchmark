package com.pricerunner.protobuf;

import com.pricerunner.common.jetty.JettyServerFactory;

import java.util.Set;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        final Set<String> configLocations = Set.of("com.pricerunner.protobuf.config", "com.pricerunner.protobuf.api");
        JettyServerFactory.create(configLocations, 8080).start();
    }
}
