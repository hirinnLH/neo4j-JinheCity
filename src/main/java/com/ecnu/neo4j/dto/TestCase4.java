package com.ecnu.neo4j.dto;

import com.ecnu.neo4j.entity.Station;
import lombok.Data;

import java.util.List;

@Data
public class TestCase4 {
    private String direction;
    private List<Station> alongStation;
    private int runtime;
}
