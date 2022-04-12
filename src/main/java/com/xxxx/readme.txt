关于Controller的注解问题

1.RestController  直接返回了String字符串
2.Controller  返回的是一个页面
3.RestController其实是RestBody+Controller


1.ResponseBody  (返回对象而不是一个页面)
    @Responsebody 注解表示该方法的返回的结果直接写入 HTTP 响应正文（ResponseBody）中，一般在异步获取数据时使用；
    通常是在使用 @RequestMapping 后，返回值通常解析为跳转路径，加上 @Responsebody 后返回结果不会被解析为跳转路径，而是直接写入HTTP 响应正文中。
    作用：
    该注解用于将Controller的方法返回的对象，通过适当的HttpMessageConverter转换为指定格式后，写入到Response对象的body数据区。
    使用时机：
    返回的数据不是html标签的页面，而是其他某种格式的数据时（如json、xml等）使用；
2.RequestBody
  @RequestBody是作用在形参列表上，用于将前台发送过来固定格式的数据【xml 格式或者 json等】封装为对应的 JavaBean 对象，
  封装时使用到的一个对象是系统默认配置的 HttpMessageConverter进行解析，然后封装到形参上。

springboot全局异常处理主要是两种：
1.@ControllerAdvice  +@ExceptionHandler   只能处理控制器异常
2.使用ErrorController类实现


分布式session的解决方案
1.使用spring来解决
    不需要变更代码

关于cookie和session
1.为什么需要session和cookie？
    源于web系统的发展和变迁。web1.0强调的是资源的共享，http协议是一种无状态的协议。
     web2.0强调的是交互。交互意味着是有多步操作，请求和请求之间有依赖存在。
     引入了session和cookie机制是来实现状态的记录。

2.Session和cookie的特征
    （1）session和cookie都是服务器生成的（程序员写的）,都是用来存储特定的值。（键值对应）
    （2）session存储在服务器，cookie是返回给客户端。
        一般来说，SessionID会以类似cookie的方法返回给客户端。
        sessionID是服务器用来识别操作存储session值的对象的。
        在服务端，session的存储方法有文件方式，数据库方法。sessionID就是识别这个文件，识别数据库某一条记录。
        客户端（浏览器）在发送请求的时候，会自动将存活的，可用的cookie封装在请求头中发送过去。

3.关于存活周期
    cookie的生命周期，受到两个因素的制约。cookie自身的存活时间（服务器设定）。 客户端是否保留cookie。
    session的生命周期，服务器对session的对象的最大时间的设置。客户端进程是否关闭。



关于RabbitMQ的知识点
1.接收消息的客户端只能绑定一个队列而不是绑定路由器。

2.RabbitMQ的六种模式
    生产者关心队列
    (1)简单模式，一个生产者一个消费者。通过队列相连接。
    (2)工作模式，一个队列有两个消费者消费数据。
    生产者关心路由器
    (3)订阅与发布模式，加入了一层交换机。每一个消费者只监听和自己绑定的队列。
    (4)路由模式，通过指定不同的RountingKey，将消息发布到不同的队列中去。
    (5)topic主题模式，因为路由模式一定RountingKey很多的话难以管理。使用通配符匹配的模式。
    注意一个队列匹配了多条的话，只发送来一条消息。 #是有或者没有 *是必须有
    (6)RPC模式，



关于Redis
1.ValueOperations valueOperations=redisTemplate.opsForValue()
这个操作的意思是从redisTemplate获取实际可以操作的对象。
通过这个对象来添加数据和获取数据。


限流算法：
1.计数器算法
    通过计算1s之内的流量的总数，超过一个阈值的话就进不去接口中。
    如何使用redis来做这个问题？
    给每一个用户一个id，里面的value是用户请求的次数，一开始是0，
    每请求一次那就加一，设置失效的时间为1分钟。
    缺点：
        假设现在服务器最大承受的QPS是150  按照限流为70%~80% 就以100来算
        1.临界问题，以1秒钟为例，后0.1s请求的次数是100 第二秒钟的前0.1s请求了100，也是不正确的。
        2.服务器空闲问题，以1秒钟为例，前0.5s处理完了100个请求，后0.5s服务器空闲。

2.漏桶算法(一般使用队列来解决)
    用于流量的均衡处理
    缺点在于如果有大量的请求过来的话，很有可能会把水桶撑爆，也会导致服务器挂掉。

3.令牌桶算法
    根据QPS将 QPS是10的话那就没1/10秒钟就产生一个令牌放到令牌桶中，来的流量先去令牌桶中获取一个令牌
    然后拿到令牌后继续往后走。




如何自定义一个注解？
需求：实现每一个接口在固定的时间内限制固定的访问次数。
在每一个方法的handler上面添加一个注解(时间,次数,是否需要登录)
实现一个注解的步骤：
(1)public @interface 注解名字
(2)设置相关的参数  添加注解 运行时  放在方法上的注解









