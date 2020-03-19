package cn.luckyqiang.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: RequestMapping
 * @Description: TODO
 * @Author: zhangzhiqiang
 * @Date: 2020-03-16 22:05
 * @Company: www.luckyqiang.cn
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
