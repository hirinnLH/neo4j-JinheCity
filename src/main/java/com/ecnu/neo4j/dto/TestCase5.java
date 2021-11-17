package com.ecnu.neo4j.dto;

import com.ecnu.neo4j.entity.Station;
import lombok.Data;

import java.util.List;

@Data
public class TestCase5 {
    private List<Station> alongStation;
    private List<String> transLine;
}
