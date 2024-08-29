package com.yiyan.study.Controller.api;

// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.yiyan.study.database.opengauss.OpengaussHelper;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    int port = 15432;
    String dbName = "desensitization";
    String username = "gaussdb";
    String password = "openGauss@123";

    OpengaussHelper opengaussHelper = new OpengaussHelper(port, dbName, username, password);

    // 用户注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, Object> register(@RequestParam String username, @RequestParam String password,
            @RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        String sql = "INSERT INTO \"user\" (username, password) VALUES (" + username + ", " + password + ")";

        try {
            int result = opengaussHelper.executeUpdate(sql);
            if (result > 0) {
                response.put("status", "success");
                response.put("message", "注册成功！");
            } else {
                response.put("status", "error");
                response.put("message", "注册失败！");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "发生错误：" + e.getMessage());
        }

        return response;
    }

    // 用户登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();
        String sql = "SELECT * FROM \"user\" WHERE username = " + username + " AND password = " + password;

        try {
            ResultSet rs = opengaussHelper.excuteQuery(sql);
            if (!rs.wasNull()) {
                response.put("status", "success");
                response.put("message", "登录成功！");
            } else {
                response.put("status", "error");
                response.put("message", "用户名或密码错误！");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "发生错误：" + e.getMessage());
        }

        return response;
    }
}
