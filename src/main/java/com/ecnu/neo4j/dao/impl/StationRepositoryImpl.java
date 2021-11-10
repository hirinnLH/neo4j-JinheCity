package com.ecnu.neo4j.dao.impl;

import com.ecnu.neo4j.dao.StationRepository;
import com.ecnu.neo4j.dto.TestCase10;
import com.ecnu.neo4j.dto.TestCase111;
import com.ecnu.neo4j.entity.Station;
import com.ecnu.neo4j.util.DB;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class StationRepositoryImpl implements StationRepository {

    @Override
    public Path getStationInfo(String name) {

        String cypher = "MATCH p=(n)-[r:`2路上行`*]->(s)\n" +
                "WHERE NOT EXISTS (()-[]->(n)) AND NOT EXISTS ((s)-[]->())\n" +
                "RETURN DISTINCT p\n";

        //连接数据库，请求数据
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
    public List<TestCase10> getNMostLine(int num) {
        List<TestCase10> returnList = new ArrayList<>();
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
            TestCase10 testCase10 = new TestCase10();
            testCase10.setStationName(record.get("name").asString());
            testCase10.setLineNum(record.get("size(lines)").asInt());
            testCase10.setLineName(record.get("lines").asList());
            returnList.add(testCase10);
        }
        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    @Override
    public TestCase111 getCaseStation() {
        TestCase111 testCase111 = new TestCase111();
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
        List<Record> recordList = result.list();
        for(Record record:recordList) {
            Value value = record.get("count");
            testCase111.setSubwayStation(value.asInt());
        }

        //处理始发站信息
        result = session.run(initialCypher);
        recordList = result.list();
        for(Record record:recordList) {
            Value value = record.get("count");
            testCase111.setInitialStation(value.asInt());
        }

        //处理终点站信息
        result = session.run(endCypher);
        recordList = result.list();
        for(Record record:recordList) {
            Value value = record.get("count");
            testCase111.setEndStation(value.asInt());
        }

        session.close();
        try {
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testCase111;
    }

    @Override
    public List<Node> getCrossStation(String line1, String line2) {
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
        return stationList;
    }
}
