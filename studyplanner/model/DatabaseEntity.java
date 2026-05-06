package com.studyplanner.model;

import java.lang.annotation.*;

/**
 * CUSTOM ANNOTATION: Annotations provide metadata about the program
 * They are not part of the program itself but provide information for compilation/runtime
 * 
 * Target(ElementType.TYPE) means this annotation can be applied to classes
 * Retention(RetentionPolicy.RUNTIME) means annotation is available at runtime (via Reflection)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DatabaseEntity {
    
    /**
     * Annotation element: defines properties that can be set when using the annotation
     * Example: @DatabaseEntity(tableName = "users")
     */
    String tableName() default "entity";
    
    String description() default "No description";
    
    boolean auditable() default true;
}
