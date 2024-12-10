package com.example.ProjectEcommerce.sercurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthEntrypoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,  // yêu cầu từ người dùng
                         HttpServletResponse response,  // Phản hồi trả về cho người dùng
                         AuthenticationException authException) // Lý do gây là lỗi xác thực
                        throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();  // xây dựng nội dung phản hồi cho body
        //body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "You may login and try again");
        //body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body); // ghi phản hồi cho luồng đầu ra.
    }
}
