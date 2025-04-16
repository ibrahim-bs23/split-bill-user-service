package com.brainstation23.skeleton.core.domain.enums;

import java.util.Arrays;

public enum UserTypeEnum {

    CUSTOMER(1, "Customer"),
    MERCHANT(2, "Merchant"),
    ADMIN(3, "Admin"),
    ;

    private final int code;
    private final String text;

    UserTypeEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static boolean isCustomer(int userTypeCode) {
        return UserTypeEnum.CUSTOMER.getCode() == userTypeCode;
    }

    public static boolean isMerchant(int userTypeCode) {
        return UserTypeEnum.MERCHANT.getCode() == userTypeCode;
    }

    public static boolean isUserTypeValid(int userType) {
        for (UserTypeEnum type : UserTypeEnum.values()) {
            if (type.getCode() == userType) return true;
        }
        return false;
    }

    public static boolean isUserTypeNotValid(int userType) {
        return !isUserTypeValid(userType);
    }

    public static UserTypeEnum getUserType(int code) {
        return Arrays.stream(UserTypeEnum.values())
                .filter(enumValue -> enumValue.code == code)
                .findFirst()
                .orElse(null);
    }

}
