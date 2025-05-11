package com.studio.core.global.enums;

import lombok.Getter;

@Getter
public enum AuthRole {

    ROLE_ROOT("상위관리자"),
    ROLE_MANAGE("하위관리자"),
    ROLE_GUEST("게스트관리자"),
    ROLE_USER("유저");


    public final String meaning;


    AuthRole(String meaning) {
        this.meaning = meaning;
    }

    public boolean isAdmin() {
        return this == ROLE_MANAGE || this == ROLE_GUEST;
    }

}
