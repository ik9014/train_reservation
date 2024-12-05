/**********************************************************
 공공데이터포털에 있는 Open API를 활용하여 기차시간표를 조회한다
 포털에서의 데이터명: "국토교통부_(TAGO)_열차정보"

 4번 API: 지역코드와 지역명
 3번 API: 역코드와 역명
 2번 API: 차량코드와 차량종류
 1번 API: 열차 시간표 조회

 1번 API를 호출하려면 먼저 2,3번 API를 호출해야 하고
 3번 API를 호출하려면 먼저 4번 API를 호출해야 한다
 ***********************************************************/

package sogong.train.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import sogong.train.info.TrainInfo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLEncoder;

@Controller
public class TrainAPIController {

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 4번 API
    // API를 호출하여 모든 지역코드와 지역명을 가져온다
    // 데이터를 Map<Integer, String> 형식으로 반환
    @GetMapping("/trainAPI/4")
    @ResponseBody
    public Map<Integer, String> getCityCodes() {
        String URL = "http://apis.data.go.kr/1613000/TrainInfoService/" +
                "getCtyCodeList?serviceKey=%s&numOfRows=100&_type=json";
        Map<Integer, String> cityCodeMap = new HashMap<>();

        try {
            // API 서버에서 반환한 응답 데이터를 문자열로 저장
            URI uri = new URI(String.format(URL, apiKey));
            String apiResponse = restTemplate.getForObject(uri, String.class);

            // 문자열 형식의 JSON 데이터를 JsonNode 트리 구조로 변환
            JsonNode rootNode = objectMapper.readTree(apiResponse);

            // JSON 데이터에서 item 배열 노드를 가져온다
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            // 지역코드와 지역명을 찾아서 저장한다
            if (itemsNode.isArray()) {
                for (JsonNode item : itemsNode) {
                    Integer cityCode = item.path("citycode").asInt();
                    String cityName = item.path("cityname").asText();
                    cityCodeMap.put(cityCode, cityName);
                }
            }
            // 예외 처리
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response", e);
        }
        return cityCodeMap;
    }


    // 3번 API
    // API를 호출하여 모든 역코드와 역명을 가져온다
    // 데이터를 Map<String, String> 형식으로 반환
    @GetMapping("/trainAPI/3")
    @ResponseBody
    public Map<String, String> getStationCodes() {
        String URL = "http://apis.data.go.kr/1613000/TrainInfoService/" +
                "getCtyAcctoTrainSttnList?serviceKey=%s&numOfRows=100&" +
                "_type=json&cityCode=%d";
        Map<String, String> stationCodeMap = new HashMap<>();
        Map<Integer, String> cityCodeMap = getCityCodes();

        try {
            // 각 지역 코드를 순회하면서 API 호출을 수행
            for (Integer cityCode : cityCodeMap.keySet()) {
                // API 서버에서 반환한 응답 데이터를 문자열로 저장
                URI uri = new URI(String.format(URL, apiKey, cityCode));
                String apiResponse = restTemplate.getForObject(uri, String.class);

                // API 서버에서 반환한 응답 데이터를 문자열로 저장
                JsonNode rootNode = objectMapper.readTree(apiResponse);

                // JSON 데이터에서 item 배열 노드를 가져온다
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

                // 역코드와 역명을 찾아서 저장한다
                if (itemsNode.isArray()) {
                    for (JsonNode item : itemsNode) {
                        String nodeId = item.path("nodeid").asText();
                        String nodeName = item.path("nodename").asText();
                        stationCodeMap.put(nodeId, nodeName);
                    }
                }
            }

            // 예외처리
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON response", e);
        }
        return stationCodeMap;
    }


