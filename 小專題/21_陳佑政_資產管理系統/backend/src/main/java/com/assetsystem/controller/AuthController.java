package com.assetsystem.controller;

import com.assetsystem.config.AuthInterceptor;
import com.assetsystem.dto.AuthRequest;
import com.assetsystem.dto.MemberResponse;
import com.assetsystem.entity.Member;
import com.assetsystem.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** 會員註冊 / 登入 / 登出，以 Session Cookie 記住登入狀態 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService service;

    public AuthController(MemberService service) {
        this.service = service;
    }

    /** POST /api/auth/register：註冊成功後直接登入 */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse register(@Valid @RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        Member member = service.register(request);
        startSession(httpRequest, member);
        return MemberResponse.from(member);
    }

    @PostMapping("/login")
    public MemberResponse login(@Valid @RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        Member member = service.login(request);
        startSession(httpRequest, member);
        return MemberResponse.from(member);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /** GET /api/auth/me：目前登入的會員 (此路徑經過 AuthInterceptor，未登入回 401) */
    @GetMapping("/me")
    public MemberResponse me(@RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId) {
        return MemberResponse.from(service.findById(memberId));
    }

    /** 登入時換一個新的 Session，避免 Session Fixation 攻擊 */
    private void startSession(HttpServletRequest httpRequest, Member member) {
        HttpSession old = httpRequest.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        httpRequest.getSession(true).setAttribute(AuthInterceptor.MEMBER_ID, member.getId());
    }
}
