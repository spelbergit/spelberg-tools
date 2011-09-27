package nl.spelberg.viewbean.wicket;

import java.io.Serializable;
import nl.spelberg.viewbean.ViewField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ViewBeanPanelTest {

    private WicketTester wicketTester;

    @Before
    public void setUp() throws Exception {
        wicketTester = new WicketTester();
    }

    @Test
    public void testShowPanel() {

        ViewBeanPanel panel = new ViewBeanPanel("testPanel", new TestBean("A value for a String."));
        wicketTester.startComponentInPage(panel);

        String field1Id = panel.getFormFieldIdFor("simpleString");
        wicketTester.assertLabel(field1Id + ":name", "simpleString");
        wicketTester.assertComponent(field1Id + ":field", TextField.class);
        wicketTester.assertComponent(field1Id + ":fieldFeedback", FeedbackPanel.class);
        wicketTester.assertModelValue(field1Id + ":field", null);

        String field2Id = panel.getFormFieldIdFor("idString");
        wicketTester.assertLabel(field2Id + ":name", "idStringID");
        wicketTester.assertComponent(field2Id + ":field", TextField.class);
        wicketTester.assertComponent(field2Id + ":fieldFeedback", FeedbackPanel.class);
        wicketTester.assertModelValue(field2Id + ":field", null);

        String field3Id = panel.getFormFieldIdFor("aString");
        wicketTester.assertLabel(field3Id + ":name", "A String");
        wicketTester.assertComponent(field3Id + ":field", TextField.class);
        wicketTester.assertComponent(field3Id + ":fieldFeedback", FeedbackPanel.class);
        wicketTester.assertModelValue(field3Id + ":field", "A value for a String.");

        String field4Id = panel.getFormFieldIdFor("anInteger");
        wicketTester.assertLabel(field4Id + ":name", "anInteger");
        wicketTester.assertComponent(field4Id + ":field", TextField.class);
        wicketTester.assertComponent(field4Id + ":fieldFeedback", FeedbackPanel.class);
        wicketTester.assertModelValue(field4Id + ":field", Integer.valueOf(42));

        String field5Id = panel.getFormFieldIdFor("anInt");
        wicketTester.assertLabel(field5Id + ":name", "anInt");
        wicketTester.assertComponent(field5Id + ":field", TextField.class);
        wicketTester.assertComponent(field5Id + ":fieldFeedback", FeedbackPanel.class);
        wicketTester.assertModelValue(field5Id + ":field", 24);

    }

    @Test
    public void testSubmitText() {
        String oldValue = "A value for a String.";
        String newValue = "New String value for A";
        TestBean testBean = new TestBean(oldValue);
        ViewBeanPanel panel = new ViewBeanPanel("testPanel", testBean);
        String aStringFieldId = panel.getFieldIdFor("aString") + ":field";
        String aStringFormFieldId = panel.getFormFieldIdFor("aString") + ":field";

        wicketTester.startComponentInPage(panel);

        wicketTester.assertModelValue(aStringFormFieldId, oldValue);
        assertEquals(oldValue, testBean.aString);

        String inputForm = "inputForm";
        wicketTester.newFormTester(inputForm).setValue(aStringFieldId, newValue);
        wicketTester.submitForm(inputForm);

        wicketTester.assertModelValue(aStringFormFieldId, newValue);
        assertEquals(newValue, testBean.aString);
    }

    private class TestBean implements Serializable {

        @ViewField
        private String simpleString;

        @ViewField(id = "idStringID")
        private String idString;

        @ViewField(id = "aStringID", label = "A String")
        private String aString;

        @ViewField
        private Integer anInteger = Integer.valueOf(42);

        @ViewField
        private int anInt = 24;


        private TestBean(String aString) {
            this.aString = aString;
        }
    }
}
