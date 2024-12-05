/**********************************************************
 MemberDTO는 회원 정보를 전달하기 위한 데이터 전송 객체 (DTO)이다
 ***********************************************************/

package sogong.train.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sogong.train.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {
    private Long id;            // 고유 ID
    private String email;       // 이메일 주소
    private String name;        // 이름
    private String password;    // 비밀번호
    private String phone;       // 전화번호
    private String role;        // 권한


    // MemberEntity 객체를 MemberDTO로 변환한다
    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setEmail(memberEntity.getEmail());
        memberDTO.setPassword(memberEntity.getPassword());
        memberDTO.setName(memberEntity.getName());
        memberDTO.setPhone(memberEntity.getPhone());
        memberDTO.setRole(memberEntity.getRole());
        return memberDTO;
    }
}