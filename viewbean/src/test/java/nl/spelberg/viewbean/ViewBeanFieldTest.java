package nl.spelberg.viewbean;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ViewBeanFieldTest {

    private TestBean testBean;
    private ViewBeanField<String> normalAttributeView;
    private ViewBeanField<String> namedAttributeView;
    private ViewBeanField<String> readOnlyAttributeView;
    private ViewBeanField<String> finalReadOnlyAttributeView;
    private ViewBeanField<String> finalAttributeView;
    private ViewBeanField<String> nullableAttributeView;
    private ViewBeanField<Integer> intAttributeView;

    @Before
    public void init() {
        testBean = new TestBean("normal", "named", "readOnly", "finalReadOnly", "nullable", 42);
        normalAttributeView = new ViewBeanField<String>(testBean, ViewBeanUtils.fieldForName(testBean, "normalValue"));
        namedAttributeView = new ViewBeanField<String>(testBean, ViewBeanUtils.fieldForName(testBean, "namedValue"));
        readOnlyAttributeView = new ViewBeanField<String>(testBean, ViewBeanUtils.fieldForName(testBean, "readOnlyValue"));
        finalReadOnlyAttributeView = new ViewBeanField<String>(testBean,
                ViewBeanUtils.fieldForName(testBean, "finalReadOnlyValue"));
        finalAttributeView = new ViewBeanField<String>(testBean, ViewBeanUtils.fieldForName(testBean, "finalValue"));
        nullableAttributeView = new ViewBeanField<String>(testBean, ViewBeanUtils.fieldForName(testBean, "nullableValue"));
        intAttributeView = new ViewBeanField<Integer>(testBean, ViewBeanUtils.fieldForName(testBean, "intValue"));
    }

    @Test
    public void testName() throws Exception {
        assertEquals("normalValue", normalAttributeView.id());
        assertEquals("name of value", namedAttributeView.id());
        assertEquals("readOnlyValue", readOnlyAttributeView.id());
        assertEquals("finalReadOnlyValue", finalReadOnlyAttributeView.id());
        assertEquals("finalValue", finalAttributeView.id());
        assertEquals("nullableValue", nullableAttributeView.id());
        assertEquals("intValue", intAttributeView.id());
    }

    @Test
    public void testValue() throws Exception {
        assertEquals("normal", normalAttributeView.value());
        assertEquals("named", namedAttributeView.value());
        assertEquals("readOnly", readOnlyAttributeView.value());
        assertEquals("finalReadOnly", finalReadOnlyAttributeView.value());
        assertEquals(null, finalAttributeView.value());
        assertEquals("nullable", nullableAttributeView.value());
        assertEquals(Integer.valueOf(42), intAttributeView.value());
    }

    @Test
    public void testUpdate() throws Exception {
        normalAttributeView.update("newNormal");
        assertEquals("newNormal", normalAttributeView.value());
        assertEquals("newNormal", testBean.getNormalValue());

        namedAttributeView.update("newNamed");
        assertEquals("newNamed", namedAttributeView.value());
        assertEquals("newNamed", testBean.getNamedValue());

        try {
            readOnlyAttributeView.update("newReadOnly");
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("Illegal to update read-only on 'readOnlyValue' in bean class '" + TestBean.class.getName() + "'",
                    e.getMessage());
        }

        try {
            finalReadOnlyAttributeView.update("newFinalReadOnly");
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("Illegal to update read-only on 'finalReadOnlyValue' in bean class '" + TestBean.class.getName() + "'",
                    e.getMessage());
        }

        try {
            finalAttributeView.update("newFinal");
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("Illegal to update final on 'finalValue' in bean class '" + TestBean.class.getName() + "'",
                    e.getMessage());
        }

        nullableAttributeView.update("newNullable");
        assertEquals("newNullable", nullableAttributeView.value());
        assertEquals("newNullable", testBean.getNullableValue());

        intAttributeView.update(6);
        assertEquals(Integer.valueOf(6), intAttributeView.value());
        assertEquals(6, testBean.getIntValue());

    }

    @Test
    public void testNullableField() {
        nullableAttributeView.update(null);
        assertNull(nullableAttributeView.value());
        assertNull(testBean.getNullableValue());

        nullableAttributeView.update("not null anymore");
        assertEquals("not null anymore", nullableAttributeView.value());
        assertEquals("not null anymore", testBean.getNullableValue());

        try {
            normalAttributeView.update(null);
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals(
                    "Illegal to update non-nullable on 'normalValue' with <null> value in bean class '" + TestBean.class.getName() +
                            "'", e.getMessage());
        }
    }

    @Test
    public void testNotAnnotatedField() throws NoSuchFieldException {
        try {
            new ViewBeanField<String>(testBean, ViewBeanUtils.fieldForName(testBean, "notAnnotatedField"));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("on does not have the annotation @" + ViewField.class.getSimpleName(), e.getMessage());
        }
    }

    @Test
    public void testValueClass() {
        assertEquals(String.class, normalAttributeView.valueClass());
        assertEquals(int.class, intAttributeView.valueClass());
    }

    private static class TestBean {

        @ViewField
        private String normalValue;

        @ViewField(id = "name of value")
        private String namedValue;

        @ViewField(readOnly = true)
        private String readOnlyValue;

        @ViewField(readOnly = true)
        private final String finalReadOnlyValue;

        @ViewField
        private final String finalValue = null;

        @ViewField(nullable = true)
        private String nullableValue;

        @ViewField
        private int intValue;

        private String notAnnotatedField;

        private TestBean(String normalValue, String namedValue, String readOnlyValue, String finalReadOnlyValue,
                String nullableValue, int intValue) {
            this.normalValue = normalValue;
            this.namedValue = namedValue;
            this.readOnlyValue = readOnlyValue;
            this.finalReadOnlyValue = finalReadOnlyValue;
            this.nullableValue = nullableValue;
            this.intValue = intValue;
        }

        public String getNormalValue() {
            return normalValue;
        }

        public String getNamedValue() {
            return namedValue;
        }

        public String getReadOnlyValue() {
            return readOnlyValue;
        }

        public String getFinalReadOnlyValue() {
            return finalReadOnlyValue;
        }

        public String getFinalValue() {
            return finalValue;
        }

        public String getNullableValue() {
            return nullableValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public String getNotAnnotatedField() {
            return notAnnotatedField;
        }
    }

}
