package com.example.demo;

import java.lang.reflect.Field;

public class TestUtilis {

    //the following method injects an object value to another with a correct given fieldname
    public static void injectObjects(Object target, String fieldName, Object toInject){
        boolean wasPrivate = false; //variable to verify if the field is private.
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
           /* if(!field.canAccess(target)){
                field.setAccessible(true);
                wasPrivate = true;
            }*/
            if(!field.isAccessible()){
                field.setAccessible(true);
                wasPrivate = true;
            }
            field.set(target, toInject);
            if (wasPrivate){
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
