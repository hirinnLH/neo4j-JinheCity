package com.ecnu.neo4j.service.impl;

import com.ecnu.neo4j.dao.StationRepository;
import com.ecnu.neo4j.dao.impl.StationRepositoryImpl;
import com.ecnu.neo4j.dto.*;
import com.ecnu.neo4j.entity.Station;
import com.ecnu.neo4j.entity.StationWithoutEnglish;
import com.ecnu.neo4j.service.StationService;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StationServiceImpl implements StationService {

    private StationRepository stationRepository = new StationRepositoryImpl();

    @Override
    public List<TestCase2> findStationInfo(String name) {
        List<TestCase2> list = new ArrayList<>();
        List<Map<String, Object>> mapList = stationRepository.getStationInfo(name);

        for(Map<String, Object> map:mapList) {
            TestCase2 testCase2 = new TestCase2();
            testCase2.setEnglish(map.get("english").toString());
            testCase2.setId(map.get("id").toString());
            testCase2.setName(map.get("name").toString());
            //station.setSystemId(map.id());
            list.add(testCase2);
        }

        return list;
    }

//    @Override
//    public TestCase7 findDepartInfo(String name) {
//        List<Path> pathList = stationRepository.getDepartInfo(name);
//        String startTime = null;        //开始运行时间
//        String endTime;          //结束运营时间
//        Integer interval = 0;           //每一班的间隔
//        List<String> stationNames = new ArrayList<>();
//        List<List<String>> stationDepart = new ArrayList<>();
//        //得到该班次的开始运行时间和间隔
//        for(Path.Segment segment: pathList.get(0)) {
//            startTime = segment.relationship().get("timetable").asString();
//            interval = segment.relationship().get("interval").asInt();
//        }
//        endTime = startTime.substring(6);               //结束时间
//        startTime = startTime.substring(0,5);           //开始时间
//        Integer hour = Integer.parseInt(startTime.substring(0,2));         //目前计算的小时
//        Integer minute = Integer.parseInt(startTime.substring(3));         //目前计算的分钟
//        Integer endHour = Integer.parseInt(endTime.substring(0,2));
//        Integer endMinute = Integer.parseInt(endTime.substring(3));
//        if(endHour < hour) {
//            endHour+=24;
//        }
//        //途径站点
//        for(Path path:pathList) {
//            for(Node node:path.nodes()) {
//                stationNames.add(node.get("name").asString());
//            }
//        }
//        while(hour < endHour || ((hour == endHour) && (minute < endMinute))) {
//            List<String> stationTime = new ArrayList<>();
//            for (int i = 0; i < stationNames.size(); i++) {
//                if (minute > 60) {
//                    hour += 1;
//                    minute -= 60;
//                }
//                if (hour > 24) {
//                    hour -= 24;
//                }
//                stationTime.add(hour + ":" + minute);
//            }
//            stationDepart.add(stationTime);
//            minute+=interval;
//            if (minute > 60) {
//                hour += 1;
//                minute -= 60;
//            }
//            if (hour > 24) {
//                hour -= 24;
//            }
//        }
//        TestCase7 testCase7 = new TestCase7();
//        testCase7.setStationList(stationNames);
//        testCase7.setTime(stationDepart);
//        return testCase7;
//    }

    @Override
    public List<TestCase10> findNMostLine(int num) {
        List<Map<String, Object>> mapList = stationRepository.getNMostLine(num);
        List<TestCase10> testCase10List = new ArrayList<>();
        for(Map<String, Object> map:mapList) {
            TestCase10 testCase10 = new TestCase10();
            testCase10.setStationName((String) map.get("name"));
            testCase10.setLineNum((Integer) map.get("size"));
            testCase10.setLineName((List<Object>) map.get("lines"));
            testCase10List.add(testCase10);
        }
        return testCase10List;
    }

    @Override
    public TestCase111 findCaseStation() {
        Map<String, Object> map = stationRepository.getCaseStation();
        TestCase111 testCase111 = new TestCase111();
        testCase111.setEndStation((Integer) map.get("endCount"));
        testCase111.setInitialStation((Integer) map.get("initialCount"));
        testCase111.setSubwayStation((Integer) map.get("subwayCount"));
        testCase111.setSubwayName((List<StationWithoutEnglish>) map.get("subwayName"));
        testCase111.setInitialName((List<StationWithoutEnglish>) map.get("initialName"));
        testCase111.setEndName((List<StationWithoutEnglish>) map.get("endName"));
        return testCase111;
    }

    @Override
    public TestCase13 findCrossStation(String line1, String line2) {
        TestCase13 testCase13 = new TestCase13();
        List<Station> list = new ArrayList<>();
        Map<String, Object> map = stationRepository.getCrossStation(line1, line2);
        for(Node node:(List<Node>)map.get("stationList")) {
            Station station = new Station();
            station.setId(node.get("id").asString());
            station.setEnglish(node.get("english").asString());
            station.setName(node.get("name").asString());
            station.setSystemId(node.id());
            list.add(station);
        }
        testCase13.setCrossStation(list);
        testCase13.setCount((int)map.get("count"));
        return testCase13;
    }

    @Override
    public List<TestCase15> findNMostLineStation(int num) {
        List<Map<String, Object>> mapList = stationRepository.getNMostLineStation(num);
        List<TestCase15> list = new ArrayList<>();
        for(Map<String, Object> map:mapList) {
            TestCase15 testCase15 = new TestCase15();
//            testCase15.setFromPlatform(map.get("fromId").toString());
//            testCase15.setToPlatform(map.get("toId").toString());
            testCase15.setToStation(map.get("toName").toString());
            testCase15.setFromStation(map.get("fromName").toString());
            testCase15.setLineCount((Integer) map.get("count"));
            list.add(testCase15);
        }
        return list;
    }
}
