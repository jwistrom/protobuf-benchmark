package com.pricerunner.protobuf.model;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Builders {

    private Builders() {
    }

    public static UserProto.User protoUser(final int phoneCount) {

        final Set<UserProto.Phone> phones = IntStream.range(0, phoneCount)
            .mapToObj(i -> UserProto.Phone.newBuilder()
                .setType(i % 2 == 0 ? UserProto.Phone.Type.MOBILE : UserProto.Phone.Type.WORK)
                .setNumber(ThreadLocalRandom.current().nextInt(10000))
                .build()
            ).collect(Collectors.toSet());

        return UserProto.User.newBuilder()
            .setName(RandomStringUtils.randomAlphabetic(10))
            .setAge(10)
            .addAllPhones(phones)
            .build();
    }

    public static User regularUser(final int phoneCount){

        final Set<Phone> phones = IntStream.range(0, phoneCount)
            .mapToObj(i -> Phone.of(ThreadLocalRandom.current().nextInt(10000), i % 2 == 0 ? Phone.Type.MOBILE : Phone.Type.WORK))
            .collect(Collectors.toSet());

        return User.builder()
            .name(RandomStringUtils.randomAlphabetic(10))
            .age(10)
            .phones(phones)
            .build();
    }
}
