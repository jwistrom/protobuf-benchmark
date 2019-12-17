package com.pricerunner.protobuf.model;

public class Phone {

    enum Type {
        MOBILE, WORK
    }

    private int number;
    private Type type;

    private Phone(final int number, final Type type) {
        this.number = number;
        this.type = type;
    }

    private Phone() {
    }

    public int getNumber() {
        return number;
    }

    public Type getType() {
        return type;
    }

    public static Phone of(final int number, final Type type){
        return new Phone(number, type);
    }


}
