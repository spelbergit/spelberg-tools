package nl.spelberg.viewbean.wicket;

import nl.spelberg.viewbean.ViewBeanField;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

public class ViewBeanFieldPanel<T> extends Panel implements ViewBeanField.UpdateListener<T> {

    private final ViewBeanField<T> viewBeanField;

    public ViewBeanFieldPanel(String id, ViewBeanField<T> viewBeanField) {
        super(id);
        this.viewBeanField = viewBeanField;

        add(new Label("name", viewBeanField.label()));
        TextField<T> field = new TextField<T>("field", new ViewBeanFieldModel<T>(viewBeanField));
        add(field);
        add(new FeedbackPanel("fieldFeedback", new ComponentFeedbackMessageFilter(ViewBeanFieldPanel.this)));

        viewBeanField.addListener(this);
    }

    @Override
    protected void onRemove() {
        super.onRemove();
        viewBeanField.removeListener(this);
    }

    public void beforeUpdate(ViewBeanField<T> tViewBeanField) {
        info(tViewBeanField.id() + " Oude waarde: " + tViewBeanField.value());
    }

    public void afterUpdate(ViewBeanField<T> tViewBeanField) {
        info(tViewBeanField.id() + " Nieuwe waarde: " + tViewBeanField.value());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViewBeanFieldPanel)) {
            return false;
        }
        ViewBeanFieldPanel that = (ViewBeanFieldPanel) o;
        return viewBeanField.equals(that.viewBeanField);
    }

    @Override
    public int hashCode() {
        return viewBeanField.hashCode();
    }

    @SuppressWarnings({"unchecked"})
    public static <T> ViewBeanFieldPanel<T> createBeanEditFieldPanel(String id, ViewBeanField viewBeanField) {
        return new ViewBeanFieldPanel<T>(id, viewBeanField);
    }
}
