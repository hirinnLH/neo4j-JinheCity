package com.ecnu.neo4j.service;

import com.ecnu.neo4j.dto.*;
import com.ecnu.neo4j.entity.Station;

import java.util.List;
import java.util.Map;

public interface StationService {
    List<TestCase2> findStationInfo(String name);
    TestCase7 findDepartInfo(String name);
    List<TestCase10> findNMostLine(int num);
    TestCase111 findCaseStation();
    TestCase13 findCrossStation(String line1, String line2);
    List<TestCase15> findNMostLineStation(int num);
}
