<h1>一.介绍</h1>
<h2>1.1 项目介绍</h2>
金河市（虚拟城市）是⼀座风景优美的城市，包括信息技术产业专区⾼新区、历史悠久的老城区、⼈口流动较多的
天河区和有高速公路联通的合江县。有两座火车站（火车西站和金河南站）和⼀座快铁站（普光快铁），有两座客
运站（金河客运站和北客站）。其公交网络也⽐较发达，⼀共有93条公交线路，包括常规线路、快速公交、高峰公
交、夜班公交等多种类型。现需要你利用所学到的非关系型数据库理论，为金河市设计一个公交线路系统。
<h2>1.2 项目初衷</h2>
<ul>
    <li>不仅仅是简单的CRUD和调用框架。</li>
    <li>适量的工程设计与开发能力。</li>
    <li>与时俱进、结合具体生活场景。</li>
    <li>为大四下软件开发实践做铺垫。</li>
    <li>积累项目经验，简历or毕业设计。</li>
</ul>

<h2>1.3 项目要求</h2>
<ul>
    <li>必须使用至少一种非关系型数据库。</li>
    <li>仔细阅读需求文档，结合数据特点，选择合适的非关系型数据库。</li>
    <li>需要有前端界面进行人机交互与可视化。</li>
    <li>注意设计的合理性，是否符合真实场景的需求。</li>
</ul>

<h2>1.4 工具及版本要求</h2>
<ul>
    <li>Neo4j community 4.3.4 (plugins: apoc, neo4j-graph-data-science)</li>
    <li>IntelliJ IDEA Ultimate 2021.2</li>
    <li>Java 11</li>
</ul>

<h1>二.配置Neo4j</h1>
<ol>
    <li>在https://neo4j.com/download-center/#community下载和安装Neo4j数据库，4.0以上版本，java要求11以上</li>
    <li>在管理员权限下打开cmd，输入neo4j.bat console验证是否安装和配置成功</li>
    <li>在浏览器中输入http://localhost:7474，初始密码和用户名皆为neo4j，需要自己改密码</li>
</ol>

<h1>三.已实现需求</h1>
请使用http://localhost:8081/nosql/controller名/方法名?参数= 访问对应需求<br><br>
<ul>
    <li>基本信息查询</li>
</ul>
1.查询30路公交的基本信息。
http://localhost:8081/nosql/LineController/listLineInfo?line_id=30

<ul>
    <li>站点查询</li>
</ul>
2.查询2路上行的全部站点信息。(方向性、区分上下行、顺序性，)
http://localhost:8081/nosql/StationController/listStationInfo?name=2%E8%B7%AF%E4%B8%8A%E8%A1%8C

3.查询锦城广场站停靠的所有线路。 (同名站点按ID分组)
http://localhost:8081/nosql/LineController/listAlongLine?name=%E9%94%A6%E5%9F%8E%E5%B9%BF%E5%9C%BA

4.查询乘坐10路从大悦城到小吃街，线路的运行方向(上行或下行)、沿路站点、运行时长。
http://localhost:8081/nosql/LineController/listRouteWithLine?start=%E5%A4%A7%E6%82%A6%E5%9F%8E&end=%E5%B0%8F%E5%90%83%E8%A1%97&lineId=10

5.查询从id为16115的站台(红瓦寺)到id为14768的站台(动物园)的最短路径。 
http://localhost:8081/nosql/LineController/listShortestRouteByStation?start=%E7%BA%A2%E7%93%A6%E5%AF%BA&end=%E5%8A%A8%E7%89%A9%E5%9B%AD

<ul>
    <li>班次查询</li>
</ul>
7.查询某条线路某个方向的全部班次信息。
10.统计停靠路线最多的站点(按照id统计)并排序，显示前15个。 
http://localhost:8081/nosql/StationController/listNMostLine?num=15

<ul>
    <li>分析查询</li>
</ul>
11.统计地铁站数量(以地铁开头)、起点站(末尾标识始发站)数量、 终点站(末尾标识终点站)数量、单行站(比较上下行确定单行站)数量。 并返回站点名，注意去重。 
A: 统计地铁站、起点站、终点站数量，并返回站点名 
http://localhost:8081/nosql/StationController/listCaseStation

12.分组统计常规公交(包括干线、支线、城乡线、驳接线、社区线)、 快速公交(K字开头)、高峰公交(G字开头)、夜班公交(N字开头)的数量。
http://localhost:8081/nosql/LineController/listLineTypeCount

13.查询15路上行和30路下行重复的站点名，并统计站点数。
http://localhost:8081/nosql/StationController/listCrossStation?line1=15%E8%B7%AF%E4%B8%8A%E8%A1%8C&line2=30%E8%B7%AF%E4%B8%8B%E8%A1%8C

14.查询261路上行一共有多少条可以换乘的线路，注意去重。 换乘线路数即261路上行停靠的所有站台停靠其他线路的数量的总和。
<ul>

    <li>统计可以换乘的线路数量</li>
    http://localhost:8081/nosql/LineController/listTransLineCount?lineName=261%E8%B7%AF%E4%B8%8A%E8%A1%8C
    
    <li>统计可以换乘的线路名称</li>
    http://localhost:8081/nosql/LineController/listTransLineName?lineName=261%E8%B7%AF%E4%B8%8A%E8%A1%8C
    
    <li>统计沿线每个站点可以换乘的线路</li>
    http://localhost:8081/nosql/LineController/listTransLineStation?lineName=261%E8%B7%AF%E4%B8%8A%E8%A1%8C
</ul>

15.查询连接两个站台之间线路最多的两个站台并且按照降序排列，显示前15个。
http://localhost:8081/nosql/StationController/listNMostLineStation?num=15

16.根据站点数量对线路进行排序，显示前15条。
http://localhost:8081/nosql/LineController/listNMostStationLine?num=15

17.根据运行时间对线路进行排序(运行时间由班次数据计算而得)，显示前15条。(2分)
http://localhost:8081/nosql/LineController/listNMostTimeLine?num=15





