package com.ecnu.neo4j.dto;

import com.ecnu.neo4j.entity.Station;
import lombok.Data;
import org.w3c.dom.Node;

import java.util.List;

@Data
public class TestCase13 {
    private List<Station> crossStation;
    private int count;
}
