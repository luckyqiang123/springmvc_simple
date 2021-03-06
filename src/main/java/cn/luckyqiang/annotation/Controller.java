package cn.luckyqiang.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: Controller
 * @Description: TODO
 * @Author: zhangzhiqiang
 * @Date: 2020-03-16 22:05
 * @Company: www.luckyqiang.cn
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
