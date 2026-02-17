package com.oyproj;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

import com.oyproj.common.utils.SnowFlake;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;
    @Test
    void test(){
        String str = "hello";
        String str2 = "hello";
        System.out.println(str==str2);
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        // 创建模拟请求对象
        MockHttpServletRequest request = new MockHttpServletRequest();
        // 可以设置请求来源用于测试
        request.addHeader("Origin", "http://example.com");

        // 获取CORS配置
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(request);

        // 验证CORS配置是否正确
        assertNotNull("CORS配置未找到", config);
    }
}
