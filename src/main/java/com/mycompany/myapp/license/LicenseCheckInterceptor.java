package com.mycompany.myapp.license;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * LicenseCheckInterceptor
 *
 * @author zifangsky
 * @date 2018/4/25
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
public class LicenseCheckInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LogManager.getLogger(LicenseCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
        // LicenseVerify licenseVerify = new LicenseVerify();

        /*
        boolean verifyResult = licenseVerify.verify();

        if (verifyResult) {
            return true;
        } else {
            response.setCharacterEncoding("utf-8");
            Map<String, String> result = new HashMap<>(1);
            result.put(
                "result",
                "Your certificate is invalid, please check whether the server is authorized or reapply for a certificate!"
            );

            response.getWriter().write(JSON.toJSONString(result));

            return false;

            */

    }
}
