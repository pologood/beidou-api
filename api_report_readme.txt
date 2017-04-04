******************************************************************************
****                       报告模块说明                                   ****
****                       author：张旭                                  ****
******************************************************************************

=========================================
== 1. ReportService总体设计
=========================================
1.1 概述
通过 ReportService，您可以获取推广效果的详细报表。由于产生报表需要一定的时间，我们采用了异步的方式对报表进行处理。您首先需要通过 getReportId()方法获得一个字符串类型的reportId，然后通过pull的方式轮询调用 getReportState()方法检查报表是否产生。待产生后，则可调用 getReportFileUrl()方法获取报表的下载地址。根据该地址即可下载您所需要的报表。 
请注意，URL的有效时长为1小时，如果超时则需重新调用此方法，获取新的url。
报告ID为string类型，采用<32位16进制数>的取值形式，例如：8e7e3f2d84a19c5df1415957434b2bd8

提交报告请求
通过getReportId()方法，您可以获取推广计划报告、推广组报告、创意报告、网站报告、搜客关键词报告、主题词报告。在调用该方法前，您需要构造GetReportIdRequest对象，并构造其中的ReportRequestType对象。在上一次请求尚未完成前，不允许发起下一次请求，如果发送了请求，系统需要等待前一个查询结束后才可以执行下一个查询。

查询生成报告的状态
通过pull的方式轮询调用 getReportState()方法检查报表是否产生。在调用该方法前，您需要构造GetReportStateRequest对象。

获取报告下载url
getReportState()返回3表示生成成功后，可调用 getReportFileUrl()方法获取报表的下载地址。根据该地址即可下载您所需要的报表。在调用该方法前，您需要构造GetReportFileUrlRequest对象。

1.2 接口说明
数据类型
ReportRequestType
属性名	类型	说明	限制
performanceData	String[]	指定绩效数据	选填；默认为srch,click,cost,ctr,cpm,acp
取值范围为srch,click,cost,ctr,cpm,acp任意组合，报表按照srch,click,cost,ctr,cpm,acp的顺序输出绩效数据。
各绩效英文缩写含义如下：
srch: 展现次数
click：点击次数
cost：消费（￥，精确到小数点后两位）
ctr：点击率（0.XXXXXX，1表示100%，精确到小数点后6位）
cpm：前次展现成本（￥，精确到小数点后两位）
acp：平均点击价格（￥，精确到小数点后两位）

startDate	date	统计开始时间，格式参考：
20120801	必填，从2010-08-13开始
如果是主题词报告必须不得早于
如果是搜客关键词报告不得早于

endDate	date	统计开始时间，格式参考：
20120801	必填
不得早于统计开始时间

idOnly	boolean	是否只需要id	选填；默认为false
取值范围：
false：既获取id也获取字面
true：只获取id

reportType	int	报告类型	必填；
1. 账户报告
2. 推广计划报告
3. 推广组报告
4. 创意报告
5. 主题词报告
6. 搜客关键词报告
7. 网站报告

statRange	int	统计范围	选填
1. 账户
2. 推广计划
3. 推广组
4. 创意
5. 关键词
6. 主题词
统计范围不能细于当前要查看的报告类型（reportType）。
统计范围为账户，可以查看任一报告类型，此时任何设置的id集合（statIds）均无效；
统计范围为推广计划，可以查看计划、组、创意、网站、搜客关键词、主题词报告，不设置id集合表示全部推广计划范围；
统计范围为推广组，可以查看组、创意、网站、搜客关键词、主题词报告，不设置id集合表示全部推广组范围；
统计范围为创意，只能查看创意报告，不设置id集合表示全部创意范围；
统计范围为主题词，只能查看主题词报告，不设置id集合表示全部主题词范围；
统计范围为关键词，只能查看搜客关键词报告，不设置id集合表示全部关键词范围。
statIds	long[]	统计范围下的id集合。根据StatRange的不同类型填写不同id，需要与statRange参数配合一起使用。	选填，默认NULL。
staRange为2时填写推广计划id;
staRange为3时填写推广组id;
staRange为4时填写创意id;
staRange为5时填写主题词id;
staRange为6时填写关键词id
id至多100个。

format	int	报告文件格式	选填，默认值为0；
0：zip压缩包格式
1：csv格式


GetReportIdRequest
属性名	类型	说明	限制
reportRequestType	ReportRequestType	报告查询条件	必填


GetReportIdResponse
属性名	类型	说明	限制
reportId	String	生成的报告ID	ID为一串MD5值，格式为32位16进制数，
例如
8e7e3f2d84a19c5df1415957434b2bd8


