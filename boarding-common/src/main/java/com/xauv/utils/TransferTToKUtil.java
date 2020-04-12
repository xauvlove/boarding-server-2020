package com.xauv.utils;

import com.google.common.collect.Lists;
import com.xauv.constants.BoardingConstant;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TransferTToKUtil<T, K> {

    /**
     *
     * @param t 原始类对象
     * @param kClass 目标类
     * @return
     * @throws Exception
     */
    private K transferTToKWithLoop(T t, Class<K> kClass) {
        boolean isTClassBeVO = t.getClass().getSimpleName().endsWith("VO");
        boolean isKClassBeVO = kClass.getSimpleName().endsWith("VO");
        K instance = null;
        try {
            Class tClass = t.getClass();
            Field[] tFields = tClass.getDeclaredFields();
            Field[] kFields = kClass.getDeclaredFields();
            instance = kClass.getConstructor().newInstance();

            ArrayList<Field> tFieldList = Lists.newArrayList(tFields);
            Map<String, Object> fieldMap = new HashMap<>();

            for (Field tField : tFieldList) {
                tField.setAccessible(true);
                String fieldName = tField.getName();
                if(isTClassBeVO && fieldName.startsWith(BoardingConstant.OBSCURE)) {
                    //去掉前缀
                    fieldName = fieldName.substring(BoardingConstant.OBSCURE.length() + 1);
                    //首字母改为小写
                    fieldName = Character.toLowerCase(tField.getName().charAt(BoardingConstant.OBSCURE.length())) + fieldName;
                }
                //扔到 map 里面的永远都是非 obscure 的属性 ---
                try {
                    fieldMap.put(fieldName, tField.get(t));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            for (Field kField : kFields) {
                kField.setAccessible(true);
                String fieldName = kField.getName();
                if(isKClassBeVO && fieldName.startsWith(BoardingConstant.OBSCURE)) {
                    //去掉
                    fieldName = fieldName.substring(BoardingConstant.OBSCURE.length() + 1);
                    //首字母改为小写
                    fieldName = Character.toLowerCase(kField.getName().charAt(BoardingConstant.OBSCURE.length())) + fieldName;
                }
                Object o = fieldMap.get(fieldName);
                if(o == null) {
                    continue;
                }
                Object value = null;
                Class<?> kFieldTypeClass = kField.getType();
                if (kFieldTypeClass.getSimpleName().equals("Long")) {
                    value = Long.parseLong(o.toString());
                } else if(kFieldTypeClass.getSimpleName().equals("Integer")) {
                    value = Integer.parseInt(o.toString());
                } else if(kFieldTypeClass.getSimpleName().equals("Double")) {
                    value = Double.parseDouble(o.toString());
                } else {
                    value = o;
                    //如果是其它类型  无法转换则直接抛出异常
                }
                kField.set(instance, value);
            }
            return instance;
        } catch (Exception e) {
            log.error("com.xauv.utils.TransferTToKUtil transferTToK", e);
            e.printStackTrace();
            System.out.println("*******************error**********************");
        }
        return instance;
    }

    public K transferTToK(T t, Class<K> kClass) {
        try {
            return transferTToKWithLoop(t, kClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<K> transferTToK(List<T> t, Class<K> kClass) {
        try {
            ArrayList<K> arrayList = Lists.newArrayList();
            t.forEach(data -> {
                arrayList.add(transferTToKWithLoop(data, kClass));
            });
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private K transferTToKWithMap(T t, Class<K> kClass) throws Exception {

        boolean isTClassBeVO = t.getClass().getSimpleName().endsWith("VO");
        boolean isKClassBeVO = kClass.getSimpleName().endsWith("VO");

        K instance = null;
        Class tClass = t.getClass();
        Field[] tFields = tClass.getDeclaredFields();
        Field[] kFields = kClass.getDeclaredFields();
        instance = kClass.getConstructor().newInstance();

        /**
         * 如果是 源对象是混淆的
         * 那么存入 map 的时候应该去掉 obscure
         */
        Map<String, Object> fieldNameValueMap = Lists.newArrayList(tFields).stream().collect(Collectors.toMap(
                //获取键，转换名
                field -> {
                    field.setAccessible(true);
                    if(isTClassBeVO && field.getName().startsWith(BoardingConstant.OBSCURE)) {
                        String fieldName = field.getName().substring(BoardingConstant.OBSCURE.length() + 1);
                        fieldName = Character.toLowerCase(field.getName().charAt(BoardingConstant.OBSCURE.length())) + fieldName;
                        return fieldName;
                    } else {
                        return field.getName();
                    }
                },
                //获取值
                field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(t);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                },
                //map 键相同情况处理
                (oldVal, newVal) -> oldVal));

        /*Map<String, Object> fieldNameValueMap = Lists.newArrayList(tFields)
                .stream().collect(Collectors.toMap(
                        Field::getName, field -> {
                            try {
                                field.setAccessible(true);
                                return field.get(t);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }, (oldVal, newVal) -> oldVal));*/
        /**
         * 如果 k 对象是混淆的
         * 那么存入的 map 并没有 obscure 前缀
         * 去掉 obscure 并从 map 里面拿
         */
        for(Field kField: kFields) {
            kField.setAccessible(true);
            String fieldName = kField.getName();
            if(isKClassBeVO && fieldName.startsWith(BoardingConstant.OBSCURE)) {
                fieldName = fieldName.substring(BoardingConstant.OBSCURE.length() + 1);
                fieldName = Character.toLowerCase(kField.getName().charAt(BoardingConstant.OBSCURE.length())) + fieldName;
            }
            kField.set(instance, fieldNameValueMap.get(fieldName));
        }
        return instance;
    }
}
