package nl.spelberg.util.converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.util.Assert;

public class BasicTypesConverter {

    private static Class[] BASIC_TYPES_ARRAY = {
            boolean.class, byte.class, int.class, long.class, float.class, double.class, char.class, Boolean.class, Byte.class,
            Integer.class, Long.class, Float.class, Double.class, Character.class, String.class
    };
    private static final Set<Class> BASIC_TYPES = new HashSet<Class>(Arrays.asList(BASIC_TYPES_ARRAY));

    @SuppressWarnings({"unchecked"})
    public static <T> T convert(Object value, Class<T> targetClass) {
        Assert.notNull(targetClass, "targetClass is null");
        if (value == null) {
            return null;
        }

        Class<?> valueClass = value.getClass();
        if (targetClass.equals(valueClass)) {
            return (T) value;
        }

        if (String.class.equals(targetClass)) {
            if (BASIC_TYPES.contains(valueClass)) {
                return (T) String.valueOf(value);
            }
        } else if (int.class.equals(targetClass)) {
            if (String.class.isAssignableFrom(valueClass)) {
                return (T) Integer.valueOf((String) value);
            } else if (Integer.class.isAssignableFrom(valueClass)) {
                return (T) value;
            }
        } else if (Integer.class.equals(targetClass)) {
            if (Integer.class.isAssignableFrom(valueClass)) {
                return (T) value;
            } else if (String.class.equals(valueClass)) {
                return (T) Integer.valueOf((String) value);
            }
        }

        throw new UnsupportedOperationException(
                "No converter available to convert from '" + valueClass.getName() + "' to '" + targetClass.getName() +
                        "' for value '" + value + "'");
    }

}
