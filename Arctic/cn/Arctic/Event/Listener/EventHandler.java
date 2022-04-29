/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Event.Listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.Arctic.Event.Priority;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface EventHandler {
    public byte priority() default 1;
    byte value() default Priority.MEDIUM;
}

