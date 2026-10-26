package com.assetsystem.dto;

import com.assetsystem.entity.Member;

/** 回傳給前端的會員資料 (不含密碼) */
public record MemberResponse(Long id, String username) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getUsername());
    }
}
