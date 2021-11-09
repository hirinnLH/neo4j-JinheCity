package com.ecnu.neo4j.controller;

import com.ecnu.neo4j.dto.*;
import com.ecnu.neo4j.service.LineService;
import com.ecnu.neo4j.service.impl.LineServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class LineController {

    private LineService service = new LineServiceImpl();

    public Object listLineInfo(HttpServletRequest request) {
        //获取请求参数 LineController/listLineInfo?line_id=30
        String line_id = String.valueOf(request.getParameter("line_id"));
        //调用service的方法
        //LineService service = new LineServiceImpl();
        TestCase1 lineInfoDto = service.findLineInfo(line_id);
        return lineInfoDto;
    }

    public Object listAlongLine(HttpServletRequest request) {
        String name = String.valueOf(request.getParameter("name"));
        List<TestCase3> result = service.findAlongLine(name);
        return result;
    }

    public Object listRouteWithLineTen(HttpServletRequest request) {
        String start = String.valueOf(request.getParameter("start"));
        String end = String.valueOf(request.getParameter("end"));
        TestCase4 testCase4 = service.findRouteWithLineTen(start, end);
        return testCase4;
    }

    public Object listShortestRouteByStationId(HttpServletRequest request) {
        String start = String.valueOf(request.getParameter("start"));
        String end = String.valueOf(request.getParameter("end"));
        TestCase51 testCase51 = service.findShortestRouteByStationId(start, end);
        return testCase51;
    }

    public Object listShortestRouteByStationName(HttpServletRequest request) {
        String start = String.valueOf(request.getParameter("start"));
        String end = String.valueOf(request.getParameter("end"));
        TestCase52 testCase52 = service.findShortestRouteByStationName(start, end);
        return testCase52;
    }
}
