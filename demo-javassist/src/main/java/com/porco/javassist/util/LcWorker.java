package com.porco.javassist.util;

import com.porco.javassist.domain.Lc;

import java.lang.reflect.Field;

public class LcWorker {

    public static String work(Object o) throws IllegalAccessException {
        Class<?> aClass = o.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Lc.class)) {
                System.out.println(field.getName() + ": " + field.get(o) + ": " + field.getAnnotation(Lc.class).value());
            }
        }
        return "";
    }
}
