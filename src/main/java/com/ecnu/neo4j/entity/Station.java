package com.ecnu.neo4j.entity;

import lombok.Data;

@Data
public class Station {
    private Long systemId;

    private String id;
    private String name;
    private String english;
}
