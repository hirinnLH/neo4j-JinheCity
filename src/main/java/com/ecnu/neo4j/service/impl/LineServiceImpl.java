package com.ecnu.neo4j.service.impl;

import com.ecnu.neo4j.dao.LineRepository;
import com.ecnu.neo4j.dao.impl.LineRepositoryImpl;
import com.ecnu.neo4j.dto.*;
import com.ecnu.neo4j.entity.Station;
import com.ecnu.neo4j.service.LineService;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LineServiceImpl implements LineService {

    private LineRepository lineRepository = new LineRepositoryImpl();

    @Override
    public TestCase1 findLineInfo(String line_id) {
        TestCase1 lineInfoDto = new TestCase1();
        Map<String, Object> map = null;
        try {
            map = lineRepository.getLineInfo(line_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lineInfoDto.setLine_id((String) map.get("id"));
        lineInfoDto.setInterval((String) map.get("interval"));
        lineInfoDto.setDirectional((String) map.get("directional"));
        lineInfoDto.setKilometer((String) map.get("kilometer"));
        lineInfoDto.setRoute((String) map.get("route"));
        lineInfoDto.setTimetable((String) map.get("runtime"));
        lineInfoDto.setType((String) map.get("type"));
        lineInfoDto.setOnewayTime((String) map.get("onewayTime"));
        return lineInfoDto;
    }

    @Override
    public List<TestCase3> findAlongLine(String name) {
        return lineRepository.getLineAlongStation(name);
    }

    @Override
    public TestCase4 findRouteWithLineTen(String start, String end) {
        Path path = lineRepository.getRouteWithLineTen(start, end);
        List<Station> stationList = new ArrayList<>();
        String direction = null;
        TestCase4 testCase4 = new TestCase4();
        int timeCount = 0;
        for(Path.Segment segment:path) {
            direction = segment.relationship().type();
            timeCount += Integer.parseInt(segment.relationship().get("runtime").asString());
        }
        for(Node node:path.nodes()) {
            Station station = new Station();
            station.setSystemId(node.id());
            station.setEnglish(node.get("english").asString());
            station.setName(node.get("name").asString());
            station.setId(node.get("id").asString());
            stationList.add(station);
        }

        testCase4.setRuntime(timeCount);
        testCase4.setDirection(direction);
        testCase4.setAlongStation(stationList);
        return testCase4;
    }

    @Override
    public TestCase51 findShortestRouteByStationId(String startId, String endId) {
        Path path = lineRepository.getShortestRouteByStationId(startId, endId);
        TestCase51 testCase51 = new TestCase51();
        testCase51.setPath(path.toString());
        return testCase51;
    }

    @Override
    public TestCase52 findShortestRouteByStationName(String startName, String endName) {
        Path path = lineRepository.getShortestRouteByStationName(startName, endName);
        TestCase52 testCase52 = new TestCase52();
        testCase52.setPath(path.toString());
        return testCase52;
    }

    @Override
    public List<TestCase12> findLineTypeCount() {
        return lineRepository.getLineTypeCount();
    }

}