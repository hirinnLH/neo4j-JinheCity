package com.ecnu.neo4j.dto;

import lombok.Data;

import java.util.List;

@Data
public class TestCase10 {
    private String stationName;
    private int lineNum;
    private List<Object> lineName;
}
