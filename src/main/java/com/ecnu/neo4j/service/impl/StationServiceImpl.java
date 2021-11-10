package com.ecnu.neo4j.service.impl;

import com.ecnu.neo4j.dao.StationRepository;
import com.ecnu.neo4j.dao.impl.StationRepositoryImpl;
import com.ecnu.neo4j.dto.*;
import com.ecnu.neo4j.entity.Station;
import com.ecnu.neo4j.service.StationService;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.ArrayList;
import java.util.List;

public class StationServiceImpl implements StationService {

    private StationRepository stationRepository = new StationRepositoryImpl();

    @Override
    public TestCase2 findStationInfo(String name) {
//        StationRepository stationRepository = new StationRepositoryImpl();
        TestCase2 testCase1 = new TestCase2();
        List<Station> stationList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();

        Path path = stationRepository.getStationInfo(name);

        for(Node node: path.nodes()) {
            Station station = new Station();
            station.setEnglish(node.get("english").asString());
            station.setId(node.get("id").asString());
            station.setName(node.get("name").asString());
            station.setSystemId(node.id());
            stationList.add(station);
        }
        stringList.add(path.toString());

        testCase1.setAlongStation(stationList);
        testCase1.setPath(stringList);
        return testCase1;
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
        return stationRepository.getNMostLine(num);
    }

    @Override
    public TestCase111 findCaseStation() {
        return stationRepository.getCaseStation();
    }

    @Override
    public TestCase13 findCrossStation(String line1, String line2) {
        TestCase13 testCase13 = new TestCase13();
        List<Station> list = new ArrayList<>();
        List<Node> nodeList = stationRepository.getCrossStation(line1, line2);
        for(Node node:nodeList) {
            Station station = new Station();
            station.setId(node.get("id").asString());
            station.setEnglish(node.get("english").asString());
            station.setName(node.get("name").asString());
            station.setSystemId(node.id());
            list.add(station);
        }
        testCase13.setCrossStation(list);
        return testCase13;
    }
}
