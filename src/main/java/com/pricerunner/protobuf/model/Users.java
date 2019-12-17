package com.pricerunner.protobuf.model;

import java.util.Set;

public class Users {

    private Set<User> users;

    private Users() {
    }

    public Users(final Set<User> users) {
        this.users = users;
    }

    public Set<User> getUsers() {
        return users;
    }


}
