import com.ecnu.neo4j.dao.LineRepository;
import com.ecnu.neo4j.dao.impl.LineRepositoryImpl;
import com.ecnu.neo4j.dto.TestCase4;
import com.ecnu.neo4j.dto.TestCase5;
import com.ecnu.neo4j.service.LineService;
import com.ecnu.neo4j.service.impl.LineServiceImpl;
import org.junit.Test;


public class LineTest {

    LineService service = new LineServiceImpl();
    LineRepository repository = new LineRepositoryImpl();

    @Test
    public void getRouteWithLineTest() {
//        Path path = repository.getRouteWithLine("大悦城", "小吃街", "5");
//        System.out.println(path);
        TestCase4 testCase4 = service.findRouteWithLine("大悦城", "小吃街", "5");
        if(testCase4 == null) {
            System.out.println("null");
        }
        else {
            System.out.println(testCase4.getAlongStation());
            System.out.println(testCase4.getDirection());
            System.out.println(testCase4.getRuntime());
        }
    }

    @Test
    public void getShortestRouteByStation() {
        TestCase5 testCase5 = service.findShortestRouteByStation("大悦城","小吃街");
        System.out.println(testCase5.getAlongStation());
    }
}
