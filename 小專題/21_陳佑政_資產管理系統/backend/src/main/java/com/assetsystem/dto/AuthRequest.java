package com.assetsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** 註冊 / 登入共用。帳號 3~50 字英數或底線，密碼 6~72 字 (BCrypt 上限 72 bytes) */
public record AuthRequest(
        @NotBlank @Size(min = 3, max = 50)
        @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "只能包含英文、數字或底線")
        String username,
        @NotBlank @Size(min = 6, max = 72) String password
) {
}
