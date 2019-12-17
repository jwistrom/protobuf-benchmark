package com.pricerunner.protobuf;

import com.pricerunner.protobuf.model.Users;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BenchmarkHttpInputMessage implements HttpInputMessage {


    private InputStream inputStream;

    public BenchmarkHttpInputMessage(final InputStream inputStream){
        this.inputStream = inputStream;
    }

    @Override
    public InputStream getBody() throws IOException {
        return inputStream;
    }

    @Override
    public HttpHeaders getHeaders() {
        return HttpHeaders.EMPTY;
    }
}
