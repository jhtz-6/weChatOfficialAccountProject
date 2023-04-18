# 项目介绍

​    认识的一个女生喜欢玩某款游戏,基于该游戏(盛世芳华)和微信公众号(小屋写随笔)做了该项目(已线上运行一年半)

​    该项目将游戏里的部分数据通过微信公众号展示了出来,玩家可以通过发送文字、语音、图片来获取游戏里的处理后数据以及攻略。截至20230315,玩家互动次数已超过160万次·。

​    项目最初版本为传统mvc架构,本次改成了DDD领域驱动模型,springboot版本为2.4.0。

# 环境依赖

​    Java环境、maven、mysql、redis

# 目录结构

![DDD领域驱动设计代码结构](https://camo.githubusercontent.com/cf8db1dc38030cf016579b41bead75902c33c279034ad933c8d9f3f81fd3bb7d/68747470733a2f2f7777772e3532306d79662e636f6d3a383038392f4444442545392541322538362545352539462539462545392541392542312545352538412541382545382541452542452545382541452541312545342542422541332545372541302538312545372542422539332545362539452538342e706e67)

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

​    新增与chatgpt对话功能,但前后语境没有联系起来;依赖GPT-3.5-Turb;参考文章 https://github.com/Grt1228/chatgpt-java

​    使用方法：在chatgpt后面输入你的内容即可。

<center>
<figure>
<img src="https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/chatgpt001.jpg" />
·
·
·
<img src="https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/chatgpt002.jpg" />
</figure>
</center>

##### 1.0.2

代码业务逻辑结构优化,使用工厂模式和责任链模式来处理用户消息。

![https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/CodeBusinessStructure.png](https://raw.githubusercontent.com/jhtz-6/weChatOfficialAccountProject/master/src/main/resources/static/photo/CodeBusinessStructure.png)



