import com.ecnu.neo4j.dto.TestCase2;
import com.ecnu.neo4j.dto.TestCase7;
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

//    @Test
//    public void stationDepartTest() {
//        TestCase7 testCase7 = stationService.findDepartInfo("N8路");
//        for(String str:testCase7.getStationList()) {
//            System.out.print(str + " ");
//        }
//        System.out.println();
//        for(List<String> timeTable: testCase7.getTime()) {
//            for(String time:timeTable) {
//                System.out.print(time + " ");
//            }
//            System.out.println();
//        }
//    }

    @Test
    public void stationDepartTest() {
        List<TestCase7> testCase7List = stationService.findDepartInfo("N8路");
        for(TestCase7 testCase7:testCase7List) {
            System.out.print(testCase7.getStationName() + ": ");
            for(String str:testCase7.getArrivedTime()) {
                System.out.print(str + " ");
            }
            System.out.println();
            //System.out.println(testCase7.getArrivedTime());
        }
    }

}
