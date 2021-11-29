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

    @Test
    public void insertLineTest() {
        String json = "{\n" +
                "    \"line\": {\n" +
                "        \"id\": \"3\",\n" +
                "        \"route\": \"金和客运站-花明公交站\",\n" +
                "        \"onewayTime\": \"约52分\",\n" +
                "        \"directional\": \"TRUE\",\n" +
                "        \"kilometer\": \"15\",\n" +
                "        \"runtime\": \"6:00-23:59\",\n" +
                "        \"interval\": \"5\",\n" +
                "        \"type\": \"干线\"\n" +
                "    },\n" +
                "    \"stationList\": [\n" +
                "        {\n" +
                "            \"name\": \"金河客运站\",\n" +
                "            \"english\": \"JinHeKeYun\",\n" +
                "            \"id\": \"21460\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"花明公交站\",\n" +
                "            \"english\": \"HuaMing GJZ\",\n" +
                "            \"id\": \"27680\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        System.out.println(service.addLine(json));
    }
}
