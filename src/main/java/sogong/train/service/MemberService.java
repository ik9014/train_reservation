
package sogong.train.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sogong.train.dto.MemberDTO;
import sogong.train.entity.MemberEntity;
import sogong.train.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        //1-> dto를 entity객체로 변환
        //2 -> repository의 save 메서드를 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);//반드시 이 메소드는 save로 사용해야함
        //repository의 save 메서드를 호출해야함

    }

    // pkId로 MemberEntity를 조회하는 메서드
    public MemberEntity findMemberById(Long pkId) {
        // pkId로 MemberEntity를 조회하고 반환
        return memberRepository.findById(pkId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + pkId));
    }

    public MemberDTO login(MemberDTO memberDTO) {
                /*
                1. 회원이 입력한 이메일로 DB에서 조회를 함.
                2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
                 */
            Optional<MemberEntity> byEmail = memberRepository.findByEmail(memberDTO.getEmail());
            //entity객체를 optional로 한 번 더 감싸줌 포장지와 비슷한 원리
            if (byEmail.isPresent()) {
                    //if문에 걸리게되면 해당 멤버가 존재한다.
                    MemberEntity memberEntity = byEmail.get();
                    if(memberEntity.getPassword().equals(memberDTO.getPassword())) {
                            //비밀번호 일치
                            //entity 객체 -> dto객체로 변환 후 리턴
                        MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                        return dto;
                    }
                    else{
                            //비밀번호 불일치
                            return null;
                    }
            }
            else {
                 return null;   //조회 결과 회원이 없다.
            }
    }
}
