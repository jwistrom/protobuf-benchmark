package com.pricerunner.protobuf;

import com.pricerunner.common.jetty.JettyServerFactory;
import org.slf4j.Logger;

import java.util.Set;

public class ClientMain {

    public static void main(String[] args) throws Exception {
        JettyServerFactory.create("com.pricerunner.protobuf.client", 8081).start();
    }
}
