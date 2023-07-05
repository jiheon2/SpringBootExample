package kopo.poly.controller;

import kopo.poly.dto.NoticeDTO;
import kopo.poly.service.INoticeService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
//@RequestMapping(value = "/notice") @RequestMapping은 유연한 매핑방법 / /noitce를 기준으로 매핑값들을 상대경로로 작성해야함
@Controller
public class NoticeController {

    // @RequiredArgsConstructor 통해 메모리에 올라간 서비스 객체를 Controller 에서 사용할수 있게 주입함
    private final INoticeService noticeService;




    /** ================ 게시판 리스트 보여주기 ================= **/
    @GetMapping(value = "/notice/noticeList")
    public String noticeList(ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".noticeList Start!"); // 로그시작

        // 공지사항 리스트 조회하기
        List<NoticeDTO> rList = noticeService.getNoticeList();
        if(rList == null) rList = new ArrayList<>();
        // JAVA 8부터 제공되는 Optional 활요하여 NPE(Null Pointer Exception) 처리
        // NPE란? > 객체 참조가 null인 상태에서 해당 객체의 멤버나 메서드에 접근하려고 할 때 발생하는 예외
        // List<NoticeDTO> rList = Optional.ofNullable(noticeService.getNoticeList()).orElseGet(ArrayList::new);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);
        // .addAtribute : Model객체에 속성을 추가하는 메서드
        // 추가된 속성은 뷰(view)에서 사용가능 / 속성은 key와 value로 구성

        log.info(this.getClass().getName() + ".noticeList End!"); // 로그 끝

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "/notice/noticeList";
    }
    // GetMapping(value = "notice/noticeList") => GET방식을 통해 접속되는 URL이 notice/noticeList 경우 함수 실행
    /** ========================================================= **/






    /** ====================== 게시판 작성 페이지 접근 코드 ================== **/
    @GetMapping(value = "/notice/noticeReg")
    public String NoticeReg() {
        log.info(this.getClass().getName() + ".noticeReg Start!");

        log.info(this.getClass().getName() + ".noticeReg End!");

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "/notice/noticeReg";
    }
    /** ===================================================================== **/





    /** =========================== 게시판 글 등록 ============================ **/
    // 게시글 등록은 Ajax로 호출되기 때문에 결과는 JSON 구조로 전달해야만 함
    // JSON 구조로 결과 메시지를 전송하기 위해 @ResponseBody 어노테이션 추가함

    @PostMapping(value = "/notice/noticeInsert")
    public String noticeInsert(HttpServletRequest request, ModelMap model, HttpSession session) {
        log.info(this.getClass().getName() + ".noticeInsert Start!");

        String msg = ""; // 메세지 내용
        String url = "/notice/noticeReg"; // 이동할 경로 내용

        // 로그인된 사용자 아이디를 가져오기
        // 로그인 미구현으로 공지사항 리스트에서 로그인 한 것 처럼 Session 값을 지정
        try { // 실행코드(예외가 발생할 수 있는 코드)
            String user_id = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String notice_yn = CmmUtil.nvl(request.getParameter("notice_yn")); // 공지글 여부
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용


            // ========================= 로그 찍기 ==========================
            log.info("session user_id : " + user_id);
            log.info("title : " + title);
            log.info("notice_yn : " + notice_yn);
            log.info("contents : " + contents);
            // ==============================================================



            // ============== 데이터 저장을 위해 DTO에 저장 =================
            NoticeDTO pDTO = new NoticeDTO(); // DTO 생성
            pDTO.setUser_id(user_id);
            pDTO.setTitle(title);
            pDTO.setNotice_yn(notice_yn);
            pDTO.setContents(contents);
            // ==============================================================


            // ============== 게시글 등록 비즈니스 로직 호출 ================
            noticeService.insertNoticeInfo(pDTO);
            // ==============================================================



            // ================= 저장 완료 메시지 =============
            msg = "등록되었습니다."; // 메시지
            url = "/notice/noticeList"; // 경로
            // ================================================


            // ================= 저장 실패 메시지 =============
        } catch (Exception e) { // 예외처리 코드
            msg = "실패하였습니다. : " + e.getMessage(); // 예외 객체의 메시지를 가져와 msg에 추가
            log.info(e.toString()); // log객체를 사용하여 예외 객체의 정보를 로깅 / toString() 예외 객체를 문자열 표현으로 반환
            e.printStackTrace(); // 객체의 스택 트레이스를 출력 / 예외가 발생한 코드 위치와 예외가 어떻게 전파되었는지 추적 가능
            // ================================================


            // ================ 결과 메시지 전달하는 코드 =============
        } finally { //예외와 상관없이 항상 실행되는 코드 / 생략 가능
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);
            log.info(this.getClass().getName() + ".noticeInsert End!");
        }
            // ========================================================

        return "/redirect"; // /redirect는 경로가 아니라 리다이렉트를 수행하는 특별한 문자열
    }
    /** =================================================================================== **/





    /** ========================== 공지글 상세보기 코드 =================================== **/
    @GetMapping(value = "/notice/noticeInfo")
    public String noticeInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".noticeInfo Start!"); // 로그시작

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글 번호(PK)

        log.info("nSeq : " + nSeq); // PK 로그 출력

        // =========== DTO에 전달받은 값을 넣는 코드 ==============
        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNotice_seq(nSeq);
        // =========== 값 전달은 반드시 DTO 객체를 이용해서 처리 ==



        // ============= 공지사항 상세정보 가져오기 코드 =============================================================
        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, true)).orElseGet(NoticeDTO::new);
        // NPE(Null Pointer Exception) 예외 처리 코드

        // 조회된 리스트 결과값 넣어주는 코드(46번째줄 코드)
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".noticeInfo End!"); // 로그 끝

        // 함수처리가 끝나고 보여줄 html 파일명
        return "/notice/noticeInfo";
    }
    /** ================================================================================= **/




    /**  ================== 게시판 수정 코드 =====================================  **/
    @GetMapping(value = "/notice/noticeEditInfo")
    public String noticeEditInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".noticeEditInfo Start!"); // 로그시작

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글 번호(PK)

        log.info("nSeq : " + nSeq); // PK 로그


        // ============== 값 전달을 위한 DTO 생성 코드(162번째줄 코드) ============
        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNotice_seq(nSeq);
        // ========================================================================


        NoticeDTO rDTO = noticeService.getNoticeInfo(pDTO, false);
        if(rDTO == null) rDTO = new NoticeDTO();
        // 코드 40번째줄

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".noticeEditInfo End!"); // 로그 끝

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "/notice/noticeEditInfo";
    }
    /** =============================================================================== **/





    /** ======================== 게시판 글 수정 실행 로직 ============================== **/
    @PostMapping(value = "/notice/noticeUpdate")
    public String noticeUpdate(HttpSession session, ModelMap model, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".noticeUpdate Start!"); // 로그시작

        String msg = ""; // 메세지 내용
        String url = ""; // 이동할 경로

        try { // 예외가 발생할 수도 있는 코드
            String user_id = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); // ID
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 글번호(PK)
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String notice_yn = CmmUtil.nvl(request.getParameter("notice_yn")); // 공지글 여부
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            // ============ 로그 코드 ===================
            log.info("user_id : " + user_id);
            log.info("nSeq : " + nSeq);
            log.info("title : " + title);
            log.info("notice_yn : " + notice_yn);
            log.info("contents : " + contents);
            // ==========================================



            // =============== 값 전달을 위한 DTO 생성 코드 ==========
            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setUser_id(user_id);
            pDTO.setNotice_seq(nSeq);
            pDTO.setTitle(title);
            pDTO.setNotice_yn(notice_yn);
            pDTO.setContents(contents);
            // =======================================================

            // 게시글 수정하기 DB
            noticeService.updateNoticeInfo(pDTO);

            // ====== 함수 처리 후 보여줄 메시지 & 이동할 경로 =========
            msg = "수정되었습니다.";
            url = "/notice/noticeInfo?nSeq=" + nSeq; //url에 pk값의 번호를 더한 것
            // =========================================================

            // =============== 함수 실행 실패 시 보여주는 메시지 코드 ==============
        } catch (Exception e) { // 예외처리 코드
            msg = "실패하였습니다. : " + e.getMessage(); // 메세지를 보여줌
            log.info(e.toString()); // 코드131번째 줄 / 예외 객체 로깅
            e.printStackTrace(); // 스택 트레이스 출력
            // =====================================================================


        } finally { // 예외와 상관없이 항상 실행
            model.addAttribute("msg", msg); // 속성 추가 "msg" = key / msg = value
            model.addAttribute("url", url); // 속성 추가
            log.info(this.getClass().getName() + ".noticeUpdate End!"); // 로그 끝
        }

        return "/redirect"; // redirect 반환(코드 145번째 줄)
    }
