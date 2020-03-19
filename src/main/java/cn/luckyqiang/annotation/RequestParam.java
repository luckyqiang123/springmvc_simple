package cn.luckyqiang.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: RequestParam
 * @Description: TODO
 * @Author: zhangzhiqiang
 * @Date: 2020-03-16 22:05
 * @Company: www.luckyqiang.cn
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
