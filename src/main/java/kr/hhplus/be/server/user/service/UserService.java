package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.user.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDao userDao;

    public int checkUserCountByUserNo(int userNo){
        // 사용자 존재 여부 확인
        return userDao.checkUserCountByUserNo(userNo);
    }
}
