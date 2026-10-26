package com.assetsystem.config;

import com.assetsystem.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 檢查 Session 是否已登入，並把會員 id 放進 request attribute，
 * Controller 以 @RequestAttribute(AuthInterceptor.MEMBER_ID) 取得目前會員。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /** Session 與 request attribute 共用的 key */
    public static final String MEMBER_ID = "memberId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }
        HttpSession session = request.getSession(false);
        Object memberId = session == null ? null : session.getAttribute(MEMBER_ID);
        if (memberId == null) {
            throw new UnauthorizedException("請先登入");
        }
        request.setAttribute(MEMBER_ID, memberId);
        return true;
    }
}
