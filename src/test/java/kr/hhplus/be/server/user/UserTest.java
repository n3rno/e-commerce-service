package kr.hhplus.be.server.user;

import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.infrastructure.persistence.mapper.UserMapper;
import kr.hhplus.be.server.user.application.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @DisplayName("등록된 사용자번호인지 확인한다.")
    @Test
    void isUserTest() {
        // given
        final int userNo = 1;

        // when
        int count = userService.checkUserCountByUserNo(userNo);

        // then
        assertThat(count).isEqualTo(1);

    }
}
