/**********************************************************
 SeatController는 열차의 좌석 선택 기능을 담당한다

 1. selectSeat(), /selectSeat (POST)
    열차 및 승객 정보를 처리하여 좌석 선택 페이지로 전달한다
 ***********************************************************/
package sogong.train.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SeatController {

    // 열차 및 승객 정보를 처리하여 좌석 선택 페이지로 전달한다
    // "/selectSeat" 경로로 들어오는 HTTP POST 요청을 처리하고
    // selectSeat.html 파일을 렌더링하여 화면에 출력한다
    @PostMapping("/selectSeat")
    public String selectSeat(
            @RequestParam String depTime,
            @RequestParam String arrTime,
            @RequestParam String trainName,
            @RequestParam String trainNum,
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String adultCharge,
            @RequestParam String passengerCount,
            @RequestParam String departureTime,
            @RequestParam String pkId,
            Model model) {

        // 운임 계산과정
        int charge = Integer.parseInt(adultCharge);
        int passenger = Integer.parseInt(passengerCount);
        int totlaCharge = charge * passenger;

        // uniqueKey는 좌석 정보를 로컬스토리지에 열차별로 고유하게 저장하기 위한 식별 문자열이다
        // Ex) 123,2024-12-02,02:33
        String uniqueKey = trainNum + "," + departureTime + "," + depTime;

        System.out.println("\n열차 번호: " + trainNum);
        System.out.println("열차 출발날짜: " + departureTime);
        System.out.println("열차 출발시각: " + depTime);
        System.out.println("최종 키: " + uniqueKey);

        model.addAttribute("passengerCount", passengerCount);
        model.addAttribute("depTime", depTime);
        model.addAttribute("arrTime", arrTime);
        model.addAttribute("trainName", trainName);
        model.addAttribute("trainNum", trainNum);
        model.addAttribute("departure", departure);
        model.addAttribute("arrival", arrival);
        model.addAttribute("adultCharge", adultCharge);
        model.addAttribute("passengerCount", passengerCount);
        model.addAttribute("totalCharge", totlaCharge);
        model.addAttribute("departureTime", departureTime);
        model.addAttribute("pkId", pkId);
        model.addAttribute("uniqueKey", uniqueKey);

        return "selectSeat";
    }
}