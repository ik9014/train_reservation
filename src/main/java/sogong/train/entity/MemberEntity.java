/**********************************************************
 MemberEntity는 데이터베이스와 연결되는 회원 정보 엔티티 클래스이다
 ***********************************************************/

package sogong.train.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import sogong.train.dto.MemberDTO;

@Entity
@Setter
@Getter
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;            // Id

    @Column(unique = true, nullable = false, length = 100)
    private String email;       // 이메일 주소

    @Column(nullable = false, length = 50)
    private String name;        // 이름

    @Column(nullable = false, length = 100)
    private String password;    // 비밀번호

    @Column(nullable = false, length = 50)
    private String phone;       // 전화번호

    @Column
    private String role;        // 권한


    // DTO 데이터를 MemberEntity로 변환한다
    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setEmail(memberDTO.getEmail());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setName(memberDTO.getName());
        memberEntity.setPhone(memberDTO.getPhone());
        memberEntity.setRole("user");
        return memberEntity;
    }
}