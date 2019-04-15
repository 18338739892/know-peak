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
    - Direct Exchange:直接匹配,通过Exchange名称+RountingKey来发送与接收消息.
    - Fanout Exchange:广播订阅,向所有的消费者发布消息,但是只有消费者将队列绑定到该路由器才能收到消息,忽略Routing Key
    - Topic Exchange：主题匹配订阅,这里的主题指的是RoutingKey,RoutingKey可以采用通配符,如:*或#，RoutingKey命名采用.来分隔多个词,只有消息这将队列绑定到该路由器且指定RoutingKey符合匹配规则时才能收到消息