package kopo.poly.service;

import kopo.poly.dto.NoticeDTO;

import java.util.List;

public interface INoticeService {

    /** =================== 공지사항 리스트  ================== **/
    List<NoticeDTO> getNoticeList() throws Exception;

    /** =================== @return 조회결과 ================== **/




    /** ====================== 공지사항 상세보기  ======================= **/
    NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception;

    /** ====== @param pDTO : 상세내용 조회할 notice_seq 값 ==============
     *  ====== @param type : 조회수 증가여부(수정보기는 조회수 증가X) ===
     *  ====================== @return 조회 결과 ======================== **/




    /** ===================== 공지사항 등록 ======================= **/
    void insertNoticeInfo(NoticeDTO pDTO) throws Exception;

    /** === @param pDTO : 화면에서 입력된 공지사항 입력된 값들 === **/




    /** =========================== 공지사항 수정 ========================== **/
    void updateNoticeInfo(NoticeDTO pDTO) throws Exception;

    /** == @param pDTO 화면에서 입력된 수정되기 위한 공지사항 입력된 값들 == **/


    void deleteNoticeInfo(NoticeDTO pDTO) throws  Exception;

}
