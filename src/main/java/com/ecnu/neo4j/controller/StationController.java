package com.ecnu.neo4j.controller;

import com.ecnu.neo4j.dto.TestCase10;
import com.ecnu.neo4j.dto.TestCase2;
import com.ecnu.neo4j.dto.TestCase7;
import com.ecnu.neo4j.service.StationService;
import com.ecnu.neo4j.service.impl.StationServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class StationController {

    private StationService service = new StationServiceImpl();

    public Object listStationInfo(HttpServletRequest request) {
        String name = String.valueOf(request.getParameter("name"));
        TestCase2 testCase1 = service.findStationInfo(name);
        return testCase1;
    }

//    public Object listDepartInfo(HttpServletRequest request) {
//        String name = String.valueOf(request.getParameter("name"));
//        TestCase7 testCase7 = service.findDepartInfo(name);
//        return testCase7;
//    }

    public Object listNMostLine(HttpServletRequest request) {
        String num = String.valueOf(request.getParameter("num"));
        List<TestCase10> testCase10 = service.findNMostLine(Integer.parseInt(num));
        return testCase10;
    }
}
