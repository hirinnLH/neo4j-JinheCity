package com.ecnu.neo4j.dto;

import com.ecnu.neo4j.entity.Station;
import com.ecnu.neo4j.entity.StationWithoutEnglish;
import lombok.Data;

import java.util.List;

@Data
public class TestCase112 {
    private List<StationWithoutEnglish> singleDir;
    private int singleDirCnt;
}
