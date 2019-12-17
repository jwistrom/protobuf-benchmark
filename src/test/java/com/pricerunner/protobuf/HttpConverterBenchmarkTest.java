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
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
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
public class HttpConverterBenchmarkTest {

    private static SimpleClientHttpRequestFactory requestFactory =  new SimpleClientHttpRequestFactory();

    private Users regularUsers;
    private UserProto.Users protoUsers;

    private ProtobufHttpMessageConverter protobufConverter;
    private MappingJackson2HttpMessageConverter jsonConverter;

    private ByteArrayOutputStream protoOutputStream;
    private ByteArrayOutputStream regularOutputStream;

    @Param({"10", "100", "1000", "10000"})
    private int usersCount;

    @Setup
    public void setup() throws IOException {
        protobufConverter = new ProtobufHttpMessageConverter();
        jsonConverter = new MappingJackson2HttpMessageConverter();

        final int phoneCount = 10;
        regularUsers = IntStream.range(0, usersCount)
            .mapToObj(i -> Builders.regularUser(phoneCount))
            .collect(collectingAndThen(toSet(), Users::new));

        protoUsers = IntStream.range(0, usersCount)
            .mapToObj(i -> Builders.protoUser(phoneCount))
            .collect(collectingAndThen(toSet(), users -> UserProto.Users.newBuilder().addAllUsers(users).build()));

        protoOutputStream = new ByteArrayOutputStream();
        regularOutputStream = new ByteArrayOutputStream();

        protoUsers.writeTo(protoOutputStream);
        JsonMapper.MAPPER.writeValue(regularOutputStream, regularUsers);

        System.out.println("Size of proto users: " + protoOutputStream.toByteArray().length);
        System.out.println("Size of regular users: " + regularOutputStream.toByteArray().length);
    }

    @Benchmark
    public void serializeProtoUsers(final Blackhole bh) throws IOException {
        final ClientHttpRequest request = requestFactory.createRequest(URI.create("http://localhost"), HttpMethod.GET);
        protobufConverter.write(protoUsers, MediaType.parseMediaType("application/x-protobuf"), request);
        bh.consume(request);
    }

    @Benchmark
    public void deserializeProtoUsers(final Blackhole bh) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(protoOutputStream.toByteArray());
        HttpInputMessage inputMessage = new BenchmarkHttpInputMessage(inputStream);
        protobufConverter.read(UserProto.Users.class, inputMessage);
        bh.consume(inputMessage);
    }

    @Benchmark
    public void serializeRegularUsers(final Blackhole bh) throws IOException {
        final ClientHttpRequest request = requestFactory.createRequest(URI.create("http://localhost"), HttpMethod.GET);
        jsonConverter.write(regularUsers, MediaType.parseMediaType("application/json"), request);
        bh.consume(request);
    }

    @Benchmark
    public void deserializeRegularUsers(final Blackhole bh) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(regularOutputStream.toByteArray());
        HttpInputMessage inputMessage = new BenchmarkHttpInputMessage(inputStream);
        jsonConverter.read(Users.class, inputMessage);
        bh.consume(inputMessage);
    }
}
