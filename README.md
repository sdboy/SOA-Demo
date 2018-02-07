[TOC]
# 1.理解SOA
## 1.1 SOA介绍
### 1.1.1 SOA定义
#### 1.1.1.1 应用角度定义:
```
从应用的角度定义:SOA是一种应用框架,它着眼于日常的业务应用,并将它们划分为单独的业务功能和流程以及所谓的服务;
```
#### 1.1.1.2 软件原理定义:
```
  SOA是一个组件模型,它将应用程序的不同功能单元(服务)通过这些服务之间定义良好的接口和契约联系起来;
```
### 1.1.2 SOA架构划分:
 - 基于SOA的解决方案,SOA架构可分为五层:
 - [x] 用户界面层 ---- 这些GUI的最终用户或应用程序访问的应用程序/服务接口;
 - [x] 业务流程层 ---- 在应用方面的业务用例服务;
 - [x] 服务层 ---- 服务合并在一起，提供统一的实时服务;
 - [x] 服务组件层 ---- 用来建造服务的组件，如功能库、技术库、技术接口等;
 - [x] 操作系统 ---- 这层包含数据模型，企业数据仓库，技术平台等;
 ```
   基于Dubbo的RPC是对SOA的技术实现;
 ```
 
## 1.2 SOA结构图
![](http://chuantu.biz/t6/223/1517909751x-1404755486.jpg)

- 应用程序前端: 业务流程的所有者,应用层: APP,WEB等;
- 服务库: 管理多个SOA服务(Dubbo的Provider和Consumer)
- 服务总线:SOA服务的调度(Dubbo的Register和Container)
- 合约:服务实现功能的协议和约束;
- 实现:服务业务的基础结构,包括业务逻辑和数据逻辑,对接口功能的实现;
- 接口:对外公开的功能;
```
举个栗子: 咱们平台服务包括: 订单服务,商品服务等等;
Dubbo+SOA的体现：
1) Dubbo框架将这些服务需要进行注册,进行统一的暴露和监控;
2)在这个大项目中,每一个子项目（子服务）都是SOA模型;
3)由应用程序前端的服务进行消费;
以订单服务为例,订单提供下单接口,参数和实现;服务在启动的时候,Dubbo框架会加载服务并注册至注册中心，并将接口暴露给消费者; 
```
# 2. Dubbo介绍
## 2.1 Dubbo入门:
### 2.1.1 Dubbo 是什么
``` 
  先给大家描述一个大致的情景：
      集群是在不同的机器上部署同一个项目,用来"负载均衡"以及"失效备援",那么它们到底是如何或者说通过什么来实现上述两点的?
      分布在各个机器上的项目的地址是怎么分配的呢?是通过谁来分配的?
  Dubbo在分布式系统中是做什么用的?
      Java下RPC[Remote Procedure Call]框架作用就是统一管理配置各个系统服务间的调用.
      原理就是:A系统调用B系统接口服务,它在背后怎么把这个流程 动态化(凭借zookeeper通知),权限化,配置化,低耦合化,自动化.
```
### 2.1.2 为什么要用Dubbo
  **服务越来越多造成的问题:** 
-  当服务越来越多时,服务的URL地址信息就会极速增长,配置管理变得非常困难,F5硬件负载均衡器的单点压力也越来越大.
- 当进一步发展,服务间依赖关系变得错踪复杂,甚至分不清哪个应用要在哪个应用之前启动，架构师都不能完整的描述应用的架构关系.
-  接着，服务的调用量越来越大，服务的容量问题就暴露出来，这个服务需要多少机器支撑？什么时候该加机器？</br>
  **Dubbo解决问题的手段:**
- 服务注册中心,动态的注册和发现服务,使用服务透明,避免了大量的URL;
- 消费方通过服务方提供的地址列表,实现负载均衡和FailOver;
- 统计服务的调用量,响应时间,会做统计作为权重调整,机器数量规划的参考指标;

# 3.SpringBoot整合Dubbo
## 3.1 项目改造
### 3.1.1 模块拆分
- **模块拆分** : 根据HMT项目的先例,我们把项目尝试拆分为:

| 模块ID      |    模块名| 作用         |  模块类型| 模块依赖     |
| :--------   | --------:| :--:         | :--:     |   :--:       |
| 1           |    root  |  项目根      |   pom    |   2,3,4,5,6  |
| 2           |    bean  |  实体类      |   jar    |    NULL      |
| 3           |    dao   |  mapper      |   jar    |    2         |
| 4           |  dubbo   |  service接口 |   jar    |    2         |
| 5           |  service |  service实现 |   jar    |   2,3,4      |
| 6           |  web     |  API         |   war    |   2,4        |
### 3.1.2 项目拆分图
- **实际拆分图** :</br>
![](http://chuantu.biz/t6/222/1517885738x-1404755564.jpg)
### 3.1.3 项目模板地址
地址: [SHOPIN-SOA](https://github.com/553899811/SHOPIN-SOA)
## 3.2.配置信息
### 3.2.1 各模块pom依赖分析
 - 模板项目只是根据最简单
#### 3.2.1.1 SHOPIN-SOA(root)
- 主要作用就是对项目的子模块以及具体jar的控制;
```java
   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>net.shopin</groupId>
    <artifactId>shopin-soa</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--子模块-->
    <modules>
        <module>shopin-bean</module>
        <module>shopin-dao</module>
        <module>shopin-dubbo</module>
        <module>shopin-service</module>
        <module>shopin-web</module>
    </modules>

    <!--模块类型-->
    <packaging>pom</packaging>
    <name>shopin-soa</name>

    <!--springboot主版本号-->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.8.RELEASE</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>1.8</java.version>
        <dubbo.version>2.5.7</dubbo.version>
        <zkclient.version>0.10</zkclient.version>
        <lombok.version>1.16.18</lombok.version>
        <spring.boot.version>1.5.7.RELEASE</spring.boot.version>
        <project.version>1.0-SNAPSHOT</project.version>
        <zookeeper.version>3.4.10</zookeeper.version>
        <spring.boot.starter.dubbo.version>1.0.0</spring.boot.starter.dubbo.version>
        <fastjson.version>1.2.44</fastjson.version>
        <mybatis.spring.boot.starter.version>1.2.2</mybatis.spring.boot.starter.version>
        <mysql.connector.java.version>6.0.6</mysql.connector.java.version>
        <junit.version>4.12</junit.version>
    </properties>

    <!--dependencyManagement让子模块可以继承此pom中的版本号-->
    <dependencyManagement>
        <dependencies>
            <!--引入bean模块-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>shopin-bean</artifactId>
                <version>${project.version}</version>
            </dependency>
            <!--引入dao模块-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>shopin-dao</artifactId>
                <version>${project.version}</version>
            </dependency>
            <!--引入dubbo接口模块-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>shopin-dubbo</artifactId>
                <version>${project.version}</version>
            </dependency>
            <!--引入service实现模块-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>shopin-service</artifactId>
                <version>${project.version}</version>
            </dependency>
            <!--引入web-api模块-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>shopin-web</artifactId>
                <version>${project.version}</version>
            </dependency>
            <!--引入fastjson-->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>${fastjson.version}</version>
            </dependency>
            <!-- Springboot依赖 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter</artifactId>
                <version>${spring.boot.version}</version>
            </dependency>
            <!-- Springboot-web依赖 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <version>${spring.boot.version}</version>
            </dependency>
            <!--mybatis 与SpingBoot整合-->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${mybatis.spring.boot.starter.version}</version>
            </dependency>
            <!--让web项目可以部署到外部tomcat上-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
                <version>${spring.boot.version}</version>
             
            </dependency>
            <!--Springboot dubbo 综合配置包 -->
            <dependency>
                <groupId>io.dubbo.springboot</groupId>
                <artifactId>spring-boot-starter-dubbo</artifactId>
                <version>${spring.boot.starter.dubbo.version}</version>
            </dependency>
            <!--定义dubbo版本号-->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>dubbo</artifactId>
                <version>${dubbo.version}</version>
            </dependency>

            <!--zookeeper 版本号-->
            <dependency>
                <groupId>org.apache.zookeeper</groupId>
                <artifactId>zookeeper</artifactId>
                <version>${zookeeper.version}</version>
                <type>pom</type>
            </dependency>

            <!-- 使用lombok实现JavaBean的get、set、toString、hashCode、equals等方法的自动生成  -->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
                <scope>provided</scope>
            </dependency>

            <!--第三方ZK客户端-->
            <dependency>
                <groupId>com.101tec</groupId>
                <artifactId>zkclient</artifactId>
                <version>${zkclient.version}</version>
            </dependency>

            <!--引入Mysql-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.connector.java.version}</version>
                <scope>runtime</scope>
            </dependency>

            <!--引入测试-->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```
#### 3.2.1.2 shopin-bean
 - 对项目中实体类控制 
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>shopin-soa</artifactId>
        <groupId>net.shopin</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>shopin-bean</artifactId>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
   <!-- 使用lombok实现JavaBean的get、set、toString、hashCode、equals等方法的自动生成  -->
    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```
#### 3.2.1.3 shopin-dao
 - 对mapper接口的控制
```java
   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>shopin-soa</artifactId>
        <groupId>net.shopin</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>shopin-dao</artifactId>
    <packaging>jar</packaging>

    <name>shopin-dao</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>

        <dependency>
            <groupId>net.shopin</groupId>
            <artifactId>shopin-bean</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
    <!--重点:让mapper.xml 能够被扫描编译 -->
    <build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>
    </build>
</project>
```
#### 3.2.1.4 shopin-dubbo
 - 对服务接口的控制
```java
   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>shopin-soa</artifactId>
        <groupId>net.shopin</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>shopin-dubbo</artifactId>
    <packaging>jar</packaging>

    <name>shopin-dubbo</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>net.shopin</groupId>
            <artifactId>shopin-bean</artifactId>
        </dependency>
    </dependencies>
</project>

```
#### 3.2.1.5 shopin-service
 - 对服务实现的控制
```java
   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <parent>
        <artifactId>shopin-soa</artifactId>
        <groupId>net.shopin</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>
    <packaging>jar</packaging>
    <artifactId>shopin-service</artifactId>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    </properties>

    <dependencies>
        <dependency>
            <groupId>net.shopin</groupId>
            <artifactId>shopin-bean</artifactId>
        </dependency>
        <dependency>
            <groupId>net.shopin</groupId>
            <artifactId>shopin-dao</artifactId>
        </dependency>
        <dependency>
            <groupId>net.shopin</groupId>
            <artifactId>shopin-dubbo</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <type>pom</type>
        </dependency>

        <dependency>
            <groupId>com.101tec</groupId>
            <artifactId>zkclient</artifactId>
        </dependency>

        <dependency>
            <groupId>io.dubbo.springboot</groupId>
            <artifactId>spring-boot-starter-dubbo</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</project>
```
#### 3.2.1.6 shopin-web
```java
   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <parent>
        <artifactId>shopin-soa</artifactId>
        <groupId>net.shopin</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>
    <artifactId>shopin-web</artifactId>
    <packaging>war</packaging>
    <name>shopin-web</name>

    <dependencies>

        <dependency>
            <artifactId>shopin-dubbo</artifactId>
            <groupId>net.shopin</groupId>
        </dependency>
        <dependency>
            <artifactId>shopin-bean</artifactId>
            <groupId>net.shopin</groupId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>io.dubbo.springboot</groupId>
            <artifactId>spring-boot-starter-dubbo</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>netty</artifactId>
                    <groupId>io.netty</groupId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <type>pom</type>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>compile</scope>
        </dependency>

    </dependencies>
    <build>
        <finalName>shopin-web</finalName>
    </build>
</project>
```
### 3.2.2 通信配置
#### 3.2.2.1 消息提供者
 - shopin-service 模块在整个项目中充当着提供具体服务实现的功能,所以我们称之为消息提供者;
 ##### 3.2.2.1.1 application.properties配置
 - 在搭建项目过程中我们选择以下的jar去集成dubbo和springboot:
```
   <dependency>
    <groupId>io.dubbo.springboot</groupId>
    <artifactId>spring-boot-starter-dubbo</artifactId>
   </dependency>
```
 - 只有在pom中引入上述jar,在application.properties中可以引入有关于spring.dubbo.xxx这些配置信息;
```
#配置server端口
server.port=8080
######################################################
# database配置源
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/springboot?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=123456


#######################################################
### 引入SpringBoot-Dubbo
#######################################################

### 表明此应用名--服务提供者角色;
spring.dubbo.application.name=shopin-service-provider

### 注册中心地址,我们选择zookeeper集群去管理,去实现负载均衡等作用;
spring.dubbo.registry.address=zookeeper://192.168.112.131:2181?backup=192.168.112.129:2181,192.168.112.130:2181
### 通信协议
spring.dubbo.protocol.name=dubbo
### 通信使用端口号
spring.dubbo.protocol.port=20880
```
##### 3.2.2.1.2 启动类配置
 - 关键点在于 @DubboComponentScan 这个注解(dubbo-2.5.7版本开始有了[注解驱动](https://github.com/mercyblitz/blogs/blob/master/java/dubbo/Dubbo-New-Programming-Model.md)),有了它,就能扫描@Service[Dubbo注解]和@Reference这两个注解了;
```
package net.shopin;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author zhangyong@shopin.cn
 * @date 2018/2/5 11:01
 */
@SpringBootApplication
@DubboComponentScan("net.shopin.service.impl")
@MapperScan(basePackages = "net.shopin.mapper")
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
```
#### 3.2.2.2 消息消费者
##### 3.2.2.2.1 application.properties配置
 - shopin-web 模块在项目中充当着消息消费者的角色,所有的service通过@Reference 这个注解,渗透到不同的controller层实现服务的跨度调度;
- 在搭建项目过程中我们选择以下的jar去集成dubbo和springboot:
```
   <dependency>
    <groupId>io.dubbo.springboot</groupId>
    <artifactId>spring-boot-starter-dubbo</artifactId>
   </dependency>
```
 - 只有在pom中引入上述jar,在application.properties中可以引入有关于spring.dubbo.xxx这些配置信息;
```
server.port=80
server.context-path=/
#######################################################
# database配置源
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/springboot?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

########################################################
# Springboot-dubbo 消费者配置信息
spring.dubbo.application.name=shopin-service-consumer
spring.dubbo.registry.address=zookeeper://192.168.112.131:2181?backup=192.168.112.129:2181,192.168.112.130:2181
#spring.dubbo.registry.address=zookeeper://192.168.59.131:2181?backup=192.168.59.129:2181,192.168.59.133:2181
spring.dubbo.protocol.name=dubbo
spring.dubbo.protocol.port=20880
```
##### 3.2.2.2.2 启动类配置
 - @DubboComponentScan 扫描controller中的@Referencr注解
```
package net.shopin;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangyong@shopin.cn
 * @date 2018/2/5 12:53
 */
@SpringBootApplication
@DubboComponentScan(basePackages = {"net.shopin.controller"})
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
```
