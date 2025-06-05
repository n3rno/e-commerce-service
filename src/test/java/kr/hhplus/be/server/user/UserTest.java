package kr.hhplus.be.server.user;

import kr.hhplus.be.server.user.repository.UserDao;
import kr.hhplus.be.server.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDao userDao;


    @DisplayName("등록된 사용자번호인지 확인한다.")
    @Test
    void isUserTest() throws IllegalAccessException {
        // given
        final int userNo = 1;

        // when
        int count = userService.checkUserCountByUserNo(userNo);

        // then
        assertThat(count).isEqualTo(1);

    }
}
