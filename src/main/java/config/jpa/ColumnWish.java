package config.jpa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置类的属性和数据库字段的对应。
 * <br/>
 * 注意：匹配时是忽略大小写区别的。
 * <br/>
 * 如果只是大小写不同，可以不用加这个注解
 * @author lifw
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnWish {
	String name() default "";
}
