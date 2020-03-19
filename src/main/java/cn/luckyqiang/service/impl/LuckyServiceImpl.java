package cn.luckyqiang.service.impl;

import cn.luckyqiang.annotation.Service;
import cn.luckyqiang.service.api.LuckyService;

/**
 * @ClassName: LuckyServiceImpl
 * @Description: TODO
 * @Author: zhangzhiqiang
 * @Date: 2020-03-16 17:55
 * @Company: www.luckyqiang.cn
 */
@Service("luckyServiceImpl")//iocMap.put("luckyServiceImpl", new LuckyServiceImpl())
public class LuckyServiceImpl implements LuckyService {
    public String query(String name, String age) {
        return "{name = " + name + ", age = " + age + "}";
    }
}
