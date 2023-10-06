package com.porco.javassist.domain;

import lombok.Data;

@Data
public class Result {
    private Object data;

    public static Result ok() {
        return new Result();
    }

    public static Result of(Object o) {
        Result result = new Result();
        result.setData(o);
        return result;
    }
}
