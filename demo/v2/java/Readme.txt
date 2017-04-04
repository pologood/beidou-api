************************************************中文*****************************************************
发布介绍:

此客户端库基于Apache CXF 2.2.9创建。目前仅支持百度API V2版本。所有的WebService代理类请参考java-docs目录下的
bean-api.tar.gz文档，本客户端库的使用请参考java-docs目录下的baidu-clientlib-api.tar.gz。源代码在src目录下。

使用示例请参考AccountServiceExamples.java。本客户端库旨在提供WebService调用封装以及代理类生成，并不提供客户端
业务逻辑。

使用过程中如果遇到任何问题，请联系apihelp@baidu.com并在标题注明《Baidu API Client V2使用过程问题求助》

注意事项:

1. 如何设置SOAP Header ?
	参见baidu-api.properties。您需要填写username、password和token。请您填写这些信息后再尝试使用此客户端库。

2. 如何处理SOAP Response Header 数据 ?
	请通过ResHeaderUtil来获取。参见examples中AccountServiceExamples.java。

3. 如何访问沙盒环境
	请参见Sandbox.txt。

4. 如何设置List类型的参数？
	List类型的属性是没有set方法的，通过调用get方法获得对List类型的属性的引用，再调用List的add方法即可。如果属性是null，
	get方法会初始化属性为ArrayList。
	参见examples中AccountServiceExamples.java。
		info.getExcludeIp().add("112.111.1.5");
        info.getExcludeIp().add("112.111.1.6");

5. 本客户端库的运行环境
	lib子目录下面有两个子目录，分别对应JDK 1.5(20+) & JDK 1.6(21+)。请选择您的开发环境对应的库。
	
6. 服务器证书
	由于原来的服务器证书到期，2011年3月22日我们更新了api服务器的安全证书。新证书是采用VeriSign公司的class3G5证书签发的。
	对于使用JDK 1.5的用户，请采用如下的命令导入根证书。根证书可从根目录下的VeriSign_G5.zip解压得到。
	%JAVA_HOME%/bin/keytool -import -trustcacerts -keystore "%JAVA_HOME%/jre/lib/security/cacerts" -file VeriSign_G5.crt -alias verisignclass3g5ca
	默认密码是changeit
	对于使用JDK 1.6的用户，升级到21+后即可。无需另行导入证书。

***********************************************English***********************************************************
Release Notes:

This client lib was generated under Apache CXF 2.2.9. Only baidu-api service V2 is supported by now. All WebService
 stub classes are ducumented in bean-api.tar.gz under folder java-docs. The java docs about this client lib is
 in baidu-clientlib-api.tar.gz under folder java-docs. The source codes are under folder src.

Please see AccountServiceExamples.java for sample usage.

Please contact apihelp@baidu.com with email title: "Baidu API Client V2 Help" if any questions.

KeyPoints:

1. How to set the SOAP Header?
	See baidu-api.properties. You will need to fill in your username, password & token before using this client lib.

2. How to deal with SOAP Response Header data?
	Please use ResHeaderUtil. see AccountServiceExamples.java

3. How to access SandBox?
	See Sandbox.txt
	We prepared both online environment address & sandbox environment address for you.

4. How to set properties with java.util.List type?
	Properties with java.util.List type do not have set method. But it will create a new ArrayList in get method
	if the property is null. So you can invoke the get method to obtain the reference of the List.
	see AccountServiceExamples.java
		info.getExcludeIp().add("112.111.1.5");
        info.getExcludeIp().add("112.111.1.6");

5. Running environment
	There are two sub folders under lib, corresponding to JDK 1.5(20+) & JDK 1.6(21+) respectively. Please choose the one you need.

6. Server CA cert
	As the old cert is overdue, we updated the server CA cert. For those clients using JDK 1.5, please import the root cert for server
	verification. The cert can be found in VeriSign_G5.zip, please unzip it. Command is:
	%JAVA_HOME%/bin/keytool -import -trustcacerts -keystore "%JAVA_HOME%/jre/lib/security/cacerts" -file VeriSign_G5.crt -alias verisignclass3g5ca
	The default password is changeit.
	For those clients using JDK 1.6, please update to 21+. There is no need to import this cert.
------------------------------------------------------------------------------------------------------------------
Copyright 2012 Baidu.com Inc.
Baidu API Team