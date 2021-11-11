package com.ecnu.neo4j.service;

import com.ecnu.neo4j.dto.*;

import java.util.List;

public interface LineService {
    TestCase1 findLineInfo(String line_id);
    List<TestCase3> findAlongLine(String name);
    TestCase4 findRouteWithLineTen(String start, String end);
    TestCase51 findShortestRouteByStationId(String startId, String endId);
    TestCase52 findShortestRouteByStationName(String startId, String endId);
    List<TestCase12> findLineTypeCount();
    TestCase141 findTransLintCount(String lineName);
    TestCase142 findTransLineName(String lineName);
    List<TestCase143> findTransLineStation(String lineName);
}
