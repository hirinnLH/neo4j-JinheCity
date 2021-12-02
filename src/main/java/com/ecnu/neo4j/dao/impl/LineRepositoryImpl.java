package com.ecnu.neo4j.dao.impl;

import com.ecnu.neo4j.dao.LineRepository;
import com.ecnu.neo4j.dto.TestCase19;
import com.ecnu.neo4j.entity.Line;
import com.ecnu.neo4j.entity.Station;
import com.ecnu.neo4j.util.DB;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Path;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static org.neo4j.driver.Values.ofOffsetDateTime;
import static org.neo4j.driver.Values.parameters;

public class LineRepositoryImpl implements LineRepository {

    //需求1
    @Override
    public Map<String, Object> getLineInfo(String line_id) throws Exception {
        Session session = DB.conn();
        String cypher = "MATCH (n:Line {id:$line_id}) \n" +
                        "RETURN DISTINCT apoc.map.removeKey(n, 'name') AS props";
        Result result = session.run(cypher, parameters("line_id", line_id));
        Record record = result.single();
        Value value = record.get("props");
        Map<String,Object> map = value.asMap();

        session.close();
        DB.close();
        return map;
    }

    //需求3
    @Override
    public List<Map<String, Object>> getLineAlongStation(String stationName) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String cypher = "MATCH p=()-[r]->(s:Station {name:$name}) \n" +
                "RETURN s.id AS id,collect(type(r)) AS lines";

        Session session = DB.conn();
        Result result = session.run(cypher, parameters("name", stationName));
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Map<String, Object> map = new HashMap<>();
            Value value = record.get("id");
            Value value1 = record.get("lines");
            map.put("id", value.asString());
            map.put("lines", value1.asList());
            mapList.add(map);
        }
        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
    }

    //需求4
    @Override
    public Path getRouteWithLine(String start, String end, String lineId) {
        String cypher = "MATCH p=(n:Station {name:$start})-[*]->(s:Station {name:$end})\n" +
                "WHERE ALL(r in relationships(p) WHERE r.id = $lineId)\n" +
                "RETURN p\n";
        Path path = null;
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("start", start, "end", end, "lineId", lineId));
        if(result.hasNext()) {
            Record record = result.single();
            Value value = record.get("p");
            path = value.asPath();
        }

        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    //需求5
    public Path getShortestRouteByStation(String start, String end) {
        String cypher;
        Path path = null;
        if(StringUtils.isNumeric(start) && StringUtils.isNumeric(end)) {
            cypher = "MATCH p=shortestPath((n:Station {id:$start})-[*]->(s:Station {id:$end}))\n" +
                    "RETURN p";
        }
        else {
            cypher = "MATCH p=shortestPath((n:Station {name:$start})-[*]->(s:Station {name:$endS})) \n" +
                    "RETURN p\n" +
                    "ORDER BY length(p)\n" +
                    "LIMIT 1";
        }
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("start", start, "end", end));
        if(result.hasNext()) {
            Record record = result.single();
            Value value = record.get("p");
            path = value.asPath();
        }

        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

//    //需求5-1
//    @Override
//    public Path getShortestRouteByStationId(String startId, String endId) {
//        String cypher = "MATCH p=shortestPath((n:Station {id:$startId})-[*]->(s:Station {id:$endId}))\n" +
//                "RETURN p";
//        Session session = DB.conn();
//        Result result = session.run(cypher, parameters("startId", startId, "endId", endId));
//        Record record = result.single();
//        Value value = record.get("p");
//        Path path = value.asPath();
//
//        session.close();
//        try {
//            DB.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return path;
//    }
//
//    public Path getShortestRouteByStationName(String startName, String endName) {
//        String cypher = "MATCH p=shortestPath((n:Station {name:$startName})-[*..10]->(s:Station {name:$endName})) \n" +
//                "RETURN p";
//        Session session = DB.conn();
//        Result result = session.run(cypher, parameters("startName", startName, "endName", endName));
//        Record record = result.single();
//        Value value = record.get("p");
//        Path path = value.asPath();
//
//        session.close();
//        try {
//            DB.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return path;
//    }

    @Override
    public List<Map<String, Object>> getLineTypeCount() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String cypher = "MATCH (l:Line)\n" +
                "RETURN l.type as type, count(distinct l.id) as count\n" +
                "ORDER BY count DESC\n";
        Session session = DB.conn();
        Result result = session.run(cypher);
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", record.get("type").asString());
            map.put("count", record.get("count").asInt());
            mapList.add(map);
        }
        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
    }

