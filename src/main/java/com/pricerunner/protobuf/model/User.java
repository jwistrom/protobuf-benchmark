package com.pricerunner.protobuf.model;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class User {

    private String name;
    private int age;
    private String nickName;
    private Set<Phone> phones;

    private User() {
    }

    private User(final Builder builder) {
        name = Objects.requireNonNull(builder.name);
        age = builder.age;
        nickName = builder.nickName;
        phones = Objects.requireNonNull(builder.phones);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Optional<String> getNickName() {
        return Optional.ofNullable(nickName);
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private int age;
        private String nickName;
        private Set<Phone> phones;

        private Builder() {
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder age(final int age) {
            this.age = age;
            return this;
        }

        public Builder nickName(final String nickName) {
            this.nickName = nickName;
            return this;
        }

        public Builder phones(final Set<Phone> phones) {
            this.phones = phones;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
