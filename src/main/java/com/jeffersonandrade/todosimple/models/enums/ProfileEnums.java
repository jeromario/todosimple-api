package com.jeffersonandrade.todosimple.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ProfileEnums {

    ADMIN(1,"ROLE_ADMIN"),
    USER(2,"ROLE_USER");

    private Integer code;
    private String description;

    public static ProfileEnums toEnum(Integer code){
        if (Objects.isNull(code)){
            return null;
        }
        for (ProfileEnums x :ProfileEnums.values()) {
            if (code.equals(x.code)){
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
