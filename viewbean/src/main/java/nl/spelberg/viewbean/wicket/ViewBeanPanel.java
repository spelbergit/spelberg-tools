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
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bean Editor panel. Provides an edit view on a given bean, on each attribute annotated with {@link nl.spelberg.viewbean.ViewField}.
 */
public class ViewBeanPanel extends Panel {

    private static final Logger log = LoggerFactory.getLogger(ViewBeanPanel.class);

    public ViewBeanPanel(String id, Serializable bean) {
        this(id, new Model<Serializable>(bean));
    }

    public ViewBeanPanel(String id, IModel<?> model) {
        super(id, model);
        Object bean = model.getObject();
        ViewBeanModel<Object> viewBeanModel = new ViewBeanModel<Object>(bean);

        add(new Label("className", bean.getClass().getName()));

        Form<ViewBeanModel<Object>> inputForm = new Form<ViewBeanModel<Object>>("inputForm");

        inputForm.add(new ListView<ViewBeanField>("fieldList", new ArrayList<ViewBeanField>(viewBeanModel.fields())) {

            @Override
            protected void populateItem(ListItem<ViewBeanField> listItem) {
                ViewBeanField viewBeanField = listItem.getModelObject();
                listItem.add(ViewBeanFieldPanel.createBeanEditFieldPanel("viewBeanField", viewBeanField));
            }

        });
        inputForm.add(new Button("submitButton"));

        add(inputForm);

    }

}
