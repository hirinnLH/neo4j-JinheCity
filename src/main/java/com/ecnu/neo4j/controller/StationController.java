package com.ecnu.neo4j.controller;

import com.ecnu.neo4j.dto.*;
import com.ecnu.neo4j.service.StationService;
import com.ecnu.neo4j.service.impl.StationServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class StationController {

    private StationService service = new StationServiceImpl();

    public Object listStationInfo(HttpServletRequest request) {
        String name = String.valueOf(request.getParameter("name"));
        return service.findStationInfo(name);

    }

//    public Object listDepartInfo(HttpServletRequest request) {
//        String name = String.valueOf(request.getParameter("name"));
//        TestCase7 testCase7 = service.findDepartInfo(name);
//        return testCase7;
//    }

    public Object listNMostLine(HttpServletRequest request) {
        String num = String.valueOf(request.getParameter("num"));
        return service.findNMostLine(Integer.parseInt(num));
    }

    public Object listCaseStation(HttpServletRequest request) {
        return service.findCaseStation();

    }

    public Object listCrossStation(HttpServletRequest request) {
        String line1 = String.valueOf(request.getParameter("line1"));
        String line2 = String.valueOf(request.getParameter("line2"));
        return service.findCrossStation(line1, line2);
    }
}
