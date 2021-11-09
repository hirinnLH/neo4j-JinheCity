package com.ecnu.neo4j.dao;

import com.ecnu.neo4j.dto.TestCase10;
import org.neo4j.driver.types.Path;

import java.util.List;

public interface StationRepository {
    //2.用线路名称返回沿路站点
    Path getStationInfo(String name);
    //7.以线路名称查询班次信息
    //List<Path> getDepartInfo(String name);
    //10.查询停靠线路最多的前n个站点
    List<TestCase10> getNMostLine(int num);
}
