package com.hilda.yygh.hosp.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.model.acl.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/login")
    public R login(@RequestBody User user) {
        return R.ok().data("token", "admin-token");
    }

    @GetMapping("/info")
    public R info(String token) {
        return R.ok()
                .data("roles", "[admin]")
                .data("introduction", "super admin")
                .data("name", "Disco Broccoli");
    }

}