GetReportStateRequest
属性名	类型	说明	限制
reportId	String	请求的报告ID	ID为一串MD5值，格式为32位16进制数，
例如
8e7e3f2d84a19c5df1415957434b2bd8


GetReportStateResponse
属性名	类型	说明	限制
isGenerated	int	报告生成状态	1：等待中
2：处理中
3：处理成功
4：处理失败
5：查询范围过大失败


GetReportFileUrlRequest
属性名	类型	说明	限制
reportId	String	报告ID	ID为一串MD5值，格式为32位16进制数，
例如
8e7e3f2d84a19c5df1415957434b2bd8


GetReportFileUrlResponse
属性名	类型	说明	限制
reportFilePath	String	下载地址url	生成的url有效期为1小时，如果超时则需重新获取新的url


接口描述
getReportId
GetReportIdResponse getReportId(GetReportIdRequest parameters)

 	方法说明
请求绩效数据报告 

 	输入信息
GetReportIdRequest：绩效数据的查询条件，含有ReportRequestType对象 

 	返回信息
GetReportIdResponse：含有本次请求所返回的reportId.


getReportState
GetReportStateResponse getReportState(GetReportStateRequest parameters)

 	方法说明
查询报告是否生成 

 	输入信息：
GetReportStateRequest，含有reportId

 	返回信息：
GetReportStateResponse，报告生成状态

 	附加说明：
在获取Report下载url前，请调用此方法。待确认报表已生成时，再获取下载的url。


getReportFileUrl
GetReportFileUrlResponse getReportState(GetReportFileUrlRequest parameters)

 	方法说明
获取报告下载地址 

 	输入信息：
GetReportFileUrlRequest，含有reportId

 	返回信息：
GetReportFileUrlResponse，含有下载url链接

 	附加说明：
生成的url有效期为1小时，如果超时则需重新调用此方法，获取新的url。
您下载到的报告文件是没有后缀的，如果您使用程序处理，可以自由添加您想要的后缀。



=========================================
== 2. 错误码
=========================================
UNAUTHORIZATION_STATRANGE_PLAN(8000, "StatIds do not have no authorization under the plan statRange"),
UNAUTHORIZATION_STATRANGE_GROUP(8001, "StatIds do not have no authorization under the group statRange"),
UNAUTHORIZATION_STATRANGE_UNIT(8002, "StatIds do not have no authorization under the unit statRange"),
UNAUTHORIZATION_STATRANGE_CTKEYWORD(8003, "StatIds do not have no authorization under the ct keyword statRange"),
UNAUTHORIZATION_STATRANGE_QTKEYWORD(8004, "StatIds do not have no authorization under the qt keyword statRange"),
UNEXPECTED_PARAMETER_STATIDS(8005, "StatIds can not be empty under the statRange"),
UNEXPECTED_PARAMETER_TOO_LONG_STATIDS(8006, "StatIds is too long"),
UNEXPECTED_PARAMETER_STATRANGE_REPORTTYPE_MISMATCH(8007, "Report type and statRange mismatch"),
UNEXPECTED_PARAMETER_STARTDATE_NOT_BEFORE_AFTERDATE(8008, "End date is not after start date"),
UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_TOO_EARLY(8009, "Date should be after 2008-11-13"),
UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_CT_TOO_EARLY(8010, "When query ct keyword report, start date should be after 2011-11-15"),
UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_QT_TOO_EARLY(8011, "When query qt keyword report, start date should be after 2011-06-23 "),
WRONG_REPORTID(8012, "reportId is not valid"),
GET_FILEURL_FAIL(8013,"the report file cannot be found"),
GET_REPORTSTATE_NOTFOUND(8014,"the report id cannot be found"),
REPORT_FORBIDDEN_DUE_TO_LOADCONTROL(8015,"your request too frequently, hold on for a while"),



=========================================
== 3. 数据库表设计
=========================================
该表是用于保存所有报告请求的表

/* Table: beidoureport.api_report   */

