package com.ecnu.neo4j.service;

import com.ecnu.neo4j.dto.TestCase10;
import com.ecnu.neo4j.dto.TestCase111;
import com.ecnu.neo4j.dto.TestCase2;
import com.ecnu.neo4j.dto.TestCase7;

import java.util.List;

public interface StationService {
    TestCase2 findStationInfo(String name);
    //TestCase7 findDepartInfo(String name);
    List<TestCase10> findNMostLine(int num);
    TestCase111 findCaseStation();
}