    // 2번 API
    // API를 호출하여 모든 차량코드와 차량종류 이름을 가져온다
    // 데이터를 Map<String, String> 형식으로 반환
    @GetMapping("/trainAPI/2")
    @ResponseBody
    public Map<String, String> getTrainGrades() {
        String URL = "http://apis.data.go.kr/1613000/TrainInfoService/" +
                "getVhcleKndList?serviceKey=%s&numOfRows=100&_type=json";
        Map<String, String> trainGradeMap = new HashMap<>();

        try {
            // API 서버에서 반환한 응답 데이터를 문자열로 저장
            URI uri = new URI(String.format(URL, apiKey));
            String apiResponse = restTemplate.getForObject(uri, String.class);

            // 문자열 형식의 JSON 데이터를 JsonNode 트리 구조로 변환
            JsonNode rootNode = objectMapper.readTree(apiResponse);

            // JSON 데이터에서 item 배열 노드를 가져온다
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            // 차량코드와 차량종류 이름을 찾아서 저장한다
            if (itemsNode.isArray()) {
                for (JsonNode item : itemsNode) {
                    String gradeCode = item.path("vehiclekndid").asText();
                    String gradeName = item.path("vehiclekndnm").asText();
                    trainGradeMap.put(gradeCode, gradeName);
                }
            }
        }

        // 예외 처리
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON response", e);
        }
        return trainGradeMap;
    }


    // 열차 시간표를 가져오는 기능을 수행한다
    // 출발역, 도착역, 출발 날짜, 열차 종류를 입력받는다
    // 데이터를 List<Map<String, String>> 형식으로 반환
    public List<Map<String, String>> getTrainSchedule
    (String depPlaceId,
     String arrPlaceId,
     String depPlandTime,
     String trainGrade) {
        List<Map<String, String>> trainSchedule = new ArrayList<>();
        String[] trainCodeArr = new String[]{};
        String URL = "http://apis.data.go.kr/1613000/" +
                "TrainInfoService/getStrtpntAlocFndTrainInfo?" +
                "serviceKey=%s&depPlaceId=%s&arrPlaceId=%s" +
                "&depPlandTime=%s&trainGradeCode=%s&numOfRows=100&_type=json";

        try {
            // 입력받은 trainGrade에 따라 trainCode를 설정
            // Ex) ITX를 입력받았으면, Code는 18, 08, 09로 설정
            if (trainGrade.equals("KTX")) {
                trainCodeArr = new String[]{"00", "16", "07", "19", "10"};
            } else if (trainGrade.equals("ITX")) {
                trainCodeArr = new String[]{"18", "08", "09"};
            }

            // 각 trainCode에 대해 API 호출
            for (String actualTrainCode : trainCodeArr) {
                URI uri = new URI(String.format(URL, apiKey, depPlaceId, arrPlaceId, depPlandTime, actualTrainCode));
                System.out.println("Request URI: " + uri);
                System.out.println("gradeCode: " + actualTrainCode);

                String apiResponse = restTemplate.getForObject(uri, String.class);

                // JSON 데이터를 처리하여 trainSchedule에 추가
                trainSchedule.addAll(parseTrainSchedule(apiResponse, 0));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI 형식이 잘못되었습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("열차 시간표 조회 중 오류 발생", e);
        }
        return trainSchedule;
    }

    // 사용자가 입력한 열차 종류에 따라 서로 다른 api문서를 호출한다
    // 완성된 api 링크를 입력받는다
    // 데이터를 List<Map<String, String>> 형식으로 반환
    private List<Map<String, String>> parseTrainSchedule(String apiResponse, int type) throws Exception {
        List<Map<String, String>> trainSchedule = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(apiResponse);

        JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {
                String depTime = item.path("depplandtime").asText().substring(8, 12); // HHmm 추출
                String arrTime = item.path("arrplandtime").asText().substring(8, 12); // HHmm 추출
                String trainName = item.path("traingradename").asText();
                String trainNo = item.path("trainno").asText();
                String adultCharge = item.has("adultcharge") ? item.path("adultcharge").asText() : "0";

                Map<String, String> schedule = new HashMap<>();
                schedule.put("depTime", depTime.substring(0, 2) + ":" + depTime.substring(2, 4)); // HHmm -> HH:mm
                schedule.put("arrTime", arrTime.substring(0, 2) + ":" + arrTime.substring(2, 4)); // HHmm -> HH:mm
                schedule.put("adultCharge", adultCharge);
                schedule.put("trainName", trainName);
                schedule.put("trainNum", trainNo);

                trainSchedule.add(schedule);
            }
        }
        return trainSchedule;
    }
}