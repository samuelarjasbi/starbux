package com.starbux.starbuxapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @RolesAllowed({"ADMIN","MODERATOR","USER"})
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @RolesAllowed("MODERATOR")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @RolesAllowed("ADMIN")
    public String adminAccess() {
        return "Admin Board.";
    }
}
