/**********************************************************
 MemberController는 회원정보 관리 기능을 담당한다

 1. saveForm(), /sogong/train/save (GET)
    회원가입 폼 페이지 반환

 2. save(), /sogong/train/save (POST)
    클라이언트로부터 전송된 MemberDTO 데이터를 받아 회원가입 처리

 3. loginForm(), /sogong/train/login (GET)
    로그인 폼 페이지 반환

 4. login(), /sogong/train/login (POST)
    클라이언트로부터 전송된 MemberDTO (로그인 정보)를 검증

 5. mainForm(), /sogong/train/main (GET)
    로그인된 사용자인지 확인 (메인화면임)

 6. logout(), /sogong/train/logout (GET)
    세션을 무효화하여 로그아웃 처리
 ***********************************************************/

package sogong.train.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sogong.train.dto.MemberDTO;
import sogong.train.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입 폼 페이지를 반환한다
    // "/sogong/train/save" 경로로 들어오는 HTTP GET 요청을 처리하고
    // save.html 파일을 렌더링하여 화면에 출력한다
    @GetMapping("/sogong/train/save")
    public String saveForm() {
        return "save";
    }


    // 클라이언트로부터 데이터를 받아 회원가입을 진행한다
    // "/sogong/train/save" 경로로 들어오는 HTTP POST 요청을 처리하고
    // "/sogong/train/login" 경로로 리다이렉트한다
    @PostMapping("/sogong/train/save")
    public String save(
            @ModelAttribute MemberDTO memberDTO) {
        System.out.println("sogong save");
        System.out.println("username: " + memberDTO);
        memberService.save(memberDTO);
        return "redirect:/sogong/train/login";
    }


    // 로그인 폼 페이지를 반환한다
    // "/sogong/train/login" 경로로 들어오는 HTTP GET 요청을 처리하고
    // login.html 파일을 렌더링하여 화면에 출력한다
    @GetMapping("/sogong/train/login")
    public String loginForm() {
        return "login";
    }


    // 사용자가 로그인 상태인지 아닌지를 검사한다
    // "/sogong/train/login" 경로로 들어오는 HTTP POST 요청을 처리한다
    // 로그인에 성공하면 "redirect:/sogong/train/main" 경로로 리다이렉트한다
    // 로그인에 실패하면 login.html 파일을 렌더링하여 화면에 출력한다
    @PostMapping("/sogong/train/login")
    public String login(
            @ModelAttribute MemberDTO memberDTO,
            HttpSession session) {
       MemberDTO loginResult = memberService.login(memberDTO);

       if (loginResult != null) {
           session.setAttribute("Id", loginResult.getId());
           session.setAttribute("loginEmail", loginResult.getEmail());
           session.setAttribute("loginName", loginResult.getName());
           session.setAttribute("role", loginResult.getRole());
           return "redirect:/sogong/train/main";
       }
       else{
           return "login";
       }
    }


    // 로그인 되어 있을 때의 메인페이지이다 (상단 바 메뉴가 다름)
    // "/sogong/train/main" 경로로 들어오는 HTTP GET 요청을 처리한다
    // 로그인 상태라면 main.html 파일을 렌더링하여 화면에 출력한다
    // 로그인 상태가 아니라면 "/sogong/train/login" 경로로 리다이렉트한다
    @GetMapping("/sogong/train/main")
    public String mainForm(
            HttpSession session,
            Model model) {
        String loginName = (String) session.getAttribute("loginName");

        if (loginName != null) {
            model.addAttribute("loginName", loginName);
            return "main";
        } else {
            return "redirect:/sogong/train/login";
        }
    }


    // 세션을 무효화하여 로그아웃을 처리한다
    // "/sogong/train/logout" 경로로 들어오는 HTTP GET 요청을 처리한다
    // index.html 파일을 렌더링하여 화면에 출력한다
    @GetMapping("/sogong/train/logout")
    public String logout(
            HttpSession session) {
        session.invalidate();
        return "index";
    }
}