package com.oyproj.common.security.enums;

/**
 * @author oywq3000
 * @since 2026-01-12
 */
public enum UserEnums {
    MEMBER("会员"),
    STORE("商家"),
    MANAGER("管理员"),
    SYSTEM("系统"),
    SEAT("坐席");
    private final String role;
    UserEnums(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }

}
