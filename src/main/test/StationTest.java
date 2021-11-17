import com.ecnu.neo4j.dto.TestCase2;
import com.ecnu.neo4j.service.StationService;
import com.ecnu.neo4j.service.impl.StationServiceImpl;
import org.junit.Test;

import java.util.List;


public class StationTest {

    StationService stationService = new StationServiceImpl();

    @Test
    public void stationInfoTest() {
        List<TestCase2> testCase2List = stationService.findStationInfo("2路上行");
        for(TestCase2 testCase2:testCase2List) {
            System.out.println("id: " + testCase2.getId());
            System.out.println("english: " + testCase2.getEnglish());
            System.out.println("name: " + testCase2.getName());
            System.out.println();
        }
    }


}
