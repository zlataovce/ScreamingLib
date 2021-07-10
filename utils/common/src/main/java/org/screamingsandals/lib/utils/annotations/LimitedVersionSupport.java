package org.screamingsandals.lib.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface LimitedVersionSupport {
    String value() default "";
}
