package nl.spelberg.viewbean.wicket;

import nl.spelberg.viewbean.BeanEditAttribute;
import org.apache.wicket.model.IModel;
import org.springframework.util.Assert;

public class BeanEditFieldModel<T> implements IModel<T> {

    private final BeanEditAttribute<T> beanEditAttribute;

    public BeanEditFieldModel(BeanEditAttribute<T> beanEditAttribute) {
        Assert.notNull(beanEditAttribute, "beanEditAttribute is null");
        this.beanEditAttribute = beanEditAttribute;
    }

    public T getObject() {
        return beanEditAttribute.value();
    }

    public void setObject(T value) {
        beanEditAttribute.update(value);
    }

    public void detach() {
    }
}
