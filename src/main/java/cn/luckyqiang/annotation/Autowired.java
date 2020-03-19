package cn.luckyqiang.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: Autowired
 * @Description: TODO
 * @Author: zhangzhiqiang
 * @Date: 2020-03-16 22:05
 * @Company: www.luckyqiang.cn
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
