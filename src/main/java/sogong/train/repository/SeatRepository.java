/**********************************************************
 SeatRepository는 SeatEntity와 DB 간 상호작용을 담당한다
 좌석 정보 조회, 관리 등을 구현한다
 ***********************************************************/

package sogong.train.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.train.entity.SeatEntity;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
    // 특정 회원의 ID에 해당하는 열차에 포함된 좌석 데이터를 조회
    List<SeatEntity> findByTrain_MemberEntity_Id(Long memberId);


    // 특정 열차에 연관된 모든 좌석 데이터를 조회
    List<SeatEntity> findByTrain_TrainId(Long trainId);


    // 특정 열차에 예약된 좌석의 개수를 카운트
    long countByTrain_TrainId(Long trainId);


    // 특정 Id에 해당하는 좌석 정보를 조회
    Optional<SeatEntity> findById(Long id);
}