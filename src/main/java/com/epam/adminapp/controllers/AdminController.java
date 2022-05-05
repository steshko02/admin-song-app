package com.epam.adminapp.controllers;

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
import com.epam.adminapp.service.AdminService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin1/")
public class AdminController {


    @Autowired
    private AdminService adminService;

    @GetMapping("/resource")
    public String resource(@AuthenticationPrincipal Jwt jwt) {
        return String.format("Resource accessed by: %s (with subjectId: %s)",
                jwt.getClaims().get("user_name"),
                jwt.getSubject());
    }

    @GetMapping(value = "/users")
    public String getUsers(Model model) throws IOException {
        model.addAttribute("users", adminService.getUsers());
        return "admin";
    }

    @RequestMapping(value = "/block", method = RequestMethod.POST)
    public String blockUser(@RequestParam String userId) throws IOException {
        adminService.blockUser(userId);
        return "redirect:localhost:8085/admin1/users";
    }

    @RequestMapping(value = "/unblock", method = RequestMethod.POST)
    public String unblockUser(@RequestParam String userId) throws IOException {

        adminService.unblockUser(userId);
        return "redirect:localhost:8085/admin1/users";
    }
}