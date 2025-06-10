package kr.hhplus.be.server.user.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int countUserByUserNo(@Param("userNo") int userNo);
}
