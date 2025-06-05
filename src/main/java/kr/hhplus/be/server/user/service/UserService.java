package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.user.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDao userDao;

    public int checkUserCountByUserNo(int userNo) {
        return userDao.checkUserCountByUserNo(userNo);
    }
}
