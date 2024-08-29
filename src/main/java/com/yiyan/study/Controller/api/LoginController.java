package com.yiyan.study.Controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiyan.study.database.opengauss.OpengaussHelper;

@RestController
@RequestMapping("/api")
public class LoginController {
    int port = 15432;
    String dbName = "test_db";
    String username = "gaussdb";
    String password = "openGauss@123";

    OpengaussHelper opengaussHelper = new OpengaussHelper(port, dbName, username, password);

    @RequestMapping("/login")
    public String login(
            @RequestParam("username") String usename,
            @RequestParam("password") String password) {

        String sql = String.format("select * from user where username='%s' and password='%s'", username, password);
        opengaussHelper.execute(sql);
        return "Login successfully";
    }
}
