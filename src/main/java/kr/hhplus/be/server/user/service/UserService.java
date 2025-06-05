package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.user.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDao userDao;

    public int checkUserCountByUserNo(int userNo) throws IllegalAccessException {
        // 사용자 존재 여부 확인
        int count = userDao.checkUserCountByUserNo(userNo);
        if (count == 0) {
            throw new IllegalAccessException();
        }
        return count;
    }
}
