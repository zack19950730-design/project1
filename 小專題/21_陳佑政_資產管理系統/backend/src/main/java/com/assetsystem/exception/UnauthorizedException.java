package com.assetsystem.exception;

/** 未登入或帳號密碼錯誤，回應 401 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
