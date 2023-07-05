package kopo.poly.persistance.mapper;

import kopo.poly.dto.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface INoticeMapper {
    List<NoticeDTO> getNoticeList() throws Exception; // 게시판 리스트
    // 매개변수가 없음 > 전체조회를 하기때문에 매개변수가 필요없음
    // 전체 조회 결과가 1개 이상이므로 List 형식으로 받아줌

    void insertNoticeInfo(NoticeDTO pDTO) throws Exception; // 게시판 글 등록
    // 성공과 실패여부 상관없이 등록

    NoticeDTO getNoticeInfo(NoticeDTO pDTO) throws Exception; // 게시판 상세보기

    void updateNoticeReadCnt(NoticeDTO pDTO) throws Exception; // 게시판 조회수 업데이트

    void updateNoticeInfo(NoticeDTO pDTO) throws Exception; // 게시판 글 수정

    void deleteNoticeInfo(NoticeDTO pDTO) throws Exception; // 게시판 글 삭제
}
