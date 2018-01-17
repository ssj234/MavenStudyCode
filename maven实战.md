# Maven的安装和配置

## 安装

Maven依赖与Java，需要设置JAVA_HOME的环境变量，maven程序下载，解压后配置环境变量即可。  
安装Maven，在命令行输入`mvn -v` 如果看到打印出的版本信息，说明安装正确。

## ～/.m2 文件夹

默认情况下，.m2文件夹放置了Maven的本地仓库，保存了需要的jar包，插件等；在m2下可以拷贝一个setting.xml文件，修改相关的配置。  
比如：proxy和mirror信息
```
	<mirror> <!--设置阿里云的仓库地址-->
        <id>nexus-aliyun</id>
        <mirrorOf>*</mirrorOf>
        <name>Nexus aliyun</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
    </mirror> 
```

# 使用入门

## 实例

1. 编写pom.xml文件，是Project Object Model的意思。
```
<project>
    <modelVerson>4.0.0</modelVersion>
    <groupId>com.shisj.mvnstudy</groupId>
    <artifactId>hello-world</artifactId>
    <version>1.0-SNAPSHOT</version>
    <name>Maven Hello World Project</name>
</project>
```
* groupId:定义了项目属于哪个组，com.mycom.appname
* artifactId：当前maven项目在组中的唯一ID
* version：版本号
* name：项目名称，描述，非必须  。
>  

2. 编写代码和测试用例 
maven规定，java代码放在src/main/java目录下，Maven会自动搜下找到主代码；测试代码在src/test/java下面；在pom.xml里面添加依赖
```
<dependencies>
    	<dependency>
    		<groupId>junit</groupId>
    		<artifactId>junit</artifactId>
    		<groupId>4.7</groupId>
    		<scope>test</scope>
    	</dependency>
    </dependencies>
```
× mvn clean compile 编译
× mvn clean test 运行测试用例
× mvn clean package 打包成jar包，在target目录下target/hello-world-1.0-SNAPSHOT.jar
× mvn clean install 安装jar包到本地仓库

我们可以看到，jar包不能直接运行，需要
```
java -classpath hello-world-1.0-SNAPSHOT.jar com.shisj.mvnstudy.helloworld.He
lloWorld
```
maven提供了打包成可执行jar的插件，需要在pom.xml中进行配置
```
<build><plugins>
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-shade-plugin</artifactId>
	<version>1.2.1</version>
	<executions>
		<execution>
			<phase>package</phase>
			<goals>
				<goal>shade</goal>
			</goals>
			<configuration>
				<transformers>
					<transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
						<mainClass>com.shisj.mvnstudy.helloworld.HelloWorld</mainClass>
					</transformer>
				</transformers>
			</configuration>
		</execution>
	</executions>
</plugin></plugins></build>
```

打包源码的插件
```
<build><plugins>
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-source-plugin</artifactId>
	<version>2.1.1</version>
	<executions>
		<execution>
			<id>attach-sources</id>
			<phase>verify</phase>
			<goals>
				<goal>jar-no-fork</goal>
			</goals>
		</execution>
	</executions>
</plugin></plugins></build>
```

## Archetype

Maven提供了Archetype来帮助我们快速勾勒出项目骨架
> mvn archetype:generate


## 坐标系统

* groupId： 当前Maven项目隶属的实际项目。
* artifactId： 定义实际项目中的一个Maven模块。
* version： 当前模块的版本号
* packaging： 打包方式，默认是jar，可以设置为war
* classifier：定义构建输出的一些附属构件，如javadoc.jar sources.jar等

## 依赖范围

* compile 编译依赖范围，默认的范围，对编译/测试/运行都有效
* test 测试依赖范围，只对测试classpath有效
* provided 已提供依赖范围，对编译和测试classpath有效，运行时无效，如servlet-api
* runtime 运行是依赖范围，如mysql的jar包
* system 系统依赖范围. 谨慎使用


**传递性依赖 **
```
<dependency>
      <groupId>commons-logging</groupId>
      <artifactId>commons-logging</artifactId>
      <version>1.2</version>
      <scope>compile</scope>
    </dependency>
```
sh-prod 依赖spring-core，spring-core依赖common-logging，sh-prod依赖core时会依赖common-logging

**可选依赖 **

```
<dependency>
      <groupId>commons-codec</groupId>
      <artifactId>commons-codec</artifactId>
      <version>1.10</version>
      <scope>compile</scope>
      <optional>true</optional>
    </dependency>
```
sh-prod 依赖spring-core，spring-core依赖commons-codec,但是可选依赖，sh-prod依赖core时不会依赖common-logging

**排除依赖 **

sh-prod 依赖spring-core，spring-core依赖common-logging，sh-prod依赖core时想使用1.3版本的common-logging，可以在引入spring-core是排除common-logging
```
<dependency>
    	<groupId>org.springframework</groupId>
    	<artifactId>spring-core</artifactId>
    	<version>${spring.version}</version>
    	<exclusions>
	    	<exclusion>
		    	<groupId>commons-logging</groupId>
			      <artifactId>commons-logging</artifactId>
	    	</exclusion>
    	</exclusions>
    </dependency>
```
** 优化依赖**

> mvn dependency:list
> mvn dependency:tree
> mvn dependency:analyze



## 聚合与继承

书中的例子有如下几个项目：
* account-parent  所有项目都继承自这个项目，这个项目的pom.xml中有如下设置

