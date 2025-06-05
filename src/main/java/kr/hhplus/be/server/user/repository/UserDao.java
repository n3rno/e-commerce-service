package kr.hhplus.be.server.user.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    int checkUserCountByUserNo(@Param("userNo") int userNo);// 이거였나
}
