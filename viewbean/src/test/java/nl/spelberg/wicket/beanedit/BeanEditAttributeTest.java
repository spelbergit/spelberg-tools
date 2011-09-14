package nl.spelberg.wicket.beanedit;

import nl.spelberg.viewbean.BeanEditAttribute;
import nl.spelberg.viewbean.BeanEditUtils;
import nl.spelberg.viewbean.ViewField;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BeanEditAttributeTest {

    private TestBean testBean;
    private BeanEditAttribute<String> normalAttribute;
    private BeanEditAttribute<String> namedAttribute;
    private BeanEditAttribute<String> readOnlyAttribute;
    private BeanEditAttribute<String> finalReadOnlyAttribute;
    private BeanEditAttribute<String> finalAttribute;
    private BeanEditAttribute<String> nullableAttribute;
    private BeanEditAttribute<Integer> intAttribute;

    @Before
    public void init() {
        testBean = new TestBean("normal", "named", "readOnly", "finalReadOnly", "nullable", 42);
        normalAttribute = new BeanEditAttribute<String>(testBean, BeanEditUtils.fieldForName(testBean, "normalValue"));
        namedAttribute = new BeanEditAttribute<String>(testBean, BeanEditUtils.fieldForName(testBean, "namedValue"));
        readOnlyAttribute = new BeanEditAttribute<String>(testBean, BeanEditUtils.fieldForName(testBean, "readOnlyValue"));
        finalReadOnlyAttribute = new BeanEditAttribute<String>(testBean, BeanEditUtils.fieldForName(testBean,
                "finalReadOnlyValue"));
        finalAttribute = new BeanEditAttribute<String>(testBean, BeanEditUtils.fieldForName(testBean, "finalValue"));
        nullableAttribute = new BeanEditAttribute<String>(testBean, BeanEditUtils.fieldForName(testBean, "nullableValue"));
        intAttribute = new BeanEditAttribute<Integer>(testBean, BeanEditUtils.fieldForName(testBean, "intValue"));
    }

    @Test
    public void testName() throws Exception {
        assertEquals("normalValue", normalAttribute.id());
        assertEquals("name of value", namedAttribute.id());
        assertEquals("readOnlyValue", readOnlyAttribute.id());
        assertEquals("finalReadOnlyValue", finalReadOnlyAttribute.id());
        assertEquals("finalValue", finalAttribute.id());
        assertEquals("nullableValue", nullableAttribute.id());
        assertEquals("intValue", intAttribute.id());
    }

    @Test
    public void testValue() throws Exception {
        assertEquals("normal", normalAttribute.value());
        assertEquals("named", namedAttribute.value());
        assertEquals("readOnly", readOnlyAttribute.value());
        assertEquals("finalReadOnly", finalReadOnlyAttribute.value());
        assertEquals(null, finalAttribute.value());
        assertEquals("nullable", nullableAttribute.value());
        assertEquals(Integer.valueOf(42), intAttribute.value());
    }

    @Test
    public void testUpdate() throws Exception {
        normalAttribute.update("newNormal");
        assertEquals("newNormal", normalAttribute.value());
        assertEquals("newNormal", testBean.getNormalValue());

        namedAttribute.update("newNamed");
        assertEquals("newNamed", namedAttribute.value());
        assertEquals("newNamed", testBean.getNamedValue());

        try {
            readOnlyAttribute.update("newReadOnly");
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("Illegal to update read-only on 'readOnlyValue' in bean class '" + TestBean.class.getName() + "'",
                    e.getMessage());
        }

        try {
            finalReadOnlyAttribute.update("newFinalReadOnly");
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("Illegal to update read-only on 'finalReadOnlyValue' in bean class '" + TestBean.class.getName() + "'",
                    e.getMessage());
        }

        try {
            finalAttribute.update("newFinal");
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("Illegal to update final on 'finalValue' in bean class '" + TestBean.class.getName() + "'",
                    e.getMessage());
        }

        nullableAttribute.update("newNullable");
        assertEquals("newNullable", nullableAttribute.value());
        assertEquals("newNullable", testBean.getNullableValue());

        intAttribute.update(6);
        assertEquals(Integer.valueOf(6), intAttribute.value());
        assertEquals(6, testBean.getIntValue());

    }

    @Test
    public void testNullableField() {
        nullableAttribute.update(null);
        assertNull(nullableAttribute.value());
        assertNull(testBean.getNullableValue());

        nullableAttribute.update("not null anymore");
        assertEquals("not null anymore", nullableAttribute.value());
        assertEquals("not null anymore", testBean.getNullableValue());

        try {
            normalAttribute.update(null);
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("Illegal to update non-nullable on 'normalValue' with <null> value in bean class '" +
                    TestBean.class.getName() + "'", e.getMessage());
        }
    }

    @Test
    public void testNotAnnotatedField() throws NoSuchFieldException {
        try {
            new BeanEditAttribute<String>(testBean, BeanEditUtils.fieldForName(testBean, "notAnnotatedField"));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("on does not have the annotation @" + ViewField.class.getSimpleName(), e.getMessage());
        }
    }

    @Test
    public void testValueClass() {
        assertEquals(String.class, normalAttribute.valueClass());
        assertEquals(int.class, intAttribute.valueClass());
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
