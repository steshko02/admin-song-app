package com.epam.adminapp.service;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.PageFilter;
import com.auth0.client.mgmt.filter.RolesFilter;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.Role;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AuthAPI authAPI;

    private ManagementAPI getManagementAPI() throws Auth0Exception {
        AuthRequest authRequest = authAPI.requestToken("https://" + "dev-hf6j43cb.eu.auth0.com" + "/api/v2/");
        TokenHolder holder = authRequest.execute();
        System.out.println(holder.getAccessToken());
        return new ManagementAPI("dev-hf6j43cb.eu.auth0.com", holder.getAccessToken());
    }

    public boolean blockUser(String userId) throws IOException {

        ManagementAPI mgmt = getManagementAPI();
        User user = new User(null);
        user.setBlocked(true);

        User request = mgmt.users().update(userId, user).execute();
        return request.isBlocked();
    }

    public boolean unblockUser(String userId) throws IOException {
        ManagementAPI mgmt = getManagementAPI();
        User user = new User(null);
        user.setBlocked(false);

        User request = mgmt.users().update(userId, user).execute();

        return request.isBlocked();
    }

    public List<User> getUsers() throws IOException {
        ManagementAPI mgmt = getManagementAPI();
        List<Role> roles = mgmt.roles().list(new RolesFilter()).execute().getItems();
        Role userRole = roles.stream().filter(r -> (r.getName().equals("User"))).findFirst().orElse(null);
        List<User> userWithFullSettings = new ArrayList<>();
        mgmt.roles().listUsers(userRole.getId(), new PageFilter()).
                execute().getItems().stream().
                forEach(user -> {
                    try {
                        userWithFullSettings.add(mgmt.users().get(user.getId(), new UserFilter()).execute());
                    } catch (Auth0Exception e) {
                        e.printStackTrace();
                    }
                });
        return userWithFullSettings;
    }

}
