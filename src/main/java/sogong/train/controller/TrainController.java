/**********************************************************
 TrainController는 API 호출을 통한 기차시간표 검색을 담당한다

 1. setStationCodes()
    역 이름을 API에서 요구하는 코드값으로 변환한다
    Ex) 서울 -> NAT010000

 2. searchTrains(), /search (POST)
    기차 시간표를 검색하고 그 결과를 반환한다
 ***********************************************************/

package sogong.train.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import sogong.train.api.TrainAPIController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TrainController {

    private final TrainAPIController trainAPIController;

    // 역 이름을 API에서 요구하는 코드 값으로 변환한다
    public String setStationCodes(String name) {
        switch (name) {
            case "서울":
                name = "NAT010000";
                break;
            case "수원":
                name = "NAT010415";
                break;
            case "평택":
                name = "NAT010754";
                break;
            case "대전":
                name = "NAT011668";
                break;
            case "동대구":
                name = "NAT013271";
                break;
            case "부산":
                name = "NAT014445";
                break;
            case "천안아산":
                name = "NATH10960";
                break;
        }
        return name;
    }


    // 사용자가 제공한 정보를 기반으로 기차 시간표를 검색한다
    // "/search" 경로로 들어오는 HTTP POST 요청을 처리하고
    // search_result.html 파일을 렌더링하여 화면에 출력한다
    @PostMapping("/search")
    public String searchTrains(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String departureTime,
            @RequestParam String trainGrade,
            @RequestParam(required = false, defaultValue = "1") String passengerCount,
            Model model) {

        String depCode = setStationCodes(departure);
        String arrCode = setStationCodes(arrival);

        String formattedDate = departureTime.replace("-", "");

        System.out.println("\nData: " + departureTime);
        System.out.println("departure: " + departure);
        System.out.println("arrival: " + arrival);
        System.out.println("depCode: " + depCode);
        System.out.println("arrCode: " + arrCode);

        // API 호출을 통한 기차시간표 조회
        List<Map<String, String>> trainSchedule = trainAPIController.getTrainSchedule(
                depCode,
                arrCode,
                formattedDate,
                trainGrade);

        model.addAttribute("trainSchedule", trainSchedule);
        model.addAttribute("departure", departure);
        model.addAttribute("arrival", arrival);
        model.addAttribute("departureTime", departureTime);
        model.addAttribute("passengerCount", passengerCount);

        return "search_results";
    }
}