# ![icon](https://2okr.com/assets/images/logo@2x.png) 
## [2OKR](https://2okr.com/)
   2OKR是一款小巧而强大的OKR管理工具，旨在帮助企业对优先的目标进行聚焦，提高企业管理效率。目前已经提供了web和微信小程序的客户端，方便使用。
   
## 设计和演示
   -  [演示](https://2okr.com/)
   - [设计原型](https://projects.invisionapp.com/d/main#/projects/prototypes/17063968)

## 功能特性
   - 结构化的目标设定
   > 从组织，团队，个人自上而下结构化的设定目标，让每个人的目标都紧密的联系在一起。目标的紧密相连将个人的贡献和组织的效率联系到一起，加深员工的主人翁精神，促进个人的参与和创新
   
   - 有聚焦的周计划
   > 员工围绕目标开展工作，每周的工作计划都和目标关联到一起，让每个人的工作都有条不紊。团队每个成员的工作计划汇聚到一起，形成目标和实践的对应.
   
   - 持续性的绩效管理
   > CFR(对话，反馈和认可)是持续性的绩效管理的部分。管理者定期与员工进行对话，评估员工的工作绩效，并予以反馈，对员工的绩效进行及时的认可和鼓励。CFR和OKR的结合让管理者、员工和组织提升全新的水平
   
## 环境依赖
   - nginx
   - jdk
   - maven
   - redis
   - rabbitmq


## 部署说明
 - 服务说明
 > 2OKR前后端分离，采用服务化方式部署，各个服务和依赖如下：
 
 |   服务        |     说明    |  依赖 |
 |   --------   |    --------   |   -----:    |
 |   [freamwork](https://code.shareworks.cn/coreteam/freamwork)     |     基础框架  |  |
 |   [sso](https://code.shareworks.cn/coreteam/2okr-sso)     |     sso认证中心  |freamwork| 
 |   [user-service](https://code.shareworks.cn/coreteam/2okr-user-service)     |     用户服务中心  |freamwork|   
 |   [msg](https://code.shareworks.cn/coreteam/msg)     |     消息通知服务 |freamwork|
 |   [2okr](https://code.shareworks.cn/coreteam/2okr)     |     2OKR API服务  | freamwork,sso,user-service,msg|
 |   [fe-okr](https://code.shareworks.cn/coreteam/fe-okr)     |  2OKR web UI  | |
 |   [fe-okr-mobile](https://code.shareworks.cn/coreteam/fe-okr-mobile)     |     2OKR 小程序UI  | |
 
 - 部署
 > 2OKR需要部署sso,user-service,msg,2okr四个API服务和fe-okr资源。api服务采用了springboot内嵌的web容器，在通过maven的 mvn package命令后得到对应的jar
 文件，通过nohup java -jar xxx.jar 文件完成部署。
 fe-okr 推荐使用nginx作为web容器,配置api访问的代理。
 
 
## 2OKR目录结构描述
 |   module        |     说明    |
 |   --------   |  -----:    |
 |   2okr-common     |     公共模块  | 
 |   2okr-dao        |     数据访问模块  |
 |   2okr-service    |      服务模块 |
 |   2okr-payment-gateway    |   支付网关 |
 |   2okr-web-api |    web API |
 |   2okr-wx-api  |    小程序API |

## Contributors
  - aimilin.yu
  - allen.luo
  - chico.zeng
  - candy.zhang
  - dean.zhu 
  - 蒋建勇
  - jianxiong.xie
  - lang.yang
  - 铄辉 
  - sean.deng
  - tao.li






  
 

 
