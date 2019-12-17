package com.pricerunner.protobuf;

import com.pricerunner.common.rest.JsonMapper;
import com.pricerunner.protobuf.model.Builders;
import com.pricerunner.protobuf.model.UserProto;
import com.pricerunner.protobuf.model.Users;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

@BenchmarkMode(value = {Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 4)
@Fork(value = 2, jvmArgs = {"-Xmx10g"})
@State(Scope.Benchmark)
public class SerializationBenchmarkTest {

    private Users regularUsers;
    private UserProto.Users protoUsers;

    @Param({"10", "100", "1000", "10000"})
    private int usersCount;

    @Setup
    public void setup(){
        final int phoneCount = 10;
        regularUsers = IntStream.range(0, usersCount)
            .mapToObj(i -> Builders.regularUser(phoneCount))
            .collect(collectingAndThen(toSet(), Users::new));

        protoUsers = IntStream.range(0, usersCount)
            .mapToObj(i -> Builders.protoUser(phoneCount))
            .collect(collectingAndThen(toSet(), users -> UserProto.Users.newBuilder().addAllUsers(users).build()));

    }

    @Benchmark
    public void regularUser(final Blackhole bh) {
        final String serialized = JsonMapper.toJson(regularUsers);
        final Users deserialized = JsonMapper.fromJson(serialized, Users.class);
        bh.consume(deserialized);
    }

    @Benchmark
    public void protoUsers(final Blackhole bh) throws IOException {
        final byte[] serialized = protoUsers.toByteArray();
        final UserProto.Users deserialized = UserProto.Users.parseFrom(serialized);
        bh.consume(deserialized);
    }
}
