package ink.toppest.secondskill.advice;

import java.lang.annotation.*;

/**
 * 描述:
 *      使用redis限流
 * @author HASEE
 * @create 2018-11-10 20:28
 */
@Documented
@Inherited
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentLimiting {
    String value();
}