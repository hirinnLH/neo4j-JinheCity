package com.ecnu.neo4j.controller;

import com.ecnu.neo4j.service.LineService;
import com.ecnu.neo4j.service.impl.LineServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class LineController {

    private LineService service = new LineServiceImpl();

    //需求1
    public Object listLineInfo(HttpServletRequest request) {
        //获取请求参数 LineController/listLineInfo?line_id=30
        String line_id = String.valueOf(request.getParameter("line_id"));
        //调用service的方法
        //LineService service = new LineServiceImpl();
        return service.findLineInfo(line_id);
    }

    //需求3
    public Object listAlongLine(HttpServletRequest request) {
        String name = String.valueOf(request.getParameter("name"));
        return service.findAlongLine(name);
    }

    //需求4
    public Object listRouteWithLineTen(HttpServletRequest request) {
        String start = String.valueOf(request.getParameter("start"));
        String end = String.valueOf(request.getParameter("end"));
        return service.findRouteWithLineTen(start, end);

    }

    //需求5-1
    public Object listShortestRouteByStationId(HttpServletRequest request) {
        String start = String.valueOf(request.getParameter("start"));
        String end = String.valueOf(request.getParameter("end"));
        return service.findShortestRouteByStationId(start, end);

    }

    //需求5-2
    public Object listShortestRouteByStationName(HttpServletRequest request) {
        String start = String.valueOf(request.getParameter("start"));
        String end = String.valueOf(request.getParameter("end"));
        return service.findShortestRouteByStationName(start, end);

    }

    //需求12
    public Object listLineTypeCount(HttpServletRequest request) {
        return service.findLineTypeCount();
    }

    //需求14-1
    public Object listTransLineCount(HttpServletRequest request) {
        String lineName = String.valueOf(request.getParameter("lineName"));
        return service.findTransLintCount(lineName);
    }

    //需求14-2
    public Object listTransLineName(HttpServletRequest request) {
        String lineName = String.valueOf(request.getParameter("lineName"));
        return service.findTransLineName(lineName);
    }

    //需求14-3
    public Object listTransLineStation(HttpServletRequest request) {
        String lineName = String.valueOf(request.getParameter("lineName"));
        return service.findTransLineStation(lineName);
    }

    //需求16
    public Object listNMostStationLine(HttpServletRequest request) {
        String num = String.valueOf(request.getParameter("num"));
        return service.findNMostStationLine(Integer.parseInt(num));
    }

    //需求17
    public Object listNMostTimeLine(HttpServletRequest request) {
        String num = String.valueOf(request.getParameter("num"));
        return service.findNMostTimeLine(Integer.parseInt(num));
    }
}
