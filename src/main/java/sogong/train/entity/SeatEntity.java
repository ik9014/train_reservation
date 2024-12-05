/**********************************************************
 SeatEntity는 데이터베이스와 연결되는 좌석 정보 엔티티 클래스이다
 ***********************************************************/

package sogong.train.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;        // 좌석 Id

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private TrainEntity train;  // 좌석이 속한 열차를 나타낸다 (다대일 관계)

    private int passengerCount; // 승객 수
    private int totalCharge;    // 운임
    private String carriageNum; // 객차 번호
    private String seat;        // 좌석 번호
}