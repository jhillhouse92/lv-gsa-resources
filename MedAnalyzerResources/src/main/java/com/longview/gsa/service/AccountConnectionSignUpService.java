package com.longview.gsa.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import com.longview.gsa.domain.UserProfile;
import com.longview.gsa.repository.UsersDao;

public class AccountConnectionSignUpService implements ConnectionSignUp {

    private static final Logger LOG = LoggerFactory.getLogger(AccountConnectionSignUpService.class);

    private final UsersDao usersDao;

    public AccountConnectionSignUpService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public String execute(Connection<?> connection) {
        org.springframework.social.connect.UserProfile profile = connection.fetchUserProfile();
        String userId = UUID.randomUUID().toString();
        // TODO: Or simply use: r = new Random(); r.nextInt(); ???
        LOG.debug("Created user-id: " + userId);
        usersDao.createUser(userId, new UserProfile(userId, profile));
        return userId;
    }
}