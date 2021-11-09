package com.ecnu.neo4j.dto;

import lombok.Data;

import java.util.List;

@Data
public class TestCase7 {
    List<String> stationList;
    List<List<String>> time;
}
