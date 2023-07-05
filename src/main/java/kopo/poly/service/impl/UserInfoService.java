package kopo.poly.service.impl;

import kopo.poly.dto.UserInfoDTO;
import kopo.poly.persistance.mapper.IUserInfoMapper;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {

    private final IUserInfoMapper userInfoMapper; //회원관련 SQL 사용하기위한 Mapper 가져오기


    /** 회원가입하기 **/
    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        int res = 0; // 회원가입 성공 : 1, 기타에러 발생 : 0

        res = userInfoMapper.insertUserInfo(pDTO); //회원가입

        log.info(this.getClass().getName() + ".insertUserInfo End!");

        return res;
    }


    /** 로그인을 위해 ID와 PW가 일치하는지 확인하기 위한 mapper 호출하기 **/
    @Override
    public UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getLogin Start!"); // 로그시작

        UserInfoDTO rDTO = Optional.ofNullable(userInfoMapper.getLogin(pDTO)).orElseGet(UserInfoDTO::new);
        // userInfoMapper.getUserLoginCHeck(pDTO) 함수 실행결과가 NULL이 발생하면, UserInfoDTO 메모리에 올리기

        // 일치하는지 확인하는 코드
        if(CmmUtil.nvl(rDTO.getUser_id()).length() > 0) {
            log.info("로그인 성공");
        }
        /*
        userInfoMapper로 부터 SELECT 쿼리의 결과로 회원아이디를 받아왔다면, 로그인 성공!
        DTO의 변수에 값이 있는지 확인하기 / 처리속도 측면에서 가장 좋은 방법은 변수의 길이를 가져오는 방법
        따라서 .length() 함수를 통해 회원아이디의 글자수를 가져와 0보다 큰지 비교함
        0보다 크다면, 글자가 존재하는 것이기 때문에 값이 존재함
         */

        log.info(this.getClass().getName() + ".getLogin End!"); // 로그 끝

        return rDTO;
    }
}
