package nl.spelberg.viewbean.wicket;

import nl.spelberg.viewbean.BeanEditAttribute;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

public class BeanEditFieldPanel<T> extends Panel implements BeanEditAttribute.UpdateListener<T> {

    private final BeanEditAttribute<T> beanEditAttribute;

    public BeanEditFieldPanel(String id, BeanEditAttribute<T> beanEditAttribute) {
        super(id);
        this.beanEditAttribute = beanEditAttribute;

        add(new Label("name", beanEditAttribute.id()));
        TextField<T> field = new TextField<T>("field", new BeanEditFieldModel<T>(beanEditAttribute));
        add(field);
        add(new FeedbackPanel("fieldFeedback", new ComponentFeedbackMessageFilter(BeanEditFieldPanel.this)));

        beanEditAttribute.addListener(this);
    }

    @Override
    protected void onRemove() {
        super.onRemove();
        beanEditAttribute.removeListener(this);
    }

    public void beforeUpdate(BeanEditAttribute<T> tBeanEditAttribute) {
        info(tBeanEditAttribute.id() + " Oude waarde: " + tBeanEditAttribute.value());
    }

    public void afterUpdate(BeanEditAttribute<T> tBeanEditAttribute) {
        info(tBeanEditAttribute.id() + " Nieuwe waarde: " + tBeanEditAttribute.value());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeanEditFieldPanel)) {
            return false;
        }
        BeanEditFieldPanel that = (BeanEditFieldPanel) o;
        return beanEditAttribute.equals(that.beanEditAttribute);
    }

    @Override
    public int hashCode() {
        return beanEditAttribute.hashCode();
    }

    @SuppressWarnings({"unchecked"})
    public static <T> BeanEditFieldPanel<T> createBeanEditFieldPanel(String id, BeanEditAttribute beanEditAttribute) {
        return new BeanEditFieldPanel<T>(id, beanEditAttribute);
    }
}
