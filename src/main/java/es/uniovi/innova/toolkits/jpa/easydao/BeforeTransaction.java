package es.uniovi.innova.toolkits.jpa.easydao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author miguelff
 * 
 *         Methods anotated with this annotation will be executed before the
 *         transaction begins.
 * 
 *         Each of the methods annotated will be executed but in no paticular
 *         order. Hence, it's recommended to annotate at most one method to be
 *         executed before transaction begins.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeTransaction {}
