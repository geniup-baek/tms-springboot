package com.example.tms.utility;

import com.example.tms.error.SystemException;
import com.example.tms.utility.converter.annotation.ConvertIgnore;
import com.example.tms.utility.converter.annotation.ConvertSourceField;
import com.example.tms.utility.converter.annotation.ConvertSourceNestField;
import com.example.tms.utility.converter.annotation.ConvertSourceType;
import com.example.tms.utility.converter.annotation.ConvertToTargetWithType;
import com.example.tms.utility.converter.annotation.MergeIgnore;
import com.example.tms.utility.converter.annotation.MergeIncludeNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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

    public static <T, S> T convert(S source, Class<T> t) {
        try {
            T target = t.getDeclaredConstructor().newInstance();
            convert(source, target);
            return target;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new SystemException(e.getCause());
        }
    }

    public static <T, S> boolean convert(S source, T target) {
        if (source == null) {
            return false;
        }

        AtomicReference<Boolean> updated = new AtomicReference<>(false);

        // // デバッグログ出力用
        // List<String> convertedItems = new ArrayList<>();
        // List<String> noSuchItems = new ArrayList<>();

        try {
            List<Field> targetFields = getTargerFieldList(target);
            Map<String, Field> sourceFields = getSourceFieldMap(source);
            Map<Class<?>, Map<String, Field>> sourceNestFields =
                    getNestFieldMap(targetFields);

            for (Field targetField : targetFields) {
                targetField.setAccessible(true);

                // Source フィールド名
                // ・基本：Target フィールド名と同一
                // ・@ConvertSourceField アノテーションが付いている場合：指定したフィールド名
                String sourceFiledName = targetField.getName();
                Annotation convertSource = targetField.getAnnotation(ConvertSourceField.class);
                if (convertSource != null) {
                    sourceFiledName = ((ConvertSourceField) convertSource).name();
                }

                // Type コンバーター
                // ・@ConvertSourceType アノテーションが付いている場合：指定したコンバーター
                // ・@ConvertToTargetWithType アノテーションが付いている場合：指定したコンバーター
                // （@ConvertToTargetWithType は、@ConvertSourceField、@ConvertSourceType が付いていない場合のみ有効）
                Class<?> converterClass = ConvertSourceType.Default.class;
                Annotation convertSourceType = targetField.getAnnotation(ConvertSourceType.class);
                if (convertSourceType != null) {
                    converterClass = ((ConvertSourceType) convertSourceType).converterClass();
                }
                if (convertSource == null && convertSourceType == null) {
                    Field sourceField = sourceFields.get(sourceFiledName);
                    if (sourceField != null) {
                        Annotation convertToTarget =
                                sourceField.getAnnotation(ConvertToTargetWithType.class);
                        if (convertToTarget != null) {
                            converterClass =
                                    ((ConvertToTargetWithType) convertToTarget).converterClass();
                        }
                    }
                }

                try {
                    Object oldValue = targetField.get(target);
                    Object newValue = getSourceValue(
                            targetField,
                            sourceFields,
                            sourceNestFields,
                            source,
                            sourceFiledName,
                            converterClass);

                    if (canUpdate(oldValue, newValue)) {
                        targetField.set(target, newValue);
                        updated.set(true);
                        // デバッグログ出力用
                        // convertedItems.add(String.format("%s%s : %s -> %s",
                        // (targetField.getName().equals(sourceFiledName))
                        // ? ""
                        // : sourceFiledName + " -> ",
                        // targetField.getName(),
                        // oldValue, newValue));
                    }
                } catch (NoSuchFieldException e) {
                    // // デバッグログ出力用
                    // noSuchItems.add(sourceFiledName);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            throw new SystemException(e.getCause());
        }

        // // デバッグログ出力用
        // if (updated.get().booleanValue()) {
        // log.debug(String.join("\n", convertedItems));
        // }
        // if (!noSuchItems.isEmpty()) {
        // log.debug(String.format("NoSuchFields: [%s]", String.join(",", noSuchItems)));
        // }

        return updated.get().booleanValue();
    }    

    public static <T> boolean merge(T source, T target) {
        if (source == null) {
            return false;
        }

        if (target.getClass() != source.getClass()) {
            throw new IllegalArgumentException(
                    "The two parameter objects should be the same class");
        }
        final AtomicReference<Boolean> updated = new AtomicReference<>(false);

        // // デバッグログ出力用
        // List<String> mergedList = new ArrayList<>();
        // List<String> mergeIgnoredList = new ArrayList<>();

        ReflectionUtils.doWithFields(target.getClass(),
                field -> {
                    field.setAccessible(true);
                    Object oldValue = field.get(target);
                    Object newValue = field.get(source);
                    boolean canMerge = false;
                    final Annotation mergeIncludeNull = field.getAnnotation(MergeIncludeNull.class);
                    if (mergeIncludeNull == null) {
                        if (canUpdate(oldValue, newValue)) {
                            canMerge = true;
                        }
                    } else {
                        if (canUpdateAlbeitNull(oldValue, newValue)) {
                            canMerge = true;
                        }
                    }

                    if (canMerge) {
                        field.set(target, newValue);
                        updated.set(true);
                        // デバッグログ出力用
                        // mergedList.add(String.format("%s : %s -> %s", field.getName(), oldValue,
                        // newValue));
                    }
                },
                field -> {
                    final Annotation mergeIgnore = field.getAnnotation(MergeIgnore.class);
                    // if (mergeIgnore != null) {
                    // // デバッグログ出力用
                    // mergeIgnoredList.add(field.getName());
                    // }
                    return mergeIgnore == null;
                });

        // // デバッグログ出力用
        // log.debug(String.format("MergeIgnoreFields: [%s]", String.join(",", mergeIgnoredList)));
        // if (Boolean.TRUE.equals(updated.get())) {
        // log.debug(String.join("\n", mergedList));
        // }

        return updated.get();
    }    

    private static <T> boolean canUpdate(T to, T from) {
        return (from != null && !from.equals(to));
    }

    private static <T> boolean canUpdateAlbeitNull(T to, T from) {
        if (from == null) {
            return to != null;
        }
        return !from.equals(to);
    }

    private static <T> List<Field> getTargerFieldList(T object) {
        // // デバッグログ出力用
        // List<String> convertIgnoredFields = new ArrayList<>();

        List<Field> fieldList = new ArrayList<>();
        ReflectionUtils.doWithFields(object.getClass(), field -> {
            field.setAccessible(true);
            fieldList.add(field);
        },
                field -> {
                    final Annotation convertIgnore = field.getAnnotation(ConvertIgnore.class);
                    // if (convertIgnore != null) {
                    // // デバッグログ出力用
                    // convertIgnoredFields.add(field.getName());
                    // }
                    return convertIgnore == null;
                });

        // // デバッグログ出力用
        // if (!convertIgnoredFields.isEmpty()) {
        // log.debug(String.format("ConvertIgnoreFields: [%s]",
        // String.join(",", convertIgnoredFields)));
        // }

        return fieldList;
    }    

    private static <S> Map<String, Field> getSourceFieldMap(S object) {
        Map<String, Field> fieldMap = new HashMap<>();
        ReflectionUtils.doWithFields(object.getClass(),
                field -> {
                    field.setAccessible(true);
                    fieldMap.put(field.getName(), field);
                });

        return fieldMap;
    }

    private static Map<Class<?>, Map<String, Field>> getNestFieldMap(List<Field> targetFieldList) {

        Map<Class<?>, Map<String, Field>> nestFieldMap = new HashMap<>();

        for (Field targerField : targetFieldList) {

            Annotation convertSourceNestField =
                    targerField.getAnnotation(ConvertSourceNestField.class);

            if (convertSourceNestField != null) {

                Class<?> nestClass = ((ConvertSourceNestField) convertSourceNestField).nestClass();

                if (!nestFieldMap.containsKey(nestClass)) {
                    Map<String, Field> fieldMap = new HashMap<>();
                    ReflectionUtils.doWithFields(nestClass, field -> {
                        field.setAccessible(true);
                        fieldMap.put(field.getName(), field);
                    });
                    nestFieldMap.put(nestClass, fieldMap);
                }
            }
        }

        return nestFieldMap;
    }   
    
    private static Object getSourceValue(
            Field targetField,
            Map<String, Field> sourceFields,
            Map<Class<?>, Map<String, Field>> sourceNestFields,
            Object source,
            String sourceFiledName,
            Class<?> converterClass)
            throws IllegalAccessException, NoSuchFieldException,
            NoSuchMethodException, InvocationTargetException,
            InstantiationException {

        Field sourceField = sourceFields.get(sourceFiledName);

        if (sourceField != null) {
            sourceField.setAccessible(true);
            return getConvertedValue(source, converterClass, sourceField);
        } else {
            Annotation convertSourceNestField =
                    targetField.getAnnotation(ConvertSourceNestField.class);

            if (convertSourceNestField != null) {
                String fieldName =
                        ((ConvertSourceNestField) convertSourceNestField).name();
                Class<?> nestClass =
                        ((ConvertSourceNestField) convertSourceNestField).nestClass();
                String nestFieldName =
                        ((ConvertSourceNestField) convertSourceNestField).nestFieldName();

                Field nestObjectField = sourceFields.get(fieldName);

                if (nestObjectField != null) {
                    nestObjectField.setAccessible(true);

                    Object nestObject = nestObjectField.get(source);
                    if (nestObject == null) {
                        return null;
                    }
                    Map<String, Field> nestFieldMap = sourceNestFields.get(nestClass);
                    Field nestField = nestFieldMap.get(nestFieldName);

                    if (nestField == null) {
                        return null;
                    }
                    nestField.setAccessible(true);
                    return getConvertedValue(nestObject, converterClass, nestField);
                }
            }
        }
        throw new NoSuchFieldException();
    }    

    private static Object getConvertedValue(Object source, Class<?> converterClass,
            Field sourceField)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException,
            InstantiationException {

        if (converterClass == ConvertSourceType.Default.class
                || converterClass == ConvertToTargetWithType.Default.class) {
            return sourceField.get(source);
        } else {

            Method convertMethod =
                    converterClass.getMethod("convert", Object.class);
            return convertMethod.invoke(
                    converterClass.getDeclaredConstructor().newInstance(),
                    sourceField.get(source));
        }
    }    
}
