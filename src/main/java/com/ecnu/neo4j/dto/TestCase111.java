package com.ecnu.neo4j.dto;

import com.ecnu.neo4j.entity.StationWithoutEnglish;
import lombok.Data;

import java.util.List;

@Data
public class TestCase111 {
    private List<StationWithoutEnglish> subwayName;
    private int subwayStation;
    private List<StationWithoutEnglish> initialName;
    private int initialStation;
    private List<StationWithoutEnglish> endName;
    private int endStation;
}
