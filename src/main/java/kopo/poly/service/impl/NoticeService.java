package kopo.poly.service.impl;

import kopo.poly.dto.NoticeDTO;
import kopo.poly.persistance.mapper.INoticeMapper;
import kopo.poly.service.INoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor // final로 만들어진 필드를 파라미터로 받는 생성자를 자동생성
@Service
public class NoticeService implements INoticeService {
    private final INoticeMapper noticeMapper;

    // noticeMapper 변수에 이미 메모리에 올라간 INoticeMapper 객체를 넣어줌
    // 예전에는 autowired 어노테이션을 통해 설정했었지만, 이젠 생성자를 통해 객체를 주입함

    /** 공지사항 리스트 **/
    @Override
    public List<NoticeDTO> getNoticeList() throws Exception {

        log.info(this.getClass().getName() + ".getNoticeList start!");

        return noticeMapper.getNoticeList();
    }

    /** 공지사항 상세보기 **/
    @Transactional
    @Override
    public NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception {
        log.info(this.getClass().getName() + ".getNoticeInfo start!");

        if(type) { // 상세보기 할 때마다, 조회수 증가하기(수정보기는 제외)
            log.info("Update ReadCNT");
            noticeMapper.updateNoticeReadCnt(pDTO);
        }
        return noticeMapper.getNoticeInfo(pDTO);
    }

    /** 공지사항 등록하기 **/
    @Transactional // ACID법칙을 지킬 수 있도록 도와주는 어노테이션
    @Override
    public void insertNoticeInfo(NoticeDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".InsertNoticeInfo start!");

        noticeMapper.insertNoticeInfo(pDTO);
    }

    /** 공지사항 수정하기 **/
    @Transactional
    @Override
    public void updateNoticeInfo(NoticeDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".updateNoticeInfo start!");

        noticeMapper.updateNoticeInfo(pDTO);
    }

    /** 공지사항 삭제하기 **/
    @Transactional
    @Override
    public void deleteNoticeInfo(NoticeDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".deleteNoticeInfo start!");

        noticeMapper.deleteNoticeInfo(pDTO);
    }
}
