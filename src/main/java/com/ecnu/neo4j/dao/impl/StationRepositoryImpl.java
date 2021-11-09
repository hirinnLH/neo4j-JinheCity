package com.ecnu.neo4j.dao.impl;

import com.ecnu.neo4j.dao.StationRepository;
import com.ecnu.neo4j.dto.TestCase10;
import com.ecnu.neo4j.util.DB;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
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
        return returnList;
    }
}
