/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.dosanko.csvutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author igahito
 */

@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface CSVAnnotation {

        String name() default ""; //CSVフィールド名　入力用

        int priority() default Integer.MAX_VALUE; //CSV列番号　出力用
}
