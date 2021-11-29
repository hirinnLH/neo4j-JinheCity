package com.ecnu.neo4j.service;

import com.ecnu.neo4j.dto.*;
import com.ecnu.neo4j.entity.Line;
import com.ecnu.neo4j.entity.Station;

import java.util.List;

public interface LineService {
    TestCase1 findLineInfo(String line_id);
    List<TestCase3> findAlongLine(String name);
    TestCase4 findRouteWithLine(String start, String end, String lineId);
    TestCase5 findShortestRouteByStation(String start, String end);
//    TestCase51 findShortestRouteByStationId(String startId, String endId);
//    TestCase52 findShortestRouteByStationName(String startId, String endId);
    List<TestCase12> findLineTypeCount();
    TestCase141 findTransLintCount(String lineName);
    TestCase142 findTransLineName(String lineName);
    List<TestCase143> findTransLineStation(String lineName);
    List<TestCase16> findNMostStationLine(int num);
    List<TestCase17> findNMostTimeLine(int num);
    String addLine(String params);
    String removeLine(String lineId);
}
