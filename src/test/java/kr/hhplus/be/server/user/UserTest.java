package kr.hhplus.be.server.user;

import kr.hhplus.be.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
public class UserTest {

    @InjectMocks //
    private UserService userService;

    @Mock
    private UserDao userDao;


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
