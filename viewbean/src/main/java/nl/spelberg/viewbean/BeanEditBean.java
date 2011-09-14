package nl.spelberg.viewbean;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class BeanEditBean<T> implements BeanEditAttribute.UpdateListener<Object> {

    private static final Logger log = LoggerFactory.getLogger(BeanEditBean.class);

    private final T bean;
    private final Map<String, BeanEditAttribute> fields;

    public BeanEditBean(T bean) {
        Assert.notNull(bean, "bean is null");
        this.bean = bean;
        this.fields = findFields(bean);
    }

    private Map<String, BeanEditAttribute> findFields(Object bean) {
        Map<String, BeanEditAttribute> beanEditFields = new LinkedHashMap<String, BeanEditAttribute>();

        Class<?> beanClass = bean.getClass();
        List<Field> fields = BeanEditUtils.allDeclaredFields(beanClass);
        for (Field field : fields) {
            ViewField annotation = field.getAnnotation(ViewField.class);
            if (annotation != null) {
                BeanEditAttribute beanEditAttribute = new BeanEditAttribute(bean, field);
                beanEditFields.put(beanEditAttribute.id(), beanEditAttribute);
            }
        }

        return beanEditFields;
    }

    public T bean() {
        return bean;
    }

    public Collection<BeanEditAttribute> fields() {
        return fields.values();
    }

    public BeanEditAttribute fieldWithName(String name) {
        BeanEditAttribute attribute = fields.get(name);
        if (attribute == null) {
            throw new IllegalStateException("Field with name '" + name + "' does not exist in BeanEditBean " + toString());
        } else {
            return attribute;
        }
    }

    @SuppressWarnings({"unchecked"})
    public <Y> BeanEditAttribute<Y> fieldWithName(String name, Class<Y> fieldClass) {
        return fieldWithName(name);
    }

    @Override
    public String toString() {
        return "BeanEditBean{beanClass=" + bean.getClass().getName() + ", beanEditFields=" + fields.entrySet() + '}';
    }

    public void beforeUpdate(BeanEditAttribute<Object> beanEditAttribute) {
        log.info("Before update: " + beanEditAttribute);
    }

    public void afterUpdate(BeanEditAttribute<Object> beanEditAttribute) {
        log.info("After update: " + beanEditAttribute);
    }
}
