package com.ecnu.neo4j.entity;

import lombok.Data;

@Data
public class Line {

    //private Long systemId;
    private String route;
    private String onewayTime;
    private String directional;
    private String kilometer;
    private String name;
    private String interval;
    private String line_id;
    private String type;
    private String timetable;
}
