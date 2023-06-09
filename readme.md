# 项目介绍

​    基于游戏(盛世芳华)和微信公众号(小屋写随笔)做了该项目(已线上运行一年半)

​    该项目将游戏里的部分数据通过微信公众号展示了出来,玩家可以通过发送文字、语音、图片来获取游戏里的处理后数据以及攻略。截至20230315,玩家互动次数已超过160万次·。

​    项目最初版本为传统mvc架构,本次改成了DDD领域驱动模型,springboot版本为2.4.0。

# 环境依赖

​    Java环境、maven、mysql、redis

# 目录结构

![DDD领域驱动设计代码结构](https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/DDD01.jpg)

# 使用说明

#####     线上体验

​    可直接前往公众号**小屋写随笔**体验。

#####     本地体验

​    resources下面有sql脚本;在application.yml中配置好mysql和redis即可启动项目。部分功能会用到百度OCR、腾讯云短信和图灵机器人,需要在数据库中添加对应配置。

​    项目启动后可参考[微信公众号官方文档接收普通消息的能力说明](https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html),本地请求url:localhost:8088/xwxsb/weChat/msg,请求参数实例:

```xml
                                <xml>
                                  <ToUserName><![CDATA[toUser]]></ToUserName>
                                  <FromUserName><![CDATA[fromUser]]></FromUserName>
                                  <CreateTime>1348831860</CreateTime>
                                  <MsgType><![CDATA[text]]></MsgType>
                                  <Content><![CDATA[this is a test]]></Content>
                                  <MsgId>1234567890123456</MsgId>
                                  <MsgDataId>xxxx</MsgDataId>
                                  <Idx>xxxx</Idx>
                                </xml>
```

# 版本更新

##### 1.0.1

​    新增与chatgpt对话功能,前后语境已经联系了起来;依赖GPT-3.5-Turbo;参考文章 https://github.com/Grt1228/chatgpt-java

​    使用方法：在chatgpt后面输入你的内容即可。

<center>
<figure>
<img src="https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/chatgpt001.jpg" /><img src="https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/chatgpt002.jpg" />
</figure>
</center>

##### 1.0.2

代码业务逻辑结构优化,使用工厂模式和责任链模式来处理用户消息。

![https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/CodeBusinessStructure.png](https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/CodeBusinessStructure.png)

##### 1.0.3

对简单关键字处理器进行了拆分:依赖于游戏内容的那一部分拆分到了复杂关键字处理器中

![](https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/%E5%BE%AE%E4%BF%A1%E5%85%AC%E4%BC%97%E5%8F%B7%E9%A1%B9%E7%9B%AE%E5%A4%84%E7%90%86%E5%99%A8%E6%B5%81%E7%A8%8B.png)

构造了系统页面(http://localhost:8088/xwxsb/ssfh/login), 管理员账号(带有类型信息)登录系统可以看到该类型账号的数据,进行维护后,可同步至数据库,然后用户使用与该管理员账号类型关联的微信公众号发消息时就可以得到维护后的内容。

系统可以很方便的添加管理员账号和微信公众号的绑定;管理员账号类型对应的处理器信息是存放在数据库中,可以动态地修改账号和微信公众号的功能权限。

![https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/%E5%BE%AE%E4%BF%A1%E5%85%AC%E4%BC%97%E5%8F%B7%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84%E5%9B%BE.png](https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/%E5%BE%AE%E4%BF%A1%E5%85%AC%E4%BC%97%E5%8F%B7%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84%E5%9B%BE.png)

##### 1.0.4

新增chatgpt聊天界面,地址:http://110.40.208.47:8088/xwxsb/ssfh/chat 使用netty进行网络通信;处理器代码优化。

![](https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/chatgptWeb.png)
