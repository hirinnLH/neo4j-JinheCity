package com.ecnu.neo4j.dao;

import com.ecnu.neo4j.dto.TestCase3;
import com.ecnu.neo4j.dto.TestCase4;
import org.neo4j.driver.types.Path;

import java.util.List;
import java.util.Map;

public interface LineRepository {
    //1.按照线路号查询线路基本信息
    Map<String, Object> getLineInfo(String line_id) throws Exception;
    //3.以站点名称查询经过该站点的线路
    List<TestCase3> getLineAlongStation(String stationName);
    //4.获得10号线指定站点到另一站点的路线和用时
    Path getRouteWithLineTen(String start, String end);
    //5-1.藉由站点的id查询指定站点到另一站点的最短路径
    Path getShortestRouteByStationId(String startId, String endId);
    //5-2.藉由站点的名称查询指定站点到另一站点的最短路径
    Path getShortestRouteByStationName(String startName, String endName);

}
