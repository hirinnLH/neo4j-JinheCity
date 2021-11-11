package com.ecnu.neo4j.dao.impl;

import com.ecnu.neo4j.dao.LineRepository;
import com.ecnu.neo4j.util.DB;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Path getRouteWithLineTen(String start, String end) {
        String cypher = "MATCH p=(n:Station {name:$start})-[:`10路上行`|`10路下行`*..10]->(s:Station {name:$end}) \n" +
                "RETURN p";

        Session session = DB.conn();
        Result result = session.run(cypher, parameters("start", start, "end", end));
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
    public Path getShortestRouteByStationId(String startId, String endId) {
        String cypher = "MATCH p=shortestPath((n:Station {id:$startId})-[*]->(s:Station {id:$endId}))\n" +
                "RETURN p";
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("startId", startId, "endId", endId));
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

    public Path getShortestRouteByStationName(String startName, String endName) {
        String cypher = "MATCH p=shortestPath((n:Station {name:$startName})-[*..10]->(s:Station {name:$endName})) \n" +
                "RETURN p";
        Session session = DB.conn();
        Result result = session.run(cypher, parameters("startName", startName, "endName", endName));
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
        return mapList;
    }
}
