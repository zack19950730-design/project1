package com.assetsystem.service;

import com.assetsystem.dto.AuthRequest;
import com.assetsystem.entity.Member;
import com.assetsystem.exception.ConflictException;
import com.assetsystem.exception.UnauthorizedException;
import com.assetsystem.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Member register(AuthRequest request) {
        String username = request.username().trim();
        if (repository.existsByUsername(username)) {
            throw new ConflictException("帳號「" + username + "」已被註冊");
        }
        Member member = new Member();
        member.setUsername(username);
        member.setPasswordHash(passwordEncoder.encode(request.password()));
        return repository.save(member);
    }

    /** 帳號不存在與密碼錯誤回傳相同訊息，避免被猜出哪些帳號存在 */
    public Member login(AuthRequest request) {
        return repository.findByUsername(request.username().trim())
                .filter(member -> passwordEncoder.matches(request.password(), member.getPasswordHash()))
                .orElseThrow(() -> new UnauthorizedException("帳號或密碼錯誤"));
    }

    public Member findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("請先登入"));
    }
}
