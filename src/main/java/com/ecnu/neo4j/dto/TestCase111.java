package com.ecnu.neo4j.dto;

import com.ecnu.neo4j.entity.StationWithoutEnglish;
import lombok.Data;

import java.util.List;

@Data
public class TestCase111 {
    private List<String> subwayName;
    private int subwayStation;
    private List<String> initialName;
    private int initialStation;
    private List<String> endName;
    private int endStation;
}
