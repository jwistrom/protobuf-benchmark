package com.pricerunner.protobuf.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pricerunner.common.rest.JsonMapper;
import com.pricerunner.common.web.client.RestTemplates;
import com.pricerunner.protobuf.model.UserProto;
import com.pricerunner.protobuf.model.Users;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Endpoint {

    private final RestTemplate restTemplate;

    public Endpoint(){
        final RestTemplate restTemplate = RestTemplates.sync();
        final List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>(restTemplate.getMessageConverters());
        httpMessageConverters.add(new ProtobufHttpMessageConverter());
        restTemplate.setMessageConverters(httpMessageConverters);
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{userCount}")
    public void requestRegularUsers(@PathVariable final int userCount){
        final ResponseEntity<Users> response = restTemplate.exchange(createRequestEntity("http://localhost:8080/" + userCount), Users.class);
        final Users payload = response.getBody();
        System.out.println("------Received regular users " + payload.getUsers().size());
    }

    @GetMapping("/bytes/{userCount}")
    public void requestRegularUsersBytes(@PathVariable final int userCount){
        final ResponseEntity<byte[]> response = restTemplate.exchange(createRequestEntity("http://localhost:8080/bytes/" + userCount), byte[].class);
        final byte[] payload = response.getBody();
        final Users users = JsonMapper.fromJson(new String(payload), Users.class);
        System.out.println("------Received regular users " + users.getUsers().size());
    }

    @GetMapping("/protobuf/{userCount}")
    public void requestProtobufUsers(@PathVariable final int userCount){
        final ResponseEntity<UserProto.Users> response = restTemplate.exchange(createRequestEntity("http://localhost:8080/protobuf/" + userCount), UserProto.Users.class);
        final UserProto.Users payload = response.getBody();
        System.out.println("------Received proto users " + payload.getUsersList().size());
    }

    @GetMapping("/protobuf/bytes/{userCount}")
    public void requestProtobufUsersBytes(@PathVariable final int userCount) throws InvalidProtocolBufferException {
        final ResponseEntity<byte[]> response = restTemplate.exchange(createRequestEntity("http://localhost:8080/protobuf/bytes/" + userCount), byte[].class);
        final byte[] payload = response.getBody();
        final UserProto.Users users = UserProto.Users.parseFrom(payload);
        System.out.println("------Received proto users " + users.getUsersList().size());
    }

    private static RequestEntity<Void> createRequestEntity(final String url){
        return RequestEntity.get(URI.create(url))
            .header("Accept", "application/json", "application/protobuf", "application/x-protobuf")
            .build();
    }

}
