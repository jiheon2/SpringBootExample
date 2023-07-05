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
import javax.servlet.http.HttpSession;

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
                url = "/user/login";
            } else if(res==2) { //ajax를 활용해서 아이디 중복, 이메일 중복을 체크하길 바람
                msg = "이미 가입된 아이디입니다.";
                url = "/user/RegForm";
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
            /** ============================================================= **/


    /** ==================== 로그인 처리 및 결과 알려주는 화면으로 이동 ===================== **/
    @PostMapping(value = "/user/loginProc")
    public String loginProc(HttpServletRequest request, ModelMap model, HttpSession session) {
    // 로그인이 성공하면 사용자의 정보를 HttpSession에 저장하기 위해 해당함수의 선언부에 매개변수로 선언

        log.info(this.getClass().getName() + ".loginProc Start!"); // 로그 시작

        String msg = ""; // 로그인 결과에 대한 메시지를 전달할 변수
        String url = ""; // 웹에서 받는 정보를 저장할 변수(회원정보 입력화면)
        UserInfoDTO pDTO = null; // DTO선언

        try { // 예외가 발생할 수 있는 코드
            String user_id = CmmUtil.nvl(request.getParameter("user_id"));
            String password = CmmUtil.nvl(request.getParameter("password"));
            // 화면으로부터 넘어온 데이터를 Input태그 속 name값과 매칭시켜 꺼냄

            // ============ 로그 ============
            log.info("user_id : " + user_id);
            log.info("password : " + user_id);
            // ==============================


            // 웹에서 받는 정보를 저장할 변수를 메모리에 올리기
            pDTO = new UserInfoDTO();

            // ================= 로그인 정보를 pDTO에 저장하는 코드 ==================
            pDTO.setUser_id(user_id);
            pDTO.setPassword(EncryptUtil.encHashSHA256(password)); // 암호화
            // =======================================================================

            // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기 위한 userInfoService 호출
            UserInfoDTO rDTO = userinfoService.getLogin(pDTO);

                /** ============== 세션에 회원아이디 정보 저장하기 코드 =================== **/

            if(CmmUtil.nvl(rDTO.getUser_id()).length() > 0) { // 만약 로그인에 성공했다면/*
             /*
                세션은 톰켓(was)의 메모리에 존재하며, 웹사이트에 접속한 사람(연결된 객체)마다 메모리에 값을 올림
                ex) 톰켓에 100명의 사용자가 로그인했다면, 사용자 각각 회원아이디를 메모리에 저장하며,
                메모리에 저장된 객체의 수는 100개임. 따라서 과도한 세션은 톰켓의 메모리 부하를 발생시켜
                서버가 다운되는 현상이 있을 수 있기 때문에, 최소한으로 사용하는 것을 권장함

                스프링에서 세션을 사용하기 위해서는 함수명의 파라미터에 HttpSession session이 존재해야함
                세션은 톰켓의 메모리에 저장되기 때문에 url마다 전달하는게 필요하지 않고,
                그냥 메모리에서 부르면 되기 때문에 화면, controller에서 쉽게 불러서 쓸 수 있음.
              */


                // ============== 세션에 회원아이디 저장하는 코드 ======================
                session.setAttribute("SS_USER_ID", user_id);
                session.setAttribute("SS_USER_NAME", CmmUtil.nvl(rDTO.getUser_name()));
                /*
                    로그인여부를 체크하기 위해 세션에 값이 존재하는지 체크
                    일반적으로 세션에 저장되는 key는 대문자로 입력, 앞에 SS붙임
                    세션에 key, value 형태로 저장함
                 */
                // =====================================================================


                // 로그인 성공 메시지와 이동할 경로의 url
                msg = "로그인이 성공했습니다. \n"+rDTO.getUser_name()+"님 환영합니다.";
                url = "/notice/noticeList";
            } else { // 실패할 경우의 메시지와 url
                msg = "로그인에 실패하였습니다.";
                url = "/user/login";
            }
        } catch (Exception e) { // 예외처리 코드 & 저장실패 메시지
            msg = "시스템 문제로 로그인이 실패했습니다.";
            log.info(e.toString());
            e.printStackTrace();

        } finally { // 속성추가 코드
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);

            log.info(this.getClass().getName() + ".loginProc End!"); // 로그 끝
        }

        return "/redirect";
    }
    /** =================================================================================================== **/


    /** ===================== 로그인을 위한 입력 화면으로 이동 ============================ **/
    @GetMapping(value = "/user/login")
    public String login() {
        log.info(this.getClass().getName() + ".user/login Start!");
        log.info(this.getClass().getName() + ".user/login End!");
        return "/user/login";
    }
    /** =================================================================================== **/
}
