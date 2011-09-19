package nl.spelberg.viewbean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotate fields to make them editable in the {@link nl.spelberg.viewbean.ViewBeanModel}.<br/>
 * <br/>
 * By default the value will be updated directly using reflection.
 */
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ViewField {

    /**
     * The id of the field, used in message lookup. Defaults to attribute name.
     */
    String id() default "";

    /**
     * The label of the field, If not set the id will be used..
     */
    String label() default "";

    /**
     * Set to true to render the attibute readonly. Defaults to false.
     */
    boolean readOnly() default false;

    /**
     * Set to true to allow null value for the attribute. Defaults to false.
     */
    boolean nullable() default false;

}
