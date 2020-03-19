# springmvc_simple
### Tomcat启动
1. 加载war包xxx.war
2. 创建容器：创建Map iocMap = new HashMap()；
3. ScanBasePackage：扫描war下的@Controller，@Service注解的类
4. 实例化：将扫描到的类通过反射进行实例化，并存入iocMap容器中
5. 依赖注入：将存在依赖的bean进行注入
6. urlMapping：http请求路径于Method建立映射关系



### Tomcat启动成功运行阶段
1. 发送http请求，调用servlet的doGet和doPost方法
2. 找到从UrlMapping中对应的Method方法对象
3. 找到Method方法对象后，直接调用method.invoke()方法
4. 返回响应结果


### 反射知识
1. 获取Class对象
```
Class<?> clazz = Class.forName(cn.luckyqiang.service.impl.LuckyServiceImpl)
```
2.反射创建LuckyServiceImpl实例

```
LuckyServiceImpl luckyServiceImpl = clazz.newInstance();
```
3.根据实例拿到类实例Class对象

```
Class<?> clazz = luckyServiceImpl.getClass()

```
4.拿到类里面定义的所有属性

```
Field[] fields = clazz.getDeclaredFields();
```
5.获取类里面的所有方法

```
Method[] methods = clazz.getMethods();
```
6.从底层调用方法，方法里的参数数组为args[]
```
method.invoke(instance, args);
```
7.获取请求路径

```
// ~~ip:端口~~/springmvc_simple_war_exploded/lucky/query
String uri = request.getRequestURI();
```




