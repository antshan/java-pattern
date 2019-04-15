# 设计模式总结
| 设计模式 | 一句话总结 | 应用 |
| -------------- | -------------- |--------------|
| 工厂模式（Factory） | 只对结果负责，封装创建过程 | BeanFactory|
|单例模式（singleton）|保证独一无二|ApplicationContext,Config|
|原型模式（prototype）|一根猴毛，吹出万千个|ArrayList,ProtoTypeBean|
|代理模式（Proxy）|找人办事，功能增强|jdkProxy，cglibProxy|
|委派模式（Delegate）|干活是你的（普通员工），功劳算我的（项目经理）||
|策略模式（Strategy）|用户选择，结果唯一|InstontitonStrategy|
|模板模式（Templete）|流程标准化，自己实现定义|JdbcTemplete,RedisStringTemplete|
|适配器模式(Adapter)|兼容转换头|HandlerAdapter|
|装饰者模式(Decorator)|包装，同宗同源|BetyArrayInputStream|
|观察者模式(Observer)|任务完成是通知|Event|

#列举SpringAop,IOC,DI的应用片段
## SpringAop
```
@Aspect
public class Aop {

	@Pointcut("execution(* com.gupao.springcode.service..*.*(..))")
	public void pointcut(){}

	@Before("pointcut()")
	public void before(){
		System.out.println("before");
	}

	@After("pointcut()")
	public void after(){
		System.out.println("after");
	}

	@Around("pointcut")
	public void arond(){
		System.out.println("rond");
	}

	@AfterThrowing(pointcut="pointcut()",throwing="error")
	public void throwsExpetction(){
		System.out.println("throwsExpetction");
	}
}
```

## IOC
@Autowired
private XxxService xxxService;

##DI 
@Resource
public Strig setPropety(String str){    
.....   
}



