package com.example.tms.utility;

import java.lang.reflect.InvocationTargetException;

import com.example.tms.error.SystemException;

import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertUtils {
    public static <T> T fromMultiValueMap(MultiValueMap<String, String> multiValueMap, Class<T> clazz) {

        try {
            T obj = clazz.getDeclaredConstructor().newInstance();

            if (multiValueMap != null) {
                ReflectionUtils.doWithFields(clazz, field -> {
                    field.setAccessible(true);
                    field.set(obj, multiValueMap.getFirst(field.getName()));
                });
            }

            return obj;

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new SystemException(e);
        }
    }
}
