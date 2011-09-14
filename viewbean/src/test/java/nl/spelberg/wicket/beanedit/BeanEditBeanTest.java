package nl.spelberg.wicket.beanedit;

import java.util.Date;
import nl.spelberg.viewbean.BeanEditAttribute;
import nl.spelberg.viewbean.BeanEditBean;
import nl.spelberg.viewbean.BeanEditUtils;
import nl.spelberg.viewbean.ViewField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BeanEditBeanTest {

    @Test
    public void testNoAttributes() {
        BeanEditBean bean = new BeanEditBean<Object>(new Object());

        assertNotNull(bean.fields());
        assertEquals(0, bean.fields().size());

        try {
            new BeanEditBean<Object>(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("bean is null", e.getMessage());
        }
    }

    @Test
    public void testSimpleAttributes() {
        SimpleBean simpleBean = new SimpleBean("simple value");
        BeanEditBean<SimpleBean> bean = new BeanEditBean<SimpleBean>(simpleBean);

        assertNotNull(bean.fields());
        assertEquals(1, bean.fields().size());

        BeanEditAttribute<String> valueAttribute = bean.fieldWithName("value", String.class);

        assertEquals(new BeanEditAttribute<String>(simpleBean, BeanEditUtils.fieldForName(simpleBean, "value")),
                bean.fields().iterator().next());

        assertEquals("value", valueAttribute.id());
        assertEquals("simple value", valueAttribute.value());
        assertFalse(valueAttribute.readOnly());
        assertFalse(valueAttribute.nullable());
    }

    private static class SimpleBean {

        @ViewField
        private String value;

        public SimpleBean(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static class ComplexBean {

        private String id;

        @ViewField
        private String name;

        @ViewField(nullable = true)
        private Date birtDate;

        @ViewField(readOnly = true)
        private int leeftijd;

        @ViewField(id = "extra-info")
        private String description;

        private ComplexBean(String id, String name, int leeftijd, String description) {
            this.id = id;
            this.name = name;
            this.leeftijd = leeftijd;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getLeeftijd() {
            return leeftijd;
        }

        public String getDescription() {
            return description;
        }
    }

}
