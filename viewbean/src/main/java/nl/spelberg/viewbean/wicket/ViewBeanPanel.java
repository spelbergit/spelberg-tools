package nl.spelberg.viewbean.wicket;

import java.io.Serializable;
import java.util.ArrayList;
import nl.spelberg.viewbean.ViewBeanField;
import nl.spelberg.viewbean.ViewBeanModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Bean Editor panel. Provides an edit view on a given bean, on each attribute annotated with {@link nl.spelberg.viewbean.ViewField}.
 */
public class ViewBeanPanel extends Panel {

    private static final Logger log = LoggerFactory.getLogger(ViewBeanPanel.class);
    private static final String INPUT_FORM = "inputForm";
    private static final String FIELD_LIST = "fieldList";
    private static final String VIEW_BEAN_FIELD = "viewBeanField";

    public ViewBeanPanel(String id, final Serializable bean) {
        this(id, new LoadableDetachableModel<ViewBeanModel>() {
            @Override
            protected ViewBeanModel load() {
                return new ViewBeanModel<Serializable>(bean);
            }
        });
    }

    public ViewBeanPanel(String id, IModel<ViewBeanModel> model) {
        super(id, model);
        ViewBeanModel<?> viewBeanModel = model.getObject();

        add(new Label("className", viewBeanModel.getBeanClass().getName()));

        Form<ViewBeanModel<Object>> inputForm = new Form<ViewBeanModel<Object>>(INPUT_FORM);

        inputForm.add(new ListView<ViewBeanField>(FIELD_LIST, new ArrayList<ViewBeanField>(viewBeanModel.fields())) {


            @Override
            protected void populateItem(ListItem<ViewBeanField> listItem) {
                ViewBeanField viewBeanField = listItem.getModelObject();
                listItem.add(ViewBeanFieldPanel.createBeanEditFieldPanel(VIEW_BEAN_FIELD, viewBeanField));
            }

        });
        inputForm.add(new Button("submitButton"));

        add(inputForm);

    }

    public String getFieldIdFor(String attributeName) {
        Assert.notNull(attributeName, "attributeName is null");
        int attributeIndex = getViewBeanModel().fieldIndexForName(attributeName);
        return FIELD_LIST + ":" + attributeIndex + ":" + VIEW_BEAN_FIELD;
    }

    public String getFormFieldIdFor(String attributeName) {
        return INPUT_FORM + ":" + getFieldIdFor(attributeName);
    }

    private ViewBeanModel getViewBeanModel() {
        return (ViewBeanModel) getDefaultModelObject();
    }
}
