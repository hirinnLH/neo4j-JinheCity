package com.ecnu.neo4j.service.impl;

import com.ecnu.neo4j.dao.LineRepository;
import com.ecnu.neo4j.dao.impl.LineRepositoryImpl;
import com.ecnu.neo4j.dto.*;
import com.ecnu.neo4j.entity.Line;
import com.ecnu.neo4j.entity.Station;
import com.ecnu.neo4j.service.LineService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.io.IOException;
import java.util.*;

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
        if(map == null) {
            return null;
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
        List<TestCase3> returnList = new ArrayList<>();
        List<Map<String, Object>> mapList = lineRepository.getLineAlongStation(name);
        for(Map<String, Object> map:mapList) {
            TestCase3 testCase3 = new TestCase3();
            testCase3.setAlongLine((List<Object>) map.get("lines"));
            testCase3.setId((String) map.get("id"));
            returnList.add(testCase3);
        }
        return returnList;
    }

    @Override
    public TestCase4 findRouteWithLine(String start, String end, String lineId) {
        Path path = lineRepository.getRouteWithLine(start, end, lineId);
        if(path == null) {
            return null;
        }
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

    //需求5
    @Override
    public TestCase5 findShortestRouteByStation(String start, String end) {
        Path path = lineRepository.getShortestRouteByStation(start, end);
        if(path == null) {
            return null;
        }
        TestCase5 testCase5 = new TestCase5();
        List<String> transLine = new ArrayList<>();
        for(Path.Segment segment:path) {
            transLine.add(segment.relationship().type());
        }
        List<Station> alongStation = new ArrayList<>();
        for(Node node:path.nodes()) {
            Station station = new Station();
            station.setSystemId(node.id());
            station.setName(node.get("name").asString());
            station.setEnglish(node.get("english").asString());
            station.setId(node.get("id").asString());
            alongStation.add(station);
        }
        testCase5.setTransLine(transLine);
        testCase5.setAlongStation(alongStation);
        return testCase5;
    }

//    @Override
//    public TestCase51 findShortestRouteByStationId(String startId, String endId) {
//        Path path = lineRepository.getShortestRouteByStationId(startId, endId);
//        if(path == null) {
//            return null;
//        }
//        TestCase51 testCase51 = new TestCase51();
//        testCase51.setPath(path.toString());
//        return testCase51;
//    }
//
//    @Override
//    public TestCase52 findShortestRouteByStationName(String startName, String endName) {
//        Path path = lineRepository.getShortestRouteByStationName(startName, endName);
//        TestCase52 testCase52 = new TestCase52();
//        testCase52.setPath(path.toString());
//        return testCase52;
//    }

    @Override
    public List<TestCase12> findLineTypeCount() {
        List<Map<String, Object>> mapList = lineRepository.getLineTypeCount();
        List<TestCase12> list = new ArrayList<>();
        int normalCount = 0;
        for(Map<String, Object> map:mapList) {
            TestCase12 testCase12 = new TestCase12();
            if(map.get("type").toString().equals("高峰线")) {
                testCase12.setCount((Integer) map.get("count"));
                testCase12.setType("高峰公交");
                list.add(testCase12);
            }
            else if(map.get("type").toString().equals("夜班线")) {
                testCase12.setCount((Integer) map.get("count"));
                testCase12.setType("夜班公交");
                list.add(testCase12);
            }
            else if(map.get("type").toString().equals("快速公交")) {
                testCase12.setCount((Integer) map.get("count"));
                testCase12.setType((String) map.get("type"));
                list.add(testCase12);
            }
            else {
                normalCount += (Integer) map.get("count");
            }
        }
        TestCase12 testCase12 = new TestCase12();
        testCase12.setCount(normalCount);
        testCase12.setType("常规公交");
        list.add(testCase12);
        return list;
    }

    @Override
    public TestCase141 findTransLintCount(String lineName) {
        Integer count = lineRepository.getTransLineCount(lineName);
        TestCase141 testCase141 = new TestCase141();
        testCase141.setCount(count);
        return testCase141;
    }

    @Override
    public TestCase142 findTransLineName(String lineName) {
        TestCase142 testCase142 = new TestCase142();
        testCase142.setName(lineRepository.getTransLineName(lineName));
        return testCase142;
    }

    @Override
    public List<TestCase143> findTransLineStation(String lineName) {
        List<TestCase143> list = new ArrayList<>();
        List<Map<String, Object>> mapList = lineRepository.getTransLineStation(lineName);
        for(Map<String,Object> map:mapList) {
            TestCase143 testCase143 = new TestCase143();
            testCase143.setName(map.get("name").toString());
            testCase143.setType((List<String>) map.get("type"));
            list.add(testCase143);
        }
        return list;
    }

    @Override
    public List<TestCase16> findNMostStationLine(int num) {
        List<TestCase16> list = new ArrayList<>();
        List<Map<String, Object>> mapList = lineRepository.getNMostStationLine(num);
        for(Map<String, Object> map:mapList) {
            TestCase16 testCase16 = new TestCase16();
            testCase16.setCount((Integer) map.get("count"));
            testCase16.setType(map.get("type").toString());
            list.add(testCase16);
        }
        return list;
    }

    @Override
    public List<TestCase17> findNMostTimeLine(int num) {
        List<Map<String, Object>> mapList = lineRepository.getNMostTimeLine(num);
        List<TestCase17> list = new ArrayList<>();
        for(Map<String, Object> map:mapList) {
            TestCase17 testCase17 = new TestCase17();
            testCase17.setTime((Integer) map.get("time"));
            testCase17.setType(map.get("type").toString());
            list.add(testCase17);
        }
        return list;
    }

    @Override
    public String addLine(String params) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = new HashMap<>();


        if(StringUtils.isEmpty(params)) {
            return null;
        }

        try {
            map = objectMapper.readValue(params, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return "读取数据失败";
        }

        Line line = new Line();
        List<Station> list = new ArrayList<>();
        if(map != null) {
            if(map.get("line") instanceof Map) {
                Map<String, String> map1 = (Map<String, String>) map.get("line");
                line.setLine_id(map1.get("id"));
                line.setDirectional(map1.get("directional"));
                line.setInterval(map1.get("interval"));
                line.setType(map1.get("type"));
                line.setKilometer(map1.get("kilometer"));
                line.setRoute(map1.get("route"));
                line.setOnewayTime(map1.get("onewayTime"));
                line.setTimetable(map1.get("runtime"));
            }
//            if(map.get("stationList") instanceof List) {
//
//            }

            try {
//                String lineJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map.get("line"));
                String stationsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map.get("stationList"));
//                line = objectMapper.readValue(lineJson, Line.class);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Station.class);
                list = objectMapper.readValue(stationsJson, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                return "数据解析失败";
            }

            List<Station> stationList = new ArrayList<>();
            for(int i = 0; i < list.size(); i++) {
                Station station = new Station();
                //station.setId(list.get(i).getId());
                station = list.get(i);
                stationList.add(station);
            }
            return lineRepository.newLine(line, stationList);
        }
        //lineRepository.insertNewLine(line, stationInfoList);
        return "数据为空";
    }
}