1. 项目搭建steps:
 - 搭建父项目:mall-demo
 - 建注册中心模块：mall-registry
 - 建gateway: mall-gateway
 - 建商品模块：拆分商品模块：item
 - common工具模块
 
  - 统一异常处理
 
 设置域名在hosts
 
 - ES:商品搜索
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
   
   
 3.    
 The bean 'item-service.FeignClientSpecification', defined in null, 
 could not be registered. A bean with that name has already been defined in null 
 and overriding is disabled.
 spring:
    main:
     allow-bean-definition-overriding: true 
     
 在springboot2.1之前默认是true
 
 #thymleaf
 默认在classpath:/templates; 默认后缀.html.

ctrl+shift+f9:recompile html: 不用重启项目。

页面静态化：解决高并发：不经过服务器的渲染。

当后台的数据发生变化的时候，ES，html里面的数据也要改变？
MQ。

只发送商品id。

docker run -d --hostname my-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3.7.3-management

docker run --name some-redis -d redis:4 redis-server --appendonly yes
1. redis 与memcache

- redis 单线程；memcache:多thread
- redis:多数据结构；memcache:k-v
- redis: 有持久化；memcache:no
- redis: 分片，有；无


##redis这里会链接不上。