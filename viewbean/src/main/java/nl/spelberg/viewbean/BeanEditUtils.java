package nl.spelberg.viewbean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanEditUtils {

    public static List<Field> allDeclaredFields(Class<?> beanClass) {
        List<Field> fieldList = new ArrayList<Field>();
        Class<?> currentClass = beanClass;
        while (currentClass != null && !Object.class.equals(currentClass)) {
            Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(fieldList, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return fieldList;
    }

    public static Field fieldForName(Object bean, String fieldName) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        throw new IllegalStateException("unknown on '" + fieldName + "' in bean class '" + bean.getClass().getName() + "'");
    }
}
