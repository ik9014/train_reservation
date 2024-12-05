/**********************************************************
 HomeController는 홈 페이지(메인 페이지)를 제공하는 역할을 한다
 ***********************************************************/

package sogong.train.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 메인 페이지를 반환한다
    // "/" 경로로 들어오는 HTTP GET 요청을 처리하고
    // index.html 파일을 렌더링하여 화면에 출력한다
    @GetMapping("/")
    public String index() {
        return "index";
    }
}