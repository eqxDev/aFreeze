package me.eqxdev.afreeze.utils.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    String name();

    String description() default "";

    String permission() default "";

    boolean playerOnly() default false;

    String[] aliases() default {};
}