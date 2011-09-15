package nl.spelberg.viewbean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import nl.spelberg.util.event.Listeners;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

public class ViewBeanField<T> implements Serializable {

    private static final long serialVersionUID = 42L;

    private final Object bean;
    private final Class beanClass;
    private final String fieldName;

    private final Listeners<UpdateListener<T>> updateListeners;

    private transient Field field;
    private transient ViewField annotation;
    private transient String id;
    private transient String label;

    public ViewBeanField(Object bean, Field field) {
        this(bean, field.getName());
    }

    public ViewBeanField(Object bean, String fieldName) {
        Assert.notNull(bean, "bean is null");
        Assert.notNull(fieldName, "fieldName is null");
        this.bean = bean;
        this.fieldName = fieldName;

        // derived fields
        this.beanClass = bean.getClass();
        this.updateListeners = Listeners.newInstanceFor(UpdateListener.class);

        // call init to make sure all checks are done, fail-fast
        init();
    }

    private void init() {
        if (field == null) {
            // on
            this.field = ViewBeanUtils.fieldForName(bean, fieldName);
            // check if on is accessible
            this.field.setAccessible(true);

            // annotation
            this.annotation = field.getAnnotation(ViewField.class);
            Assert.notNull(this.annotation, "on does not have the annotation @" + ViewField.class.getSimpleName());

            // name
            this.id = StringUtils.isBlank(annotation.id()) ? field.getName() : annotation.id();
            this.label = StringUtils.isBlank(annotation.label()) ? this.id : annotation.label();
        }
    }

    public String id() {
        init();
        return id;
    }

    public String label() {
        init();
        return label;
    }

    @SuppressWarnings({"unchecked"})
    public T value() {
        init();
        try {
            return (T) field.get(bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Class valueClass() {
        init();
        return field.getType();
    }

    public void update(T value) {
        init();
        if (annotation.readOnly()) {
            throw new UnsupportedOperationException(
                    "Illegal to update read-only on '" + id + "' in bean class '" + beanClass.getName() + "'");
        }
        if (Modifier.isFinal(field.getModifiers())) {
            throw new UnsupportedOperationException(
                    "Illegal to update final on '" + id + "' in bean class '" + beanClass.getName() + "'");
        }
        if (value == null && !annotation.nullable()) {
            throw new UnsupportedOperationException(
                    "Illegal to update non-nullable on '" + id + "' with <null> value in bean class '" + beanClass.getName() +
                            "'");
        }

        updateListeners.onAll().beforeUpdate(this);

        try {
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        updateListeners.onAll().afterUpdate(this);
    }

    public boolean readOnly() {
        init();
        return annotation.readOnly();
    }

    public boolean nullable() {
        init();
        return annotation.nullable();
    }

    public void addListener(UpdateListener<T> updateListener) {
        this.updateListeners.add(updateListener);
    }

    public void removeListener(UpdateListener<T> updateListener) {
        this.updateListeners.remove(updateListener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViewBeanField)) {
            return false;
        }
        ViewBeanField that = (ViewBeanField) o;
        return bean.equals(that.bean) && fieldName.equals(that.fieldName);
    }

    @Override
    public int hashCode() {
        int result = bean.hashCode();
        result = 31 * result + fieldName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ViewBeanField{name='" + id + ", bean=" + bean + "'}";
    }

    public static interface UpdateListener<T> {

        void beforeUpdate(ViewBeanField<T> viewBeanField);
        void afterUpdate(ViewBeanField<T> viewBeanField);

    }

}
