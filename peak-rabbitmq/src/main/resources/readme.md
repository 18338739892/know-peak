### 基础知识
1. 此消息队列为RabbitMq消息队列
2. rabbitMq主要涉及到[队列,生产者,消费者]
3. 生产者和消费者都可以创建队列
4. 队列可以多次创建,但是如果第二次创建的时候,参数与之前的不一样，虽然现实成功，但是参数不会改变
5. 生产者把消息(同时带上routing key)发送Exchange
6. rabbitmq数据具有数据缓存机制,没有消费者消费时,会进行暂时缓存。
7. 如果程序有bug,忘记ask,那么RabbitMqServer不会将消息再次发送给它，因为服务器任务这个消费者处理有限
8. 概念说明

    | 概念  |   说明  |
    |---|  --- |
    |Broker|它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,| 
    |Exchange|消息交换机,它指定消息按什么规则,路由到哪个队列。 |
    |Queue|消息的载体,每个消息都会被投到一个或多个队列。 |
    |Binding|绑定，它的作用就是把exchange和queue按照路由规则绑定起来.| 
    |Routing Key|路由关键字,exchange根据这个关键字进行消息投递。 |
    |vhost|虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。| 
    |Producer|消息生产者,就是投递消息的程序. |
    |Consumer|消息消费者,就是接受消息的程序. |
    |Channel|消息通道,在客户端的每个连接里,可建立多个channel.|
9. 可以使用hannel.ExchangeDeclare进行exchange持久化,channel.QueueDeclare队列持久化,在投递时指定delivery_mode => 2(1是非持久化)消息持久化
    - 如果exchange和queue都是持久化的,那么它们之间的binding也是持久化的,如果exchange和queue两者之间有一个持久化，一个非持久化,则不允许建立绑定.
    - 注意：一旦创建了队列和交换机,就不能修改其标志了,例如,创建了一个non-durable的队列,然后想把它改变成durable的,唯一的办法就是删除这个队列然后重现创建。 

10. 任务分发机制
    - Round-robin dispathching循环分发
        - RabbbitMQ的分发机制非常适合扩展,而且它是专门为并发程序设计的,如果现在load加重,那么只需要创建更多的Consumer来进行任务处理。
    - Message acknowledgment消息确认
        - 消息发送之后,消息需要对消息进行消息发送成功与否进行确认,如果消息服务器关闭，此时还没有ask,则此消息重新发送
11. 消息分发机制
    - Fair dispath 公平分发
        - 分发机制不是那么优雅,默认状态下,RabbitMQ将第n个Message分发给第n个Consumer。n是取余后的,它不管Consumer是否还有unacked Message，只是按照这个默认的机制进行分发. 
        - 那么如果有个Consumer工作比较重,那么就会导致有的Consumer基本没事可做,有的Consumer却毫无休息的机会,那么,Rabbit是如何处理这种问题呢?
        - 通过basic.qos方法设置prefetch_count=1，这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message，换句话说,在接收到该Consumer的ack前,它不会将新的Message分发给它
            `channel.basic_qos(prefetch_count=1) `,当然也可以在配置文件中配置
            
12. 交换路由的几种类型
    - Direct Exchange:直接匹配,通过Exchange名称+RountingKey来发送与接收消息.【如果路由键完全匹配的话，消息才会被投放到相应的队列。】
    - Fanout Exchange:广播订阅,向所有的消费者发布消息,但是只有消费者将队列绑定到该路由器才能收到消息,忽略Routing Key【当发送一条消息到fanout交换器上时，它会把消息投放到所有附加在此交换器上的队列。】
    - Topic Exchange：主题匹配订阅,这里的主题指的是RoutingKey,RoutingKey可以采用通配符,如:*或#，RoutingKey命名采用.来分隔多个词,只有消息这将队列绑定到该路由器且指定RoutingKey符合匹配规则时才能收到消息【设置模糊的绑定方式，“*”操作符将“.”视为分隔符，匹配单个字符；“#”操作符没有分块的概念，它将任意“.”均视为关键字的匹配部分，能够匹配多个字符。】
    