/** ====================================================================================================== **/



/** ========================== 게시글 삭제 코드 ================================= **/
    @GetMapping(value = "/notice/noticeDelete")
    public String deleteNoticeInfo(HttpSession session, ModelMap model, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".noticeDelete Start!"); // 로그 시작

        String msg = ""; // 메세지
        String url = ""; // 이동할 경로(리다이렉트할 URL)를 저장

        try { // 예외 발생 가능한 코드
            String user_id = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
            String title = CmmUtil.nvl(request.getParameter("title"));
            String notice_yn = CmmUtil.nvl(request.getParameter("notice_yn"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));
            // CmmUtil.nvl() : 값이 null일 경우, 대체값을 지정할 수 있는 유틸리티 메서드

            // ================== 로그 ==============
            log.info("user_id : " + user_id);
            log.info("nSeq : " + nSeq);
            log.info("title : " + title);
            log.info("notice_yn : " + notice_yn);
            log.info("contents : " + contents);
            // ======================================





            // ========= 값 저장할 DTO 선언 =========
            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setUser_id(user_id);
            pDTO.setNotice_seq(nSeq);
            pDTO.setTitle(title);
            pDTO.setNotice_yn(notice_yn);
            pDTO.setContents(contents);
            // ======================================


            // ====== 비즈니스 로직 수행 ========
            noticeService.deleteNoticeInfo(pDTO);
            // 메서드 호출 후 pDTO 전달 > NoticeService가 삭제하는 기능 수행

            // 사용자에게 알려줄 코드
            msg = "삭제되었습니다.";
            url = "/notice/noticeList"; // 삭제 후 noticeList로 리다이렉트

        } catch (Exception e) { // 예외 처리
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);
            log.info(this.getClass().getName() + ".noticeDelete End!"); // 로그 끝
        }

        return "/redirect";
    }
}
