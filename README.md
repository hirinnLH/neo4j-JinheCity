# neo4j-JinheCity

#### 介绍
2021非关系型数据库 金河市项目

#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.查询30路公交的基本信息。（2分）
http://localhost:8081/nosql/LineController/listLineInfo?line_id=30

2.查询2路上行的全部站点信息。(方向性、区分上下行、顺序性，2分)
http://localhost:8081/nosql/StationController/listStationInfo?name=2%E8%B7%AF%E4%B8%8A%E8%A1%8C

3.查询锦城广场站停靠的所有线路。 (同名站点按ID分组，2分)
http://localhost:8081/nosql/LineController/listAlongLine?name=%E9%94%A6%E5%9F%8E%E5%B9%BF%E5%9C%BA

4.查询乘坐10路从大悦城到小吃街，线路的运行方向(上行或下行)、沿路站点、运行时长。
变更：2分变更为3分
http://localhost:8081/nosql/LineController/listRouteWithLineTen?start=%E5%A4%A7%E6%82%A6%E5%9F%8E&end=%E5%B0%8F%E5%90%83%E8%A1%97

5.查询从id为16115的站台(红瓦寺)到id为14768的站台(动物园)的最短路径。 
• 使用id进行最短路径查询。 (2分)
http://localhost:8081/nosql/LineController/listShortestRouteByStationId?start=16115&end=14768
• 使用name进行最短路径查询。(4分)
http://localhost:8081/nosql/LineController/listShortestRouteByStationName?start=%E7%BA%A2%E7%93%A6%E5%AF%BA&end=%E5%8A%A8%E7%89%A9%E5%9B%AD

10.统计停靠路线最多的站点(按照id统计)并排序，显示前15个。(2分) 变更：显示前15个即可。
http://localhost:8081/nosql/StationController/listNMostLine?num=15

11.统计地铁站数量(以地铁开头)、起点站(末尾标识始发站)数量、 终点站(末尾标识终点站)数量、单行站(比较上下行确定单行站)数量。 并返回站点名，注意去重。 (4分)
2分：统计地铁站、起点站、终点站数量，并返回站点名 
http://localhost:8081/nosql/StationController/listCaseStation

12.分组统计常规公交(包括干线、支线、城乡线、驳接线、社区线)、 快速公交(K字开头)、高峰公交(G字开头)、夜班公交(N字开头)的数量。(2分)
http://localhost:8081/nosql/LineController/listLineTypeCount




