package com.ecnu.neo4j.dao.impl;

import com.ecnu.neo4j.dao.StationRepository;
import com.ecnu.neo4j.util.DB;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

public class StationRepositoryImpl implements StationRepository {

    @Override
    public List<Map<String, Object>> getStationInfo(String name) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String cypher = "MATCH p=(n)-[*]->(s)\n" +
                "WHERE ALL(r in relationships(p) WHERE type(r)=$name) AND NOT EXISTS (()-[]->(n)) AND NOT EXISTS ((s)-[]->())\n" +
                "UNWIND nodes(p) as node\n" +
                "RETURN DISTINCT properties(node) as prop\n";

        //连接数据库，请求数据
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("name", name));
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Value value = record.get("prop");
            Map<String, Object> map = value.asMap();
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
//    public List<Path> getDepartInfo(String name) {
//        List<Path> pathList = new ArrayList<>();
////        String cypher = "MATCH p=(n:Station)-[r:`$name`*]->(s:Station)\n" +
////                "WHERE NOT EXISTS ((s)-[]->())\n" +
////                "RETURN p\n";
//        String cypher = "MATCH p=(n)-[*]->(s)\n" +
//                "WITH p, relationships(p) as r\n" +
//                "WHERE NOT EXISTS ((s)-[]->())\n" +
//                "UNWIND r as rel\n" +
//                "WITH p as path, type(rel) = $name as ty\n" +
//                "RETURN path\n";
//        Session session = DB.conn();
//        Result result = session.run(cypher, parameters("name", name));
//        List<Record> recordList = result.list();
//        for(Record record:recordList) {
//            Value value = record.get("p");
//            Path path = value.asPath();
//            pathList.add(path);
//        }
//        session.close();
//        try {
//            DB.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return pathList;
//    }

    @Override
    public List<Map<String, Object>> getNMostLine(int num) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String cypher = "MATCH p=()-[r]->() \n" +
                "UNWIND nodes(p) as no\n" +
                "WITH no, collect(distinct(type(r))) AS lines\n" +
                "UNWIND lines as line\n" +
                "RETURN DISTINCT no.name as name, size(lines), lines\n" +
                "ORDER BY size(lines) DESC\n" +
                "LIMIT $num \n";
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("num", num));
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", record.get("name").asString());
            map.put("size", record.get("size(lines)").asInt());
            map.put("lines", record.get("lines").asList());
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
    public Map<String, Object> getCaseStation() {
        Map<String, Object> map = new HashMap<>();
        //查询地铁站的语句
        String subwayCypher = "MATCH (n:Station) \n" +
                "WHERE n.name CONTAINS(\"地铁\") \n" +
                "RETURN count(DISTINCT n.name) as count\n";
        //查询始发站的语句
        String initialCypher = "MATCH (n:Station) \n" +
                "WHERE n.name CONTAINS(\"始发站\") \n" +
                "RETURN count(DISTINCT n.name) as count\n";
        //查询终点站的语句
        String endCypher = "MATCH (n:Station) \n" +
                "WHERE n.name CONTAINS(\"终点站\") \n" +
                "RETURN count(DISTINCT n.name) as count\n";
        Session session = DB.conn();

        //处理地铁站的信息
        Result result = session.run(subwayCypher);
        Record record = result.single();
        map.put("subwayCount", record.get("count").asInt());

        //处理始发站信息
        result = session.run(initialCypher);
        record = result.single();
        map.put("initialCount", record.get("count").asInt());

        //处理终点站信息
        result = session.run(endCypher);
        record = result.single();
        map.put("endCount", record.get("count").asInt());

        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, Object> getCrossStation(String line1, String line2) {
        List<Node> stationList = new ArrayList<>();
        String cypher = "MATCH p=()-[*]-()\n" +
                "WHERE ALL(r IN relationships(p) WHERE TYPE(r) = $line1)\n" +
                "UNWIND NODES(p) as n\n" +
                "WITH n, (n)-[]-() as p1\n" +
                "UNWIND p1 as pp1\n" +
                "WITH n, pp1\n" +
                "WHERE ALL(r1 IN relationships(pp1) WHERE type(r1) = $line2)\n" +
                "RETURN DISTINCT n\n";
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("line1", line1, "line2", line2));
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Node station = record.get("n").asNode();
            stationList.add(station);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stationList", stationList);
        map.put("count", stationList.size());

        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> getNMostLineStation(int num) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String cypher = "MATCH p=(n)-[r]-(s)\n" +
                "RETURN n.id, s.id, n.name, s.name, count(DISTINCT r.id) as num\n" +
                "ORDER BY num DESC\n" +
                "LIMIT $num\n";
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("num", num));
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Map<String, Object> map = new HashMap<>();
            map.put("fromId", record.get("n.id").asString());
            map.put("toId", record.get("s.id").asString());
            map.put("fromName", record.get("n.name").asString());
            map.put("toName", record.get("s.name").asString());
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
}
