/**********************************************************
 MemberRepository는 MemberEntity와 DB 간 상호작용을 담당한다
 회원 정보 조회 및 관리 기능을 구현한다
 ***********************************************************/

package sogong.train.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.train.entity.MemberEntity;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 회원 이메일을 기준으로 데이터를 조회한다
    Optional<MemberEntity>findByEmail(String email);
}
