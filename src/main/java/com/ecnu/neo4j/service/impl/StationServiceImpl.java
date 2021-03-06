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

    @Override
    public List<TestCase7> findDepartInfo(String name) {
        List<TestCase7> result = new ArrayList<>();
        Path path = stationRepository.getDepartInfo(name);
        String startTime = null;         //开始运行时间
        Integer interval = 0;           //每一班的间隔
        List<Integer> timeTable = new ArrayList<>();                //站与站之间的时间间隔
        boolean needToRev = false;              //标记22:00-1:00这样跨天的现象
        //得到该班次的开始运行时间和间隔
        for(Path.Segment segment: path) {
            startTime = segment.relationship().get("timetable").asString();
            interval = Integer.parseInt(segment.relationship().get("interval").asString());
            timeTable.add(Integer.parseInt(segment.relationship().get("runtime").asString()));
        }
        String[] border = startTime.split("-");     //结束时间和开始时间
        String[] nowCul = border[0].split(":");     //[0] = hour, [1] = minute
        String[] endTime = border[1].split(":");    //[0] = endhour, [1] = endminute
        Integer hour = Integer.parseInt(nowCul[0]);         //目前计算的小时
        Integer minute = Integer.parseInt(nowCul[1]);         //目前计算的分钟
        Integer endHour = Integer.parseInt(endTime[0]);
        Integer endMinute = Integer.parseInt(endTime[1]);

        //若是由跨天的现象，则-12小时计算
        if(endHour < hour) {
            endHour+=12;
            hour-=12;
            needToRev = true;
        }
        //途径站点
        for(Node node:path.nodes()) {
            TestCase7 testCase7 = new TestCase7();
            testCase7.setStationName(node.get("name").asString());
            result.add(testCase7);
            List<String> eachStationTime = new ArrayList<>();
            testCase7.setArrivedTime(eachStationTime);
        }


        for (int i = 0; i < result.size(); i++) {
            List<String> eachStationTime = result.get(i).getArrivedTime();
            if (minute >= 60) {
                hour += 1;
                minute -= 60;
            }
            if (hour >= 24) {
                hour -= 24;
            }

            if (minute / 10 == 0) {
                //处理跨天现象
                if(needToRev) {
                    eachStationTime.add(hour+12 + ":0" + minute);
                }
                else {
                    eachStationTime.add(hour + ":0" + minute);
                }
            } else {
                if(needToRev) {
                    eachStationTime.add(hour+12 + ":" + minute);
                    //stationTime.add(hour+12 + ":" + minute);
                }
                else {
                    eachStationTime.add(hour + ":" + minute);
                }
            }
            result.get(i).setArrivedTime(eachStationTime);
            if (i == result.size() - 1) {
                break;
            }
            minute += timeTable.get(i);
        }

        //查看下一班的最后一站是否还在运营时间内
        List<String> lastStationTime = result.get(result.size() - 1).getArrivedTime();
        String[] nowLastTime = lastStationTime.get(lastStationTime.size()-1).split(":");
        int nowLastHour = Integer.parseInt(nowLastTime[0]);
        int nowLastMinute = Integer.parseInt(nowLastTime[1]);
        nowLastMinute+=interval;
        if(needToRev) {
            nowLastHour-=12;
        }
        if (nowLastMinute >= 60) {
            nowLastHour += 1;
            nowLastMinute -= 60;
        }

        while(nowLastHour < endHour || ((nowLastHour == endHour) && (nowLastMinute <= endMinute))) {

            for(int i = 0; i < result.size(); i++) {
                List<String> eachStationTime = result.get(i).getArrivedTime();
                String baseCul = eachStationTime.get(eachStationTime.size()-1);
                String[] cul = baseCul.split(":");
                int minCul = Integer.parseInt(cul[1]);
                int hourCul = Integer.parseInt(cul[0]);
                minCul+=interval;
                if (minCul >= 60) {
                    hourCul += 1;
                    minCul -= 60;
                }
                if (hourCul >= 24) {
                    hourCul -= 24;
                }
                if (minCul / 10 == 0) {
                    eachStationTime.add(hourCul + ":0" + minCul);
                } else {
                    eachStationTime.add(hourCul + ":" + minCul);
                }
                if (i == result.size() - 1) {
                    //nowLastHour+=interval;
                    nowLastMinute+=interval;
                    if (nowLastMinute >= 60) {
                        nowLastHour += 1;
                        nowLastMinute -= 60;
                    }
//                    if (nowLastHour >= 24) {
//                        nowLastHour -= 24;
//                    }
                }
            }
        }
        return result;
    }

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
    public TestCase112 findSingleDirectStation(String lineId) {
        Map<String, List<StationWithoutEnglish>> map = stationRepository.getSingleDirectStation(lineId);
        List<StationWithoutEnglish> upGoing = map.get("upGoing");
        List<StationWithoutEnglish> downGoing = map.get("downGoing");

        List<StationWithoutEnglish> singleDirection = new ArrayList<>();
        boolean isIn = false;
        for(StationWithoutEnglish swe:upGoing) {
            for(StationWithoutEnglish swe2:downGoing) {
                if(swe.getName().equals(swe2.getName())) {
                    isIn = true;
                    break;
                }
            }
            if(!isIn) {
                singleDirection.add(swe);
            }
            isIn = false;
        }

        isIn = false;
        for(StationWithoutEnglish swe:downGoing) {
            for(StationWithoutEnglish swe2:upGoing) {
                if(swe.getName().equals(swe2.getName())) {
                    isIn = true;
                    break;
                }
            }
            if(!isIn) {
                singleDirection.add(swe);
            }
            isIn = false;
        }
        TestCase112 testCase112 = new TestCase112();
        testCase112.setSingleDir(singleDirection);
        testCase112.setSingleDirCnt(singleDirection.size());
        return testCase112;
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
