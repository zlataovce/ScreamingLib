package org.screamingsandals.lib.utils.annotations.methods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The OnPostEnable annotation is used to mark method in {@link org.screamingsandals.lib.utils.annotations.Service}
 * which can be bound into Controllable.
 * Each service can have only OnPostEnable method.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface OnPostEnable {
}
