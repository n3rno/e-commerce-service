package kr.hhplus.be.server.user.application.service;

import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public int checkUserCountByUserNo(int userNo){
        // 사용자 존재 여부 확인
        return userRepository.checkUserCountByUserNo(userNo);
    }
}
