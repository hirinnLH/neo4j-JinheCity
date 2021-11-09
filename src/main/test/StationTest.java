import com.ecnu.neo4j.dao.StationRepository;
import com.ecnu.neo4j.dao.impl.StationRepositoryImpl;
import com.ecnu.neo4j.dto.TestCase2;
import com.ecnu.neo4j.service.StationService;
import com.ecnu.neo4j.service.impl.StationServiceImpl;
import org.junit.Test;
import org.neo4j.driver.types.Path;

import java.util.List;

public class StationTest {
    @Test
    public void stationInfoTest() {
        StationService stationService = new StationServiceImpl();
        TestCase2 testCase1 = stationService.findStationInfo("2路上行");
        System.out.println("Path: " + testCase1.getPath());
        System.out.println("Station: " + testCase1.getAlongStation());
    }

//    @Test
//    public void departInfoTest() {
//        StationRepository stationRepository = new StationRepositoryImpl();
//        List<Path> pathList = stationRepository.getDepartInfo("N8路");
//        for(Path path:pathList) {
//            int i = 0;
//            System.out.println(i);
//            System.out.println(path);
//        }
//    }
}
