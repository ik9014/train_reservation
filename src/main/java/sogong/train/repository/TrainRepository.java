/**********************************************************
 TrainRepository는 TrainEntity와 DB 간 상호작용을 담당한다
 열차 정보 데이터 조회 등을 구현한다
 ***********************************************************/

package sogong.train.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.train.entity.TrainEntity;
import java.util.List;

public interface TrainRepository extends JpaRepository<TrainEntity, Long> {
    // 특정 회원 Id를 사용하여 해당 회원이 예약한 열차 정보를 조회
    List<TrainEntity> findByMemberEntityId(Long memberId);
}