13. [RabbitMq的五种队列模式](https://www.cnblogs.com/ysocean/p/9251884.html)
    - 一个生产者对应一个消费者【生产者将消息发送到“hello”队列。消费者从该队列接收消息。】
    - work 模式【一个生产者对应多个消费者，但是只能有一个消费者获得消息！！！】
        - 消费者1和消费者2获取到的消息内容是不同的，也就是说同一个消息只能被一个消费者获取。
        - 【channel.basicQos(1);】增加如上代码，表示同一时刻服务器只会发送一条消息给消费者。可以实现能者多劳,即让效率高的去消费更高的信息
        - 效率高的消费者消费消息多。可以用来进行负载均衡。
    - 发布/订阅模式【一个消费者将消息首先发送到交换器，交换器绑定到多个队列，然后被监听该队列的消费者所接收并消费。】
        - 交换器，在RabbitMQ中，交换器主要有四种类型:direct、fanout、topic、headers
        - 此模式用到的交换机是->fanout
        - (就是一条消息发送到一个绑定着两个不同的队列的交换机上,交换机会把消息发送到这两个队列上)消费1和消费者2都消费了该消息,这是因为消费者1和消费者2都监听了被同一个交换器绑定的队列。如果消息发送到没有队列绑定的交换器时，消息将丢失，因为交换器没有存储消息的能力，消息只能存储在队列中。
        - 应用场景:比如一个商城系统需要在管理员上传商品新的图片时，前台系统必须更新图片，日志系统必须记录相应的日志，那么就可以将两个队列绑定到图片上传交换器上，一个用于前台系统更新图片，另一个用于日志系统记录日志。
    - 路由模式【生产者将消息发送到direct交换器，在绑定队列和交换器的时候有一个路由key，生产者发送的消息会指定一个路由key，那么消息只会发送到相应key相同的队列，接着监听该队列的消费者消费消息。】
        - 此模式用到的交换机是->direct
        - 应用场景:利用消费者能够有选择性的接收消息的特性，比如我们商城系统的后台管理系统对于商品进行修改、删除、新增操作都需要更新前台系统的界面展示，而查询操作确不需要，那么这两个队列分开接收消息就比较好。
    - 主题模式【上面的路由模式是根据路由key进行完整的匹配（完全相等才发送消息），这里的通配符模式通俗的来讲就是模糊匹配。符号"#"表示匹配一个或多个词，符号"*"表示匹配一个词。】
        - 此模式用到的交换机是->topic
        - 生产者发送消息绑定的路由key为update.Name；消费者1监听的队列和交换器绑定路由key为update.#；消费者2监听的队列和交换器绑定路由key为select.#。很显然，消费者1会接收到消息，而消费者2接收不到。
    - 其他说明
        - 但是实际上只有三种，第一种简单队列，第二种工作模式，剩下的三种都是和交换器绑定的合起来称为一种，这小节我们就来详细介绍交换器。
        - 前面三种分别对应路由模式、发布订阅模式和通配符模式，headers 交换器允许匹配 AMQP 消息的 header 而非路由键，除此之外，header 交换器和 direct 交换器完全一致，但是性能却差很多，因此基本上不会用到该交换器
        
[生产者与消费者](https://images2018.cnblogs.com/blog/1120165/201807/1120165-20180718210739808-323080287.png)
    
    
    
    
14. 扩展知识
    - @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)这个是说在每次注入的时候回自动创建一个新的bean实例
      @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)单例模式，在整个应用中只能创建一个实例
      @Scope(value=WebApplicationContext.SCOPE_GLOBAL_SESSION)全局session中的一般不常用
      @Scope(value=WebApplicationContext.SCOPE_APPLICATION)在一个web应用中只创建一个实例
      @Scope(value=WebApplicationContext.SCOPE_REQUEST)在一个请求中创建一个实例
      @Scope(value=WebApplicationContext.SCOPE_SESSION)每次创建一个会话中创建一个实例
        - 这个是只有在用户访问系统时才会创建,也就是说,当Spring启动时容器中是没有的,这时候另一个类依赖此bean是不可以的,这是可以指定以下属性进行代理注入,带当前类是接口是可使用INTERFACES创建一个JDK代理模式,这是一个类时，可使用TARGET_CLASS基于类的代理模式
        - 里面还有个属性
            proxyMode=ScopedProxyMode.INTERFACES创建一个JDK代理模式
            proxyMode=ScopedProxyMode.TARGET_CLASS基于类的代理模式
            proxyMode=ScopedProxyMode.NO（默认）不进行代理

