package kr.hhplus.be.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan(value = {"kr.hhplus.be.server.user.repository", "kr.hhplus.be.server.point.repository", "kr.hhplus.be.server.goods.repository", "kr.hhplus.be.server.order.repository"})
@SpringBootApplication
@MapperScan("kr.hhplus.be.server.**.infrastructure.persistence.mapper")
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
