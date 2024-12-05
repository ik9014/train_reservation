/**********************************************************
 TicketController는 열차 티켓 예약 및 관리를 담당한다

 1. reservation(), /payment (POST)
    사용자로부터 좌석 정보를 받아 열차 및 좌석 데이터를 저장한다

 2. ConfirmPayment(), /confirmPayment (POST)
    결제 확인 요청을 처리하고, 메인 화면으로 리다이렉트한다

 3. showTickets(), /tickets (GET)
    사용자의 모든 열차 티켓 정보를 조회한다

 4. showTickets2(), /ticket/{id} (GET)
    특정 열차 ID에 대한 티켓의 좌석번호를 조회한다

 5. deleteTicket(), /tickets (POST)
    좌석 ID를 기반으로 티켓 데이터를 삭제한다

 6. refundTicket(), /ticket/refund (POST)
    특정 좌석 ID와 관련된 데이터를 찾아 환불 요청을 처리한다
 ***********************************************************/

package sogong.train.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sogong.train.entity.MemberEntity;
import sogong.train.entity.SeatEntity;
import sogong.train.entity.TrainEntity;
import sogong.train.service.MemberService;
import sogong.train.service.SeatService;
import sogong.train.service.TrainService;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TicketController {

    private final TrainService trainService;
    private final SeatService seatService;
    private final MemberService memberService;

    // 사용자로부터 좌석 정보를 받아 열차 및 좌석 데이터를 저장한다
    // "/payment" 경로로 들어오는 HTTP POST 요청을 처리하고
    // payment.html 파일을 렌더링하여 화면에 출력한다
    @PostMapping("/payment")
    public String reservation(
            @RequestParam(required = false) String selectSeat, // 좌석 정보 (열차번호, 날짜, 시간)
            @RequestParam(required = false) String arrTime,
            @RequestParam(required = false) String trainName,
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String arrival,
            @RequestParam(required = false) String adultCharge,
            @RequestParam(required = false) String passengerCount,
            @RequestParam(required = false) Long pkId,
            Model model) {

        // 운임 계산 과정
        int charge = Integer.parseInt(adultCharge);
        int passenger = Integer.parseInt(passengerCount);
        String totalCharge = String.valueOf(charge * passenger);

        MemberEntity memberEntity = memberService.findMemberById(pkId);
        TrainEntity trainEntity;

        // selectSeat.html의 uniqueKey를 분해하는 과정이다
        String[] selectSeatArray = selectSeat.split(",");
        String trainNum = selectSeatArray[0];       // Ex) 233
        String departureTime = selectSeatArray[1];  // Ex) 2024-11-30
        String depTimeandseat = selectSeatArray[2]; // Ex) 06:43-1-6B

        String[] depTimeandseatArray = depTimeandseat.split("-");
        String depTime = depTimeandseatArray[0];        // Ex) 06:43
        String carriageNumber = depTimeandseatArray[1]; // Ex) 1
        String seat = depTimeandseatArray[2];           // Ex) 6B

        for (int i = 0; i < selectSeatArray.length; i += 3){
            String[] depTimeandseatArray2 = depTimeandseat.split("-");
            String depTime2 = depTimeandseatArray2[0];
            String carriageNumber2 = depTimeandseatArray2[1];
            String seat2= depTimeandseatArray2[2];

            // 열차 정보 구성
            trainEntity = new TrainEntity();
            trainEntity.setDepartureTime(departureTime);
            trainEntity.setDepTime(depTime2);
            trainEntity.setArrTime(arrTime);
            trainEntity.setTrainName(trainName);
            trainEntity.setTrainNum(trainNum);
            trainEntity.setDeparture(departure);
            trainEntity.setArrival(arrival);
            trainEntity.setMemberEntity(memberEntity);

            // 열차 정보를 데이터베이스에 저장
            trainService.saveTrain(trainEntity);

            // 좌석 정보 구성
            SeatEntity seatEntity = new SeatEntity();
            seatEntity.setTrain(trainEntity);
            seatEntity.setPassengerCount(passenger);
            seatEntity.setTotalCharge(charge * passenger);
            seatEntity.setCarriageNum(carriageNumber2);
            seatEntity.setSeat(seat2);

            // 좌석 정보를 데이터베이스에 저장
            seatService.saveSeat(seatEntity);
        }

        System.out.println("\nselectSeat: " + selectSeat);
        System.out.println("pkId: " + pkId);
        System.out.println("departureTime: " + departureTime);
        System.out.println("depTime: " + depTime);
        System.out.println("arrTime: " + arrTime);
        System.out.println("trainName: " + trainName);
        System.out.println("trainNum: " + trainNum);
        System.out.println("departure: " + departure);
        System.out.println("arrival: " + arrival);
        System.out.println("adultCharge: " + adultCharge);
        System.out.println("passengerCount: " + passengerCount);
        System.out.println("totalCharge: " + totalCharge);
        System.out.println("seat: " + seat);

        model.addAttribute("carriageNumber", carriageNumber);
        model.addAttribute("departureTime", departureTime);
        model.addAttribute("depTime", depTime);
        model.addAttribute("arrTime", arrTime);
        model.addAttribute("trainName", trainName);
        model.addAttribute("trainNum", trainNum);
        model.addAttribute("departure", departure);
        model.addAttribute("arrival", arrival);
        model.addAttribute("adultCharge", adultCharge);
        model.addAttribute("passengerCount", passengerCount);
        model.addAttribute("totalCharge", totalCharge);
        model.addAttribute("pkId", pkId);
        model.addAttribute("seat", seat);

        return "payment";
    }


    // 결제 요청을 처리하고, 결제 확인 후 메인 화면으로 이동한다
    // "/confirmPayment" 경로로 들어오는 HTTP POST 요청을 처리하고
    // "/sogong/train/main" 경로로 리다이렉트한다
    @PostMapping("/confirmPayment")
    public String confirmPayment(
            @RequestParam String depTime,
            @RequestParam String arrTime,
            @RequestParam String trainName,
            @RequestParam String trainNum,
            @RequestParam String adultCharge,
            @RequestParam Long pkId) {
        return "redirect:/sogong/train/main";  // 홈 페이지로 리다이렉트
    }


    // (열차정보) 사용자의 모든 티켓 정보를 조회한다
    // "/tickets" 경로로 들어오는 HTTP GET 요청을 처리하고
    // tickets.html 파일을 렌더링하여 화면에 출력한다
    @GetMapping("/tickets")
    public String showTickets(
            HttpSession session,
            Model model) {

        // 세션에서 로그인된 사용자 ID를 가져온다
        Long memberId = (Long) session.getAttribute("Id");
        System.out.println("로그인된 유저 ID: " + memberId);

        // 멤버 ID로 티켓 정보 조회
        List<TrainEntity> trainEntities = trainService.getTrainsByMemberId(memberId);

        System.out.println("조회된 티켓 수: " + trainEntities.size());

        model.addAttribute("tickets", trainEntities);

        return "tickets";
    }


    // (좌석정보) 사용자가 선택한 티켓의 좌석을 조회한다
    // "/ticket/{id}" 경로로 들어오는 HTTP GET 요청을 처리하고
    // ticketDetail.html 파일을 렌더링하여 화면에 출력한다
    @GetMapping("/ticket/{id}")
    public String showTickets2(
            @PathVariable("id") Long id,
            Model model){

        TrainEntity trainEntity = trainService.getTrainDetail(id);
        SeatEntity seatEntity = seatService.getSeatDetail(id);

        model.addAttribute("train", trainEntity);
        model.addAttribute("seats", seatEntity);

        return "ticketDetail";
    }


    // 좌석 ID를 기반으로 티켓 정보를 삭제한다
    // "/tickets" 경로로 들어오는 HTTP POST 요청을 처리하고
    // "/tickets" 경로로 리다이렉트한다
    @PostMapping("/tickets")
    public String deleteTicket(
            @RequestParam Long seatId,
            Model model){

        seatService.removeSeatAndTrainIfNecessary(seatId);

        return "redirect:/tickets";
    }


    // 특정 좌석 ID와 관련된 데이터를 찾아 환불 요청을 처리한다
    // "/ticket/refund" 경로로 들어오는 HTTP POST 요청을 처리하고
    // ticketRefund.html 파일을 렌더링하여 화면에 출력한다
    @PostMapping("/ticket/refund")
    public String refundTicket(
            @RequestParam Long seatId,
            Model model) {

        Optional<SeatEntity> seatOptional = seatService.findSeatById(seatId);
        SeatEntity seat = seatOptional.get();

        // uniqueKey는 특정 좌석 정보를 js 로컬스토리지에서 삭제하기 위한 식별 문자열이다
        String uniqueKey = String.format(
                "%s,%s,%s",
                seat.getTrain().getTrainNum(),
                seat.getTrain().getDepartureTime(),
                seat.getTrain().getDepTime()
        );

        String carriageNum = seat.getCarriageNum();
        String seatNum = seat.getSeat();

        model.addAttribute("seatId", seatId);
        model.addAttribute("uniqueKey", uniqueKey);
        model.addAttribute("carriageNum", carriageNum);
        model.addAttribute("seatNum", seatNum);

        return "ticketRefund";
    }
}