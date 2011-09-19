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

        wicketTester.startComponentInPage(new ViewBeanPanel("testPanel", new TestBean("A value for a String.")));

        wicketTester.assertLabel("inputForm:fieldList:0:viewBeanField:name", "simpleString");
        wicketTester.assertComponent("inputForm:fieldList:0:viewBeanField:field", TextField.class);
        wicketTester.assertComponent("inputForm:fieldList:0:viewBeanField:fieldFeedback", FeedbackPanel.class);
        wicketTester.assertModelValue("inputForm:fieldList:0:viewBeanField:field", null);

        wicketTester.assertLabel("inputForm:fieldList:1:viewBeanField:name", "idStringID");
        wicketTester.assertComponent("inputForm:fieldList:1:viewBeanField:field", TextField.class);
        wicketTester.assertComponent("inputForm:fieldList:1:viewBeanField:fieldFeedback", FeedbackPanel.class);
        wicketTester.assertModelValue("inputForm:fieldList:1:viewBeanField:field", null);

        wicketTester.assertLabel("inputForm:fieldList:2:viewBeanField:name", "A String");
        wicketTester.assertComponent("inputForm:fieldList:2:viewBeanField:field", TextField.class);
        wicketTester.assertComponent("inputForm:fieldList:2:viewBeanField:fieldFeedback", FeedbackPanel.class);
        wicketTester.assertModelValue("inputForm:fieldList:2:viewBeanField:field", "A value for a String.");

    }

    @Test
    public void testSubmitText() {
        TestBean testBean = new TestBean("A value for a String.");
        wicketTester.startComponentInPage(new ViewBeanPanel("testPanel", testBean));

        wicketTester.newFormTester("inputForm").setValue("fieldList:2:viewBeanField:field", "New String value for A");
        wicketTester.submitForm("inputForm");

        wicketTester.assertModelValue("inputForm:fieldList:2:viewBeanField:field", "New String value for A");
        assertEquals("New String value for A", testBean.aString);
    }

    private class TestBean implements Serializable {

        @ViewField
        private String simpleString;

        @ViewField(id = "idStringID")
        private String idString;

        @ViewField(id = "aStringID", label = "A String")
        private String aString;

        private TestBean(String aString) {
            this.aString = aString;
        }
    }
}
