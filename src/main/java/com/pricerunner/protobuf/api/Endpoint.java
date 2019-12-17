package com.pricerunner.protobuf.api;

import com.pricerunner.common.rest.JsonMapper;
import com.pricerunner.protobuf.model.Builders;
import com.pricerunner.protobuf.model.UserProto;
import com.pricerunner.protobuf.model.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;

@RestController
public class Endpoint {

    private static final int PHONE_COUNT = 100;

    @GetMapping(value = "/protobuf/{userCount}", produces = "application/x-protobuf;charset=UTF-8")
    public UserProto.Users getProtoUser(@PathVariable final int userCount){
        final UserProto.Users users = IntStream.range(0, userCount)
            .mapToObj(__ -> Builders.protoUser(PHONE_COUNT))
            .collect(collectingAndThen(Collectors.toSet(), UserProto.Users.newBuilder()::addAllUsers)).build();

        return users;
    }

    @GetMapping("/protobuf/bytes/{userCount}")
    public ResponseEntity<byte[]> getProtoUserBytes(@PathVariable final int userCount){
        final UserProto.Users users = IntStream.range(0, userCount)
            .mapToObj(__ -> Builders.protoUser(PHONE_COUNT))
            .collect(collectingAndThen(Collectors.toSet(), UserProto.Users.newBuilder()::addAllUsers)).build();

        return ResponseEntity.ok(users.toByteArray());
    }

    @GetMapping("/{userCount}")
    public Users getRegularUser(@PathVariable final int userCount){
        final Users users = IntStream.range(0, userCount)
            .mapToObj(__ -> Builders.regularUser(PHONE_COUNT))
            .collect(collectingAndThen(Collectors.toSet(), Users::new));
        return users;
    }

    @GetMapping("/bytes/{userCount}")
    public ResponseEntity<byte[]> getRegularUserBytes(@PathVariable final int userCount){
        final Users users = IntStream.range(0, userCount)
            .mapToObj(__ -> Builders.regularUser(PHONE_COUNT))
            .collect(collectingAndThen(Collectors.toSet(), Users::new));
        return ResponseEntity.ok(JsonMapper.toJson(users).getBytes());
    }

}
