package com.assetsystem.exception;

/** 資料重複 (例如帳號已被註冊)，回應 409 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
