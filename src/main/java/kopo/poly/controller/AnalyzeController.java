package kopo.poly.controller;

import com.google.gson.Gson;
import kopo.poly.dto.AnalyzeDTO;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.service.IAnalyzeService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/notice")
public class AnalyzeController {
    private final IAnalyzeService analyzeService;

    @GetMapping(value = "/analyze")
    public String analyzeNotice(HttpSession session, ModelMap model, HttpServletRequest request) {
        log.info(this.getClass().getName() + ".analyzeNotice Start!");

        String msg = "";
        String s_url = "";

        try {
            String user_id = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
//            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
//            String title = request.getParameter("title");
//            String notice_yn = request.getParameter("notice_yn");
//            String content = request.getParameter("content");
//
//
//
//            log.info("title : " + title);
//            log.info("notice_yn : " + notice_yn);
//
//
            String nSeq = request.getParameter("nSeq");
            AnalyzeDTO pDTO = new AnalyzeDTO();
            pDTO.setNotice_seq(nSeq);
//            pDTO.setUser_id(user_id);
//            pDTO.setNotice_seq(nSeq);
//            pDTO.setTitle(title);
//            pDTO.setNotice_yn(notice_yn);
//            pDTO.setContent(content);
            String content = analyzeService.getContents(pDTO).toString();

            log.info("user_id : " + user_id);
            log.info("nSeq : " + nSeq);
            log.info("content : " + content);


            AnalyzeDTO rDTO = Optional.ofNullable(analyzeService.getContents(pDTO)).orElseGet(AnalyzeDTO::new);
            model.addAttribute("rDTO", rDTO);



            log.info("user_id : " + user_id);
            log.info("nSeq : " + nSeq);
            log.info("content : " + content);

            String openApiURL = "http://aiopen.etri.re.kr:8000/NELinking";
            String accessKey = "5e3a93c3-01e2-4454-9cbd-7f62b393b211";
            String contents = content;

            log.info("contents : " + contents);

            Gson gson = new Gson();

            Map<String, Object> requests = new HashMap<>();
            Map<String, String> argument = new HashMap<>();

            argument.put("text", contents);
            requests.put("document", argument);
            log.info("argument" + argument);

            URL url;
            Integer responseCode = null;
            String responseBody = null;

            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", accessKey);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(requests).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                responseBody = sb.toString();
            } else {
                InputStream errorStream = con.getErrorStream();
                if (errorStream != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(errorStream));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    responseBody = sb.toString();
                }
            }

            log.info("[responseCode] " + responseCode);
            log.info("[responseBody]");
            log.info(responseBody);

//            AnalyzeDTO rDTO = new AnalyzeDTO();
//            rDTO.setUser_id(user_id);
            rDTO.setNotice_seq(nSeq);
//            rDTO.setTitle(title);
//            rDTO.setNotice_yn(notice_yn);
            rDTO.setContents(content);
            rDTO.setAnalyzed_contents(responseBody);


            analyzeService.insertAnalyzeNotice(rDTO);

            msg = "분석되었습니다.";
            s_url = "redirect:/notice/noticeList";

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            model.addAttribute("msg", msg);
            model.addAttribute("url", s_url);
            log.info(this.getClass().getName() + ".analyzeNotice End!");
        }

        return "/redirect";
    }
}