CREATE TABLE `api_report` (

`id` bigint(20) NOT NULL auto_increment,

`queryparam` varchar(2048) NOT NULL COMMENT '提交报告的查询JSON串',

`status` int(4)    NOT NULL default '1' COMMENT '任务处理状态：1: 未开始；2: 处理中；3: 完成; 4XX:失败; 5: 查询范围过大失败; ',

`userid`  int(10) NOT NULL COMMENT '被请求报告所属userid',

`reportid` varchar(100) NOT NULL COMMENT '报告id，给予请求者的token',

`machineid`   varchar(50) NOT NULL COMMENT '正在处理的机器host',

`performancedata`  int(10) NOT NULL default '63' COMMENT '显示哪些绩效数据，包括展现、点击、消费、CTR、CPM、ACP，默认63的二进制值为111111',

`iszip` tinyint(3) NOT NULL default '0' COMMENT '是否是压缩过到报告文件, 0为zip,1为csv格式',

`opuser` int(10) NOT NULL COMMENT '请求报告操作人userid',

`addtime` datetime NOT NULL COMMENT '请求时间',

`modtime` datetime NOT NULL COMMENT '程序处理当中的修改时间',

`retry`  int(10)  NOT NULL default '0' COMMENT '重试次数',

PRIMARY KEY (`id`),

KEY `IN_APIREPORT_STATUS_MACHINEID` (`status`,`machineid`),

KEY `IN_APIREPORT_STATUS_REPORTID` (`reportid`)

)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='API报告表' ;


=========================================
== 4. 任务状态
=========================================
4.1 外部任务状态
所谓外部任务状态就是通过getReportState()接口获取的状态值。

1：等待中
2：处理中
3：处理成功
4：处理失败
5：查询范围过大失败（一般是因为doris查询返回-10，-250等导致）

4.2 内部任务状态
所谓内部任务状态就是beidoureport.api_report表中status字段保存的值。
未处理：1
处理中：2
完成：3
失败：400
服务器内部错误强制失败：401
查询范围过大失败：402
处理异常重试中：403
处理超时重试中：404

其中4XX代表错误，虽然都是错误但是对于前端 getReportState()接口来说获取状态不一定就是错误的。继续往下看，

4.3 内外部状态映射
内部   		=> 			外部
1未处理 	=>			1等待中
2处理中 	=>			2处理中
3完成 		=> 			3处理成功
400失败 	=> 			4处理失败
401服务器内部错误强制失败 => 4处理失败
402查询范围过大失败 => 5：查询范围过大失败（一般是因为doris查询返回-10，-250等导致）
403处理异常重试中 => 处理中
404处理超时重试中 => 处理中


=========================================
== 5. Report工作方式
=========================================
5.1 getReportId

1. 如果开启了节流阀，首先过节流阀，某个请求用户所有“处理中”、“处理超时重试中”状态的任务数量超过一个阈值，当前为5，则禁止其再请求新报告，否则继续。

2. 拿到请求进行各种校验解析，如果通过则继续2，否则返回错误信息

3. 如果开启了哈希缓存，则从memcache中查找该请求串为key对应的value（即reportId），如果命中缓存则直接返回reportId，否则将任务插入到数据库中的api_report表，如果插入成功则继续3。

4. 插入任务到JMS队列中（监听端口1066X），每个tomcat实例默认启动25个消费者线程来处理任务，处理任务的逻辑是

1）判断参数是否合法，不合法将任务状态置为“失败”，否则继续。

2）更新任务状态为“处理中“。

3）构造后端的请求参数queryparam，查询doris返回数据，针对不同的报告类型，进行merge的方式不同：

3.1）账户、计划、组、创意、以DB数据左连接，没有绩效数据的，展现点击消费置为0，CTR,CPM,ACP置为-1

3.2）网站，以doris数据为准，只查询一级域名，对于百度自有流量的需要再次查询doris查出二级域名的统计数据（例如tieba.baidu.com）；查询不出的保存只包含表头的空文件

3.3）主题词，搜客关键词以doris数据为准，查询不出的保存只包含表头的空文件，对于doris中有北斗没有的数据，在词的后面追加“[已删除]”三个字。

如果doris查询范围过大则置任务状态为“查询范围过大失败“

4）保存查询结果到文件中，文件名为md5串32位字符，无后缀，文件以tab分割。

5）如果需要压缩，则压缩成zip文件，文件名为追加“.zip”。

6）更新任务状态为处理“成功“。

7）如果开启来哈希缓存，则往memcache中加入key=请求串，value=reportId的键值对，超时时间默认为3600秒。

 
中间发生不可恢复的系统内部错误，例如参数错误，置状态为“失败“；如果发生了IOException，或者其他内部异常，则允许进行重试，目前会重试2两次，如果还是失败了最终会置状态为”失败“，否则为”失败重试中“，前端来看就是”处理中状态“。


5.2 getReportState
查询数据库中的任务，返回映射到前端的状态码

5.3 getReportFileUrl
如果status=3，那么返回下载的url，url的配置见下一节

5.4 轮询处理失败、超时任务,叫做cron trigger
由com/baidu/beidou/api/external/report/facade/impl/ApiReportTaskMonitor类处理，目前是每5分钟检测一次并处理

5.5 系统初始化需要处理上一次断掉的任务
由com/baidu/beidou/api/external/report/system/InitUndoneTasks类处理，在InitSystemListener中调用

