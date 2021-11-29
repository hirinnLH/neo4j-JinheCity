package com.ecnu.neo4j.dao.impl;

import com.ecnu.neo4j.dao.StationRepository;
import com.ecnu.neo4j.entity.Station;
import com.ecnu.neo4j.entity.StationWithoutEnglish;
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

    //环线N8这样判断的话无返回
    @Override
    public List<Map<String, Object>> getStationInfo(String name) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String cypher = "MATCH p=(n)-[*]->(s)\n" +
                "WHERE ALL(r in relationships(p) WHERE type(r)= $name) AND ANY(n in nodes(p) WHERE n.name CONTAINS \"始发站\")\n" +
                "UNWIND nodes(p) as node\n" +
                "RETURN DISTINCT properties(node) as prop\n\n";

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

    @Override
    public Path getDepartInfo(String name) {
        String cypher = "MATCH p=()-[*]->()\n" +
                "WHERE ALL(r in relationships(p) WHERE type(r) = $name) AND ANY(n in nodes(p) WHERE n.name CONTAINS \"(始发站)\")\n" +
                "RETURN p\n" +
                "ORDER BY length(p) DESC\n" +
                "LIMIT 1\n";
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("name", name));
        Record record = result.single();
        Value value = record.get("p");
        Path path = value.asPath();
        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

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
        String subwayCypher = "MATCH (n:Station)\n" +
                "WHERE n.name CONTAINS(\"地铁\")\n" +
                "RETURN distinct n.id, n.name\n";
        //查询始发站的语句
        String initialCypher = "MATCH (n:Station)\n" +
                "WHERE n.name CONTAINS(\"始发站\")\n" +
                "RETURN distinct n.id, n.name\n";
        //查询终点站的语句
        String endCypher = "MATCH (n:Station)\n" +
                "WHERE n.name CONTAINS(\"终点站\")\n" +
                "RETURN distinct n.id, n.name\n";
        Session session = DB.conn();

        //处理地铁站的信息
        List<StationWithoutEnglish> subwayName = new ArrayList<>();
        Result result = session.run(subwayCypher);
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            StationWithoutEnglish swe = new StationWithoutEnglish();
            swe.setId(record.get("n.id").asString());
            swe.setName(record.get("n.name").asString());
            subwayName.add(swe);
        }
        map.put("subwayName", subwayName);
        map.put("subwayCount", subwayName.size());

        //处理始发站信息
        List<StationWithoutEnglish> initialName = new ArrayList<>();
        result = session.run(initialCypher);
        recordList = result.list();
        for(Record record:recordList) {
            StationWithoutEnglish swe = new StationWithoutEnglish();
            swe.setId(record.get("n.id").asString());
            swe.setName(record.get("n.name").asString());
            initialName.add(swe);
        }
        map.put("initialName", initialName);
        map.put("initialCount", initialName.size());

        //处理终点站信息
        List<StationWithoutEnglish> endName = new ArrayList<>();
        result = session.run(endCypher);
        recordList = result.list();
        for(Record record:recordList) {
            StationWithoutEnglish swe = new StationWithoutEnglish();
            swe.setId(record.get("n.id").asString());
            swe.setName(record.get("n.name").asString());
            endName.add(swe);
        }
        map.put("endName", endName);
        map.put("endCount", endName.size());

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
        String cypher = "MATCH p=(n:Station)-[r]->(s:Station)\n" +
                "RETURN n.id, s.id, n.name, s.name, count(DISTINCT r.id) as num\n" +
                "ORDER BY num DESC\n" +
                "LIMIT $num\n";
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("num", num));
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Map<String, Object> map = new HashMap<>();
//            map.put("fromId", record.get("n.id").asString());
//            map.put("toId", record.get("s.id").asString());
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
