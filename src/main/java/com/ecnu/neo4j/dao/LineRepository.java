package com.ecnu.neo4j.dao;

import com.ecnu.neo4j.entity.Line;
import com.ecnu.neo4j.entity.Station;
import org.neo4j.driver.types.Path;

import java.util.List;
import java.util.Map;

public interface LineRepository {
    //1.按照线路号查询线路基本信息
    Map<String, Object> getLineInfo(String line_id) throws Exception;
    //3.以站点名称查询经过该站点的线路
    List<Map<String, Object>> getLineAlongStation(String stationName);
    //4.获得10号线指定站点到另一站点的路线和用时
    Path getRouteWithLine(String start, String end, String lineID);
    //5
    Path getShortestRouteByStation(String start, String end);
    //5-1.藉由站点的id查询指定站点到另一站点的最短路径
//    Path getShortestRouteByStationId(String startId, String endId);
//    //5-2.藉由站点的名称查询指定站点到另一站点的最短路径
//    Path getShortestRouteByStationName(String startName, String endName);
    //12. 统计每一种类型的线路数量
    List<Map<String, Object>> getLineTypeCount();
    //14-1.查询261路上行一共有多少条可以换乘的线路
    Integer getTransLineCount(String lineName);
    //14-2. 查询261路上行的可以换乘的线路名称
    List<String> getTransLineName(String lineName);
    //14-3. 查询261路上行的站点分别能换乘的线路名称
    List<Map<String, Object>> getTransLineStation(String lineName);
    //15. 根据站点数量对线路进行排序，显示前15条。
    List<Map<String, Object>> getNMostStationLine(int num);
    //17.根据运行时间对线路进行排序(运行时间由班次数据计算而得)，显示前15条。
    List<Map<String, Object>> getNMostTimeLine(int num);
    //19-1.添加一条站点数不少于10的线路。
    String newLine(Line line, List<Station> stationList);
    //20-1. 删除某条线路，若沿途站点为该线路独有，也需要删除该站点。
    String cancelLine(String lineId);
}
