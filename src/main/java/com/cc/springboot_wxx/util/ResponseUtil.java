package com.cc.springboot_wxx.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP响应工具类
 * 统一处理JSON响应格式
 */
public class ResponseUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 成功响应
     * @param response HttpServletResponse
     * @param data 返回数据
     */
    public static void success(HttpServletResponse response, Object data) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
    
    /**
     * 错误响应
     * @param response HttpServletResponse
     * @param code 错误码
     * @param message 错误信息
     */
    public static void error(HttpServletResponse response, int code, String message) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}