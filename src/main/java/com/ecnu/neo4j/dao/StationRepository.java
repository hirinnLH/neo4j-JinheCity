package com.ecnu.neo4j.dao;

import com.ecnu.neo4j.dto.TestCase10;
import com.ecnu.neo4j.dto.TestCase111;
import com.ecnu.neo4j.entity.Station;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.List;
import java.util.Map;

public interface StationRepository {
    //2.用线路名称返回沿路站点
    Path getStationInfo(String name);
    //7.以线路名称查询班次信息
    //List<Path> getDepartInfo(String name);
    //10.查询停靠线路最多的前n个站点
    List<Map<String, Object>> getNMostLine(int num);
    //11-1. 查询地铁站、终点站、始发站
    Map<String, Object> getCaseStation();
    //13 查询两路线的交叉站点
    Map<String, Object> getCrossStation(String line1, String line2);
}
