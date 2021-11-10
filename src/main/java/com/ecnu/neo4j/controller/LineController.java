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
        return service.findLineInfo(line_id);
    }

    public Object listAlongLine(HttpServletRequest request) {
        String name = String.valueOf(request.getParameter("name"));
        return service.findAlongLine(name);
    }

    public Object listRouteWithLineTen(HttpServletRequest request) {
        String start = String.valueOf(request.getParameter("start"));
        String end = String.valueOf(request.getParameter("end"));
        return service.findRouteWithLineTen(start, end);

    }

    public Object listShortestRouteByStationId(HttpServletRequest request) {
        String start = String.valueOf(request.getParameter("start"));
        String end = String.valueOf(request.getParameter("end"));
        return service.findShortestRouteByStationId(start, end);

    }

    public Object listShortestRouteByStationName(HttpServletRequest request) {
        String start = String.valueOf(request.getParameter("start"));
        String end = String.valueOf(request.getParameter("end"));
        return service.findShortestRouteByStationName(start, end);

    }

    public Object listLineTypeCount(HttpServletRequest request) {
        return service.findLineTypeCount();
    }
}
