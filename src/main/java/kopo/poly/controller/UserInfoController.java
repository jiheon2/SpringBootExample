package kopo.poly.controller;

import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserInfoController {
    private final IUserInfoService userinfoService;

    @GetMapping(value = "/user/userRegForm") // 회원가입 화면으로 이동
    public String userRegForm() {
        log.info(this.getClass().getName() + ".user/userRegForm");

        return "/user/userRegForm";
    }

    @PostMapping(value = "/user/insertUserInfo") // 회원가입 로직처리
    public String insertUserInfo(HttpServletRequest request, ModelMap modelMap) throws Exception {

        log.info(this.getClass().getName() + ".insertUserInfo start!");
        int res;
        String msg = ""; //회원가입 결과에 대한 메시지를 전달할 변수
        String url = ""; //회원가입 결과에 대한 URL을 전달할 변수

        UserInfoDTO pDTO = null; // 웹에서 받는 정보를 저장할 변수

        try {

            /** ======웹에서 받는 정보를 String 변수에 저장하는 코드====== **/

            String user_id = CmmUtil.nvl(request.getParameter("user_id")); // getParameter 변수를 가져옴 > user_id를 키의 형식
            String user_name = CmmUtil.nvl(request.getParameter("user_name"));
            String password = CmmUtil.nvl(request.getParameter("password"));
            String email = CmmUtil.nvl(request.getParameter("email"));
            String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
            String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

            /** ========================================================= **/







            /** ======로그를 찍어 값이 제대로 들어왔는지 파악하기 =======**/

            log.info("user_id : " + user_id);
            log.info("user_name : " + user_name);
            log.info("password : " + password);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            /** ========================================================**/






            /** ==============웹에서 받는 정보를 DTO에 저장하는 코드===================== **/

            pDTO = new UserInfoDTO(); // 웹에서 받는 정보를 저장할 변수를 메모리에 올림

            pDTO.setUser_id(user_id);
            pDTO.setUser_name(user_name);
            pDTO.setPassword(EncryptUtil.encHashSHA256(password)); //HashSHA알고리즘 : 비밀번호가 절대로 복호화되지 않도록 암호화(원상태 복구X)
            pDTO.setEmail(EncryptUtil.encAES128CBC(email)); //AES128-CBC알고리즘 : 민감한 정보 암호화(원상태 복구O)
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            /** =========무조건 웹으로 받은 정보는 DTO에 저장한다고 이해하기 =========== **/







            /** =====================회원가입 후 코드===================== **/

            res = userinfoService.insertUserInfo(pDTO); // pDTO를 DB에 넣는 함수(insertUserInfo 함수 기능)

            log.info("회원가입 결과(res) : " + res);

            if(res==1) {
                msg = "회원가입되었습니다.";
            } else if(res==2) { //ajax를 활용해서 아이디 중복, 이메일 중복을 체크하길 바람
                msg = "이미 가입된 아이디입니다.";
            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";
            }
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e; // 저장이 실패되면 사용자에게 보여줄 메시지
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);

            log.info(this.getClass().getName() + ".insertUserInfo End!");
        }

        return "/redirect";
    }
}
