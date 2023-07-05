package kopo.poly.service;


import kopo.poly.dto.UserInfoDTO;

public interface IUserInfoService {
//    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;
//    // 아이디 중복체크
//
//    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;
//    // 이메일 주소 중복체크 및 인증 값

    int insertUserInfo(UserInfoDTO pDTO) throws Exception;
    // 회원가입(회원정보 등록하기)

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;
    // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기

//    UserInfoDTO searchUserIdOrPasswordProc(UserInfoDTO pDTO) throws Exception;
//    // 아이디, 비밀번호 찾기에 활용
//
//    int newPasswordProc(UserInfoDTO pDTO) throws Exception;
//    // 비밀번호 재설정

}