5.5 节流阀LoadControl拦截器
由com/baidu/beidou/api/external/report/interceptor/LoadControlInterceptor类处理

=========================================
== 6. 程序配置
=========================================
6.1 platform-related.properties

API_REPORT.DOWNLOAD_URL_PREFIX=http://10.81.31.95:8088/apireport/
API_REPORT.DOWNLOAD_FILE_SAVEPATH=/home/work/beidou/apache/htdocs/apireport

API_REPORT.DOWNLOAD_URL_PREFIX表示getFileUrl接口返回的文件服务器前缀，例如可以返回http://10.81.31.95:8088/apireport/329urwej98ewurw9q8ur.zip，真正上线的时候这个ip和端口会是bvs的地址。
API_REPORT.DOWNLOAD_FILE_SAVEPATH是文件保存的路径，真正的线上是用mfs映射的磁盘。文件会在指定路径生成，可以在配置的路径下取文件

6.2 reportConfig.properties

# 表示需要查询doris返回二级域名对应绩效数据的域名列表，以逗号分割
ReportWebConstants.showSecondDomainSiteList=baidu.com

# 传入statIds最大的长度
ReportWebConstants.MAX_STATIDS=100

# 失败、超时重试最大次数
ReportWebConstants.MAX_RETRY=2

# 报告模块cron trigger每隔几分钟检查失败、超时任务的设置时间
# 认为已经处理超时的时间分钟数，任务状态是“处理中”，如果now-optime大于这个分钟数，则认为是超时任务
ReportWebConstants.TASK_TIMEOUT_MINUTES=5

# 是否开启节流阀，用来作负载控制，由LoadControlInterceptor调用，如果为false则不限制用户并发请求
ReportWebConstants.ENABLE_THROTTLE=true
# 1个用户最多可以同时处理的任务数量，用来作负载控制，由LoadControlInterceptor调用，如果1个用户“未处理”+“处理中”状态的任务超过这个阈值就禁止其再插入JMS任务
ReportWebConstants.CONCURRENT_TASKS_PER_USER=5

# 是否开启哈希缓存，开启哈希缓存会将成功的queryparam做md5哈希成一个key，对应到value是
# reportId，保存在memcache中缓存，如果相同到query过来，就直接取出reportId返回，不放入JMS队列中处理
# 如果为false，则不保存在memcache中
ReportWebConstants.ENABLE_HASH_CACHE=true
# 此参数只有在开启了哈希缓存时才有效，该参数单位为秒，
# 设置保存在memcache中缓存的实效时间
ReportWebConstants.EXPIRE_TIME_OF_HASH_CACHE=3600

# 是否在服务器启动时处理所有状态为1和404的任务，做到原子性，由InitUndoneTasks使用
ReportWebConstants.ENABLE_INIT_ATOMICITY=true

# qt时间范围
qtStartDateYYYY=2011
qtStartDateMM=6
qtStartDateDD=23

# ct时间范围
ktStartDateYYYY=2011
ktStartDateMM=11
ktStartDateDD=15

# beidou时间范围
beidouStartDateYYYY=2008
beidouStartDateMM=11
beidouStartDateDD=13

=========================================
== 7. 黑名单功能
=========================================
可以配置api的黑名单用户列表，PM通过总控中心配置，beidou-cron通过脚本导入userid到beidou.api_blacklist表，每5分钟检测一回是否有改变，如果有则刷新缓存，命中黑名单到用户访问api会报错SystemError。

=========================================
== 8. 调用方式举例
=========================================

McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.95:8230/beidou-api/api/ApiReportService", "UTF-8", new ExceptionHandler());

ApiReportService exporter = ProxyFactory.createProxy(ApiReportService.class, proxy);

GetReportIdRequest request = new GetReportIdRequest();

ReportRequestType type = new ReportRequestType();

//type.setPerformanceData(new String[]{"SrCh","acp","srch","ctr","acp","cost"});

type.setPerformanceData(new String[]{});

String start = "20111231";

String end = "20120104";

type.setStartDate(DateUtils.strToDate(start));

type.setEndDate(DateUtils.strToDate(end));

type.setFormat(0);

type.setIdOnly(false);

type.setReportType(7);

type.setStatRange(2);

type.setStatIds(new long[]{757447l, 757448l});

//type.setStatIds(new long[]{2166146l, 2166147l, 2166149l});

//type.setStatIds(new long[]{9283622l, 9283621l, 9283628l, 9283629l});

request.setReportRequestType(type);

ApiResult<GetReportIdResponse> result = exporter.getReportId(dataUser, request, apiOption);

System.out.println(result);

 
