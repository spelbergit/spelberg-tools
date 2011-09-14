package nl.spelberg.viewbean.wicket;

import java.io.Serializable;
import java.util.ArrayList;
import nl.spelberg.viewbean.BeanEditAttribute;
import nl.spelberg.viewbean.BeanEditBean;
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
public class BeanEditor extends Panel {

    private static final Logger log = LoggerFactory.getLogger(BeanEditor.class);

    public BeanEditor(String id, Serializable bean) {
        this(id, new Model<Serializable>(bean));
    }

    public BeanEditor(String id, IModel<?> model) {
        super(id, model);
        Object bean = model.getObject();
        BeanEditBean<Object> beanEditBean = new BeanEditBean<Object>(bean);

        add(new Label("className", bean.getClass().getName()));

        Form<BeanEditBean<Object>> inputForm = new Form<BeanEditBean<Object>>("inputForm");

        inputForm.add(new ListView<BeanEditAttribute>("fieldList", new ArrayList<BeanEditAttribute>(beanEditBean.fields())) {

            @Override
            protected void populateItem(ListItem<BeanEditAttribute> listItem) {
                BeanEditAttribute beanEditAttribute = listItem.getModelObject();
                listItem.add(BeanEditFieldPanel.createBeanEditFieldPanel("beanEditAttribute", beanEditAttribute));
            }

        });
        inputForm.add(new Button("submitButton"));

        add(inputForm);

    }

}
