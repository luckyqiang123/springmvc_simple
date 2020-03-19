package cn.luckyqiang.servlet;

import cn.luckyqiang.annotation.*;
import cn.luckyqiang.controller.LuckyController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DispatcherServlet
 * @Description: TODO
 * @Author: zhangzhiqiang
 * @Date: 2020-03-16 21:47
 * @Company: www.luckyqiang.cn
 */
public class DispatcherServlet extends HttpServlet {

    List<String> classNames = new ArrayList<String>();

    //synchronized保证线程安全 ioc容器
    Map<String, Object> beans = new HashMap<String, Object>();

    //
    Map<String, Object> handleMap = new HashMap<String, Object>();

    //初始化容器，扫描 实例化bean URLMAPPING
    public void init(ServletConfig config) {
        //扫描
        scanPackage("cn.luckyqiang");

        doInstance();

        doAutowired();

        urlMapping();

    }

    //业务请求相关代码
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // /springmvc_simple_war_exploded/lucky/query
        String uri = request.getRequestURI();
        // /springmvc_simple_war_exploded
        String context = request.getContextPath();
        // lucky/query
        String path = uri.replace(context, "");
        // handleMap.get("lucky/query")
        Method method = (Method) handleMap.get(path);
        /**
         * "/lucky/query" -> "public void cn.luckyqiang.controller.LuckyController.query(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse,java.lang.String,java.lang.String)"
         */
        LuckyController instance = (LuckyController) beans.get("/" + path.split("/")[1]);
        Object args[] = hand(request, response, method);
        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void scanPackage(String basePackage) {
        //cn.luckyqiang --> /cn/luckyqiang
        URL url = this.getClass().getClassLoader().getResource("/" + basePackage.replaceAll("\\.", "/"));
        String filrStr = url.getFile();
        File file = new File(filrStr);
        String[] filesStr = file.list();
        for (String path : filesStr) {
            File filePath = new File(filrStr + path);
            if (filePath.isDirectory()) {
                scanPackage(basePackage + "." + path);
            } else {
                //list: cn.luckyqiang.servlet.DispatcherServlet.class
                classNames.add(basePackage + "." + filePath.getName());
            }
        }
    }

    private void doInstance() {
        for (String className : classNames) {
            String cn = className.replace(".class", "");
            try {
                Class<?> clazz = Class.forName(cn);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    Object instance = clazz.newInstance();
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    String key = requestMapping.value();
                    beans.put(key, instance);
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Object instance = clazz.newInstance();
                    Service service = clazz.getAnnotation(Service.class);
                    String key = service.value();
                    beans.put(key, instance);
                } else {
                    continue;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void doAutowired() {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(Controller.class)) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Autowired autowired = field.getAnnotation(Autowired.class);
                        String key = autowired.value();
                        field.setAccessible(true);
                        try {
                            field.set(instance, beans.get(key));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    } else {
                        continue;
                    }
                }
            } else if (clazz.isAnnotationPresent(Service.class)) {
                continue;
            }
        }
    }

    private void urlMapping() {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(Controller.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                String classPath = requestMapping.value();
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping mapping  = method.getAnnotation(RequestMapping.class);
                        String methodPath = mapping.value();
                        handleMap.put(classPath + methodPath, method);
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    /**
     * class1.isAssignableFrom(class2) 判定此 Class1 对象所表示的类或接口与指定的 Class2 参数所表示的类或接口
     * 是否相同，或是否是其超类或超接口。
     * @param request
     * @param response
     * @param method
     * @return
     */
    private static Object[] hand(HttpServletRequest request, HttpServletResponse response, Method method) {
        Class<?>[] paramClazzs = method.getParameterTypes();
        Object[] args = new Object[paramClazzs.length];
        int args_i = 0;
        int index = 0;
        for (Class<?> paramClazz : paramClazzs) {
            if (ServletRequest.class.isAssignableFrom(paramClazz)) {
                args[args_i++] = request;
            }
            if (ServletResponse.class.isAssignableFrom(paramClazz)) {
                args[args_i++] = response;
            }
            Annotation[] paramAns = method.getParameterAnnotations()[index];
            if (paramAns.length > 0) {
                for (Annotation paramAn : paramAns) {
                    if (RequestParam.class.isAssignableFrom(paramAn.getClass())) {
                        RequestParam rp = (RequestParam) paramAn;
                        args[args_i++] = request.getParameter(rp.value());
                    }
                }
            }
            index++;
        }
        return args;
    }
}
