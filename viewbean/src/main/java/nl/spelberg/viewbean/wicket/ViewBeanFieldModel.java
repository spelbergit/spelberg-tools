package nl.spelberg.viewbean.wicket;

import nl.spelberg.viewbean.ViewBeanField;
import org.apache.wicket.model.IModel;
import org.springframework.util.Assert;

public class ViewBeanFieldModel<T> implements IModel<T> {

    private final ViewBeanField<T> viewBeanField;

    public ViewBeanFieldModel(ViewBeanField<T> viewBeanField) {
        Assert.notNull(viewBeanField, "viewBeanField is null");
        this.viewBeanField = viewBeanField;
    }

    public T getObject() {
        return viewBeanField.value();
    }

    public void setObject(T value) {
        viewBeanField.update(value);
    }

    public void detach() {
    }
}