```
<!--1.配置如下模块，执行mvn命令时，会reactor后分别对每个模块执行命令-->
 <modules>
  	<module>../account-email</module>
  	<module>../account-persist</module>
  	<module>../account-captcha</module>
  </modules>

<!-- 2.设置父依赖 -->
<dependencyManagement> 
  	<dependencies>
  		<!-- spring -->
  		<dependency>
	    	<groupId>org.springframework</groupId>
	    	<artifactId>spring-core</artifactId>
	    	<version>${springframework.version}</version>
	    </dependency>
	   ...
	<dependencies>
<dependencyManagement>

<!--3.设置依赖插件-->
<build>
  	<pluginManagement>
  		<plugins>
  			<plugin>
  				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-source-plugin</artifactId>
				<version>2.1.1</version>
  			</plugin>
  		</plugins>
  	</pluginManagement>
  </build> 
```

* account-email/account-captcha/account-persist 继承自account-parent

```
<!-- 继承 account-parent-->
 <parent>
  	<groupId>com.shisj.mvnstudy.account</groupId>
  	<artifactId>account-parent</artifactId>
  	<version>1.0.1-SNAPSHOT</version>
  	<relativePath>../account-parent/pom.xml</relativePath>
  </parent>
```

* account-service继承自account-parent，依赖与account-email/account-captcha/account-persist 

```
<parent> <!--继承-->
  	<groupId>com.shisj.mvnstudy.account</groupId>
  	<artifactId>account-parent</artifactId>
  	<version>1.0.1-SNAPSHOT</version>
  	<relativePath>../account-parent/pom.xml</relativePath>
  </parent>
<dependencies> <!--一依赖三个模块，由于是一个项目，groupId和version相同，使用变量表示-->
    <dependency>
      <groupId>${project.groupId}</groupId>
      <artifactId>account-email</artifactId>
      <version>${project.version}</version>
    </dependency>
    <dependency>
      <groupId>${project.groupId}</groupId>
      <artifactId>account-persist</artifactId>
      <version>${project.version}</version>
    </dependency>
    <dependency>
      <groupId>${project.groupId}</groupId>
      <artifactId>account-captcha</artifactId>
      <version>${project.version}</version>
    </dependency>
  </dependencies>
```

* account-web 继承自account-parent，依赖account-service，最后使用这个项目达成的war包进行测试


##  jetty测试

pom.xml中加入插件
```
<build>
    <finalName>account</finalName> <!-- account.war -->
    <plugins>
		<plugin>
			<groupId>org.mortbay.jetty</groupId>
			<artifactId>jetty-maven-plugin</artifactId>
			<version>7.1.6.v20100715</version>
			<configuration>
				<scanIntervalSeconds>10</scanIntervalSeconds>
				<webAppConfig>
					<contextPath>/test</contextPath>
				</webAppConfig>
			</configuration>
		</plugin>
	</plugins>
  </build>
```
setting.xml中加入
```
<pluginGroups>
    <pluginGroup>org.mortbay.jetty</pluginGroup>
  </pluginGroups>
```
在命令行运行`mvn jetty:run -Djetty.port=9999`，之后在浏览器中http://localhost:9999/test/中可以进行测试


## Cargo自动化部署

**部署到本地**

1.standalone模式，会将程序部署到target下，setting加入cargo后，运行mvn cargo:run启动tomcat
```
<plugins>
    	<plugin>
    		<groupId>org.codehaus.cargo</groupId>
			<artifactId>cargo-maven2-plugin</artifactId>
			<version>1.4.9</version>
			<configuration>
				<container>
					<containerId>tomcat7x</containerId>
					<home>/home/shisj/software/tomcat</home>
				</container>
				<configuration>
					<type>standalone</type>
					<home>${project.build.directory}/tomcat7x</home>
				</configuration>
			</configuration>
    	</plugin>
```

2. 部署到现有的web容器下,existing模式，会讲war包部署到tomcat的webapps下面，运行mvn clean package cargo:run可以启动tomcat
```
<plugin>
    		<groupId>org.codehaus.cargo</groupId>
			<artifactId>cargo-maven2-plugin</artifactId>
			<version>1.4.9</version>
			<configuration>
				<container>
					<containerId>tomcat7x</containerId>
					<home>/home/shisj/software/tomcat</home>
				</container>
				<configuration>
					<type>existing</type>
					<home>/home/shisj/software/tomcat</home>
				</configuration>
			</configuration>
    	</plugin>
```

** 部署到远程服务**

修改插件的配置为remote，用户名和密码在tomcat-user.xml文件中配置
```
<role rolename="manager"/><role rolename="manager-script"/><role rolename="manager-gui"/><role rolename="manager-jmx"/><role rolename="manager-status"/><user username="admin" password="admin123" roles="manager,manager-gui,manager-script,manager-jmx,manager-status"/>
```
pom.xml中配置cargo，之后运行 mvn cargo:redeploy即可发布到tomcat中
```
<plugin>
    		<groupId>org.codehaus.cargo</groupId>
			<artifactId>cargo-maven2-plugin</artifactId>
			<version>1.4.9</version>
			<configuration>
				<container>
					<containerId>tomcat7x</containerId>
					<type>remote</type>
				</container>
				<configuration>
					<type>runtime</type>
					<properties>
						<cargo.remote.username>admin</cargo.remote.username>
						<cargo.remote.password>admin123</cargo.remote.password>
						<cargo.tomcat.manager.url>http://localhost:8080/manager</cargo.tomcat.manager.url>
					</properties>
				</configuration>
			</configuration>
    	</plugin>
```

## 命令

```
mvn help:system  打印出所有的Java系统属性和环境变量
mvn clean
mvn compile
mvn test
mvn package
mvn instal
mvn deploy
```

## 将依赖的jar包拷贝到指定路径


```
mvn dependency:copy-dependencies -DoutputDirectory=src/main/webapp/WEB-INF/lib  -DincludeScope=runtime  
```