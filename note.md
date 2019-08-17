1. 项目搭建steps:
 - 搭建父项目:mall-demo
 - 建注册中心模块：mall-registry
 - 建gateway: mall-gateway
 - 建商品模块：拆分商品模块：item
 - common工具
 
 统一异常处理：
 
 
 启动：test:启动顺序：registry, item,gateway
 1.Failed to configure a DataSource: 'url' attribute is not specified
 
 在@SpringbootApplication上
 @SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
 
 2.Caused by: 
 java.lang.ClassNotFoundException:
  com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect
       at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
      引入dependency:  
   <dependency>
       <groupId>com.netflix.hystrix</groupId>
       <artifactId>hystrix-javanica</artifactId>
       <version>1.5.12</version>
   </dependency>