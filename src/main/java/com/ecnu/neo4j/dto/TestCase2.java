package com.ecnu.neo4j.dto;

import com.ecnu.neo4j.entity.Station;
import lombok.Data;


import java.util.List;

@Data
public class TestCase2 {
    List<Station> alongStation;
    List<String> path;
}