//    @Override
//    public Integer getTransLineCount(String lineName) {
//        String cypher = "MATCH p=()-[*]-()\n" +
//                "WHERE ALL(r IN relationships(p) WHERE TYPE(r) = $lineName)\n" +
//                "UNWIND NODES(p) as n\n" +
//                "WITH n, (n)-[]-() as p1\n" +
//                "UNWIND p1 as pp1\n" +
//                "UNWIND relationships(pp1) as r1\n" +
//                "WITH r1\n" +
//                "WHERE TYPE(r1) <> $lineName\n" +
//                "RETURN count(DISTINCT TYPE(r1)) as num\n";
//        Session session = DB.conn();
//        Result result = session.run(cypher, parameters("lineName", lineName));
//        Record record = result.single();
//        Value value = record.get("num");
//        Integer count = value.asInt();
//
//        session.close();
//        try {
//            DB.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return count;
//    }
//
//    @Override
//    public List<String> getTransLineName(String lineName) {
//        List<String> list = new ArrayList<>();
//        String cypher = "MATCH p=()-[*]-()\n" +
//                "WHERE ALL(r IN relationships(p) WHERE TYPE(r) = $lineName)\n" +
//                "UNWIND NODES(p) as n\n" +
//                "WITH n, (n)-[]-() as p1\n" +
//                "UNWIND p1 as pp1\n" +
//                "UNWIND relationships(pp1) as r1\n" +
//                "WITH r1\n" +
//                "WHERE TYPE(r1) <> $lineName\n" +
//                "RETURN DISTINCT TYPE(r1) as name\n";
//        Session session = DB.conn();
//        Result result = session.run(cypher, parameters("lineName", lineName));
//        List<Record> recordList = result.list();
//        for(Record record:recordList) {
//            Value value = record.get("name");
//            list.add(value.asString());
//        }
//
//        session.close();
//        try {
//            DB.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    @Override
    public List<Map<String, Object>> getTransLineStation(String lineName) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String cypher = "MATCH p=()-[*]-()\n" +
                "WHERE ALL(r IN relationships(p) WHERE TYPE(r) = $lineName)\n" +
                "UNWIND NODES(p) as n\n" +
                "WITH n, (n)-[]-() as p1\n" +
                "UNWIND p1 as pp1\n" +
                "UNWIND relationships(pp1) as r1\n" +
                "WITH n, r1\n" +
                "WHERE TYPE(r1) <> $lineName\n" +
                "RETURN n.name as name, collect(DISTINCT TYPE(r1)) as type\n";
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("lineName", lineName));
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Map<String, Object> map = new HashMap<>();
            Value value = record.get("name");
            map.put("name", value.asString());
            value = record.get("type");
            map.put("type", value.asList());
            mapList.add(map);
        }

        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
    }

    @Override
    public List<Map<String, Object>> getNMostStationLine(int num) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String cypher = "MATCH p=()-[r]-()\n" +
                "UNWIND NODES(p) as n\n" +
                "RETURN type(r) as type, count(DISTINCT n) as num\n" +
                "ORDER BY num DESC\n" +
                "LIMIT $num\n";
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("num", num));
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", record.get("type").asString());
            map.put("count", record.get("num").asInt());
            mapList.add(map);
        }

        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
    }

    @Override
    public List<Map<String, Object>> getNMostTimeLine(int num) {
        String cypher = "MATCH p=()-[r]-()\n" +
                "RETURN type(r) as type, count(r.runtime) as time\n" +
                "ORDER BY time DESC\n" +
                "LIMIT $num";
        List<Map<String, Object>> mapList = new ArrayList<>();
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("num", num));
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", record.get("type").asString());
            map.put("time", record.get("time").asInt());
            mapList.add(map);
        }

        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
    }

    @Override
    public String newLine(Line line, List<TestCase19> stationList) {
        String lineId = line.getLine_id();
        String lineName = lineId + "路";
        String route = line.getRoute();
        //String onewayTime = line.getOnewayTime();
        String directional = line.getDirectional();
        String kilometer = line.getKilometer();
        String interval = line.getInterval();
        String type = line.getType();
        String timetable = line.getTimetable();

        int timeSum = 0;
        for(TestCase19 tc:stationList) {
            if(tc == stationList.get(0)) continue;
            timeSum+=Integer.parseInt(tc.getRuntime());
        }
        String onewayTime = String.valueOf(timeSum);
        line.setOnewayTime(onewayTime);

        //查看数据库中是否已经有该线路id
        String checkExist = "MATCH (l:Line {id:\"" + lineId +"\"})\n" +
                        "RETURN l";
        Session session = DB.conn();
        Result result = session.run(checkExist);
        if(result.hasNext()) {
            return "已存在该线路";
        }

        //-----Line入库-----
        String cypher = "CREATE (:Line {id:\"" + lineId + "\", directional:\"" + directional +
                "\", interval:\"" + interval + "\", kilometer:\"" + kilometer + "\", onewayTime:\"" + onewayTime +
                "\", route:\"" + route + "\", runtime:\"" + timetable + "\", type:\"" + type + "\"})";


        session.run(cypher);

        //-----关系节点入库-----
        if(directional.equals("TRUE")) {
            for(int i = 0; i < stationList.size() - 1; i++) {
                String relationNode = "CREATE (:StationStationRelation {from:\"" + stationList.get(i).getId() +
                        "\", line_id:\"" + lineId +  "\",relation:\"" + lineName  + "上行" + "\", runtime:\"" +
                        stationList.get(i+1).getRuntime() + "\",to:\"" + stationList.get(i+1).getId() + "\"})";
                session.run(relationNode);
            }
            for(int i = stationList.size()-1; i > 0; i--) {
                String relationNode = "CREATE (:StationStationRelation {from:\"" + stationList.get(i).getId() +
                        "\", line_id:\"" + lineId +  "\",relation:\"" + lineName + "下行" + "\", runtime:\"" +
                        stationList.get(i).getRuntime() + "\",to:\"" + stationList.get(i-1).getId() + "\"})";
                session.run(relationNode);
            }
        }
        else {
            for(int i = 0; i < stationList.size() - 1; i++) {
                String relationNode = "CREATE (:StationStationRelation {from:\"" + stationList.get(i).getId() + "\", line_id:\"" +
                        lineId +  "\",relation:\"" + lineName + "\", runtime:\"" + stationList.get(i+1).getRuntime() +
                        "\",to:\"" + stationList.get(i+1).getId() + "\"})";
                session.run(relationNode);
            }
        }

        //-----关系节点将Line节点的信息以属性存放-----
        String putProp = "MATCH (n:StationStationRelation),(m:Line) \n" +
                "WHERE n.line_id = m.id\n" +
                "SET n.directional=m.directional,n.interval=m.interval,n.kilometer=m.kilometer," +
                "n.onewayTime=m.onewayTime,n.route=m.route, n.type=m.type, n.timetable=m.runtime";
        session.run(putProp);

        //-----创建关系-----
        String createRelationship = "MATCH (n:Station),(m:StationStationRelation),(s:Station) \n" +
                "WHERE n.id = m.from AND m.to = s.id \n" +
                "CREATE (n)-[:StationStationRelation {relation:m.relation,id:m.line_id,directional:m.directional" +
                ",interval:m.interval,kilometer:m.kilometer,onewayTime:m.onewayTime,route:m.route,runtime:m.runtime" +
                ",timetable:m.timetable, type:m.type}] ->(s) ";
        session.run(createRelationship);

        //-----以具体线路作为关系名称-----
        String putRelType = "MATCH (n:Station)-[r:StationStationRelation]->(m:Station)\n" +
                "CALL apoc.create.relationship(n, r.relation, apoc.map.removeKey(PROPERTIES(r), 'relation'), m) YIELD rel\n" +
                "DELETE r";
        session.run(putRelType);

        //-----删除StationStationRelation节点-----
        String delRelationNode = "MATCH (n:StationStationRelation) DELETE n";
        session.run(delRelationNode);

        String checkInsert = "MATCH p=(n)-[*]->(s)\n" +
                "WHERE ALL(r in relationships(p) WHERE r.id = $id)\n" +
                "UNWIND nodes(p) as node\n" +
                "RETURN DISTINCT node.id as id\n";

        result =  session.run(checkInsert, parameters("id", lineId));
        List<Record> recordList = result.list();

        for(Record record:recordList) {
            boolean isIn = false;
            Value value = record.get("id");
            String resultId = value.asString();
//            Station station = new Station();
//            station.setEnglish(value.get("english").asString());
//            station.setName(value.get("name").asString());
//            station.setId(value.get("id").asString());
            for(TestCase19 s:stationList) {
                if(s.getId().equals(resultId)) {
                    isIn = true;
                    break;
                }
            }
            if(!isIn) {
                return "插入失败";
            }
        }

        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "插入成功";
    }

    @Override
    public String cancelLine(String lineId) {
        //delete line
        String cypher = "MATCH (n:Line {id:$lineId}) DELETE n";
        String relationCypher = "MATCH p=()-[r]-() \n" +
                "WHERE r.id = $lineId\n" +
                "DELETE r\n";

        Session session = DB.conn();
        session.run(cypher, parameters("lineId", lineId));
        session.run(relationCypher, parameters("lineId", lineId));

        //delete alone stations
        cypher = "MATCH (n:Station) WHERE NOT EXISTS((n)-[]-()) DELETE n";
        session.run(cypher);

        //search if the line still exists
        cypher = "MATCH (n:Line {id:$lineId}) RETURN n";
        relationCypher = "MATCH p=()-[]-() \n" +
                "WHERE ALL(r in relationships(p) WHERE r.id = $lineId) \n" +
                "RETURN p\n";
        Result result = session.run(cypher, parameters("lineId", lineId));
        Result result1 = session.run(relationCypher, parameters("lineId", lineId));
        if(!result.hasNext() && !result1.hasNext()) {
            return "删除成功";
        }
        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "删除失败";
    }
}