15. [延迟队列的实现](https://github.com/mylxsw/growing-up/blob/master/doc/RabbitMQ%E5%8F%91%E5%B8%83%E8%AE%A2%E9%98%85%E5%AE%9E%E6%88%98-%E5%AE%9E%E7%8E%B0%E5%BB%B6%E6%97%B6%E9%87%8D%E8%AF%95%E9%98%9F%E5%88%97.md)

16. 定义队列时参数说明

| 参数	|值	|说明
|--- |  --- |  --- |
| queue |	-	|队列名称|
| passive|	false|	队列不存在则创建，存在则直接成功|
| durable|	true|	队列持久化|
| exclusive|	false|	排他，指定该选项为true则队列只对当前连接有效，连接断开后自动删除|
| no-wait|	false|	该方法需要应答确认|
| auto-delete|	false	|当不再使用时，是否自动删除|
    
    
    
    
17. 配置模板暂存
```
    server.port=6006
    spring.application.name=springboot_rabbitmq
    #rabbitmq config
    #spring.rabbitmq.addresses=单机，集群多个地址以,号隔开
    spring.rabbitmq.host=localhost
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=tanjie
    spring.rabbitmq.password=tanjie666
    spring.rabbitmq.virtual-host=/
    #开启rabbitmq的confirm机制,如果消息没有到达exchange,或者exchange在ack生产者的时候，生产者没有收到,那么生产者会进行重发
    #如果设置为false,经过测试，不会进行回调
    spring.rabbitmq.publisher-confirms=true
    #开启rabbitmq的生产端{template}重试机制,默认是false,默认重试3次
    spring.rabbitmq.template.retry.enabled=true
    #关闭消息的强制路由，当生产者将消息发到exchange，如果没有queue进行绑定, 禁止broker发送basic.return，表示当前消息无人消费
    #因为我们配置了消息的持久性，就算没有消费者，消息也在磁盘，默认就是false
    spring.rabbitmq.template.mandatory=false
    #开启rabbitmq的消费者{listener}重试机制,该重试机制需要设置为自动ack,本次方案和PHP保持一致，如果消费者消费失败后，手动将消息放入死信队列等待消息被重新消费
    # 默认该配置为false,设置为true的意思是，如果消费者消费失败了，rabbitmq server会自动重试3次
    #spring.rabbitmq.listener.simple.retry.enabled=true
    #消费端采用手动应答
    spring.rabbitmq.listener.simple.acknowledge-mode=manual
    #默认缓存模式是channel,在springboot里面,比如在框架rabbitmqTemplate中使用的通道将会可靠地返回到缓存中
    #spring.rabbitmq.cache.connection.mode=channel
    #设置默认通道缓存的大小
    #spring.rabbitmq.cache.channel.size=10
    #配置生产者的配置，包括exchange,routingkey等
    java.rabbitmq.send.service.exchange=scm3.materials
    java.rabbitmq.send.service.rountkey=direct_rounting_key
    #配置supply监听信息
    java.rabbitmq.consumer.service.retry.exchange=scm3.materials.retry
    java.rabbitmq.consumer.service.fail.exchange=scm3.materials.fail
    java.rabbitmq.consumer.service.supply.retry.routingkey=material@supply
    #配置user监听信息
    java.rabbitmq.consumer.service.user.retry.routingkey=material@user
```
    