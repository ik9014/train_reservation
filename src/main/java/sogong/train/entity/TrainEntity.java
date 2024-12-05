/**********************************************************
 TrainEntity는 데이터베이스와 연결되는 열차 정보 엔티티 클래스이다
 ***********************************************************/

package sogong.train.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TrainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainId;               // 열차 Id

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;  // 이 열차를 예약한 사용자 정보와 연결 (다대일 관계)

    private String departureTime;       // 출발 날짜
    private String trainName;           // 열차 이름 Ex) KTX, ITX
    private String trainNum;            // 열차 번호
    private String departure;           // 출발역
    private String arrival;             // 도착역
    private String depTime;             // 출발 시각
    private String arrTime;             // 도착 시각
}