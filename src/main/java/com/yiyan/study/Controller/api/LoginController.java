package com.yiyan.study.Controller.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yiyan.study.database.opengauss.OpengaussHelper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    String host = "127.0.0.1";
    int port = 15432;
    String dbName = "desensitization";
    String dbUsername = "gaussdb";
    String dbPassword = "openGauss@123";

    OpengaussHelper opengaussHelper = new OpengaussHelper(host, port, dbName, dbUsername, dbPassword);

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 用户注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, Object> register(@RequestBody Map<String, String> user) {
        Map<String, Object> response = new HashMap<>();
        String username = user.get("username");
        String password = user.get("password");

        String encryptedPassword = passwordEncoder.encode(password);
        String sql = "INSERT INTO \"user\" (username, password) VALUES (?, ?)";

        try (Connection conn = opengaussHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, encryptedPassword);

            System.out.println(pstmt.toString());
            int result = pstmt.executeUpdate();
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
    public Map<String, Object> login(@RequestBody Map<String, String> user) {
        Map<String, Object> response = new HashMap<>();
        String sql = "SELECT password FROM \"user\" WHERE username = ?";
        String username = user.get("username");
        String password = user.get("password");

        try (Connection conn = opengaussHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            System.out.println(pstmt.toString());
            ResultSet rs = pstmt.executeQuery();
            System.out.println("login:");
            System.out.println(rs);
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (passwordEncoder.matches(password, storedPassword)) {
                    response.put("status", "success");
                    response.put("message", "登录成功！");
                } else {
                    response.put("status", "error");
                    response.put("message", "密码错误！");
                }
            } else {
                response.put("status", "error");
                response.put("message", "用户名错误！");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "发生错误：" + e.getMessage());
        }

        return response;
    }
}
