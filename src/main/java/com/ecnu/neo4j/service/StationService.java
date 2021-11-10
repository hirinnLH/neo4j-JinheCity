package com.ecnu.neo4j.service;

import com.ecnu.neo4j.dto.*;

import java.util.List;

public interface StationService {
    TestCase2 findStationInfo(String name);
    //TestCase7 findDepartInfo(String name);
    List<TestCase10> findNMostLine(int num);
    TestCase111 findCaseStation();
    TestCase13 findCrossStation(String line1, String line2);
}
