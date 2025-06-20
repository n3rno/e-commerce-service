package kr.hhplus.be.server.goods.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

@AllArgsConstructor
@Getter
public class GoodsResponseDto {

    	private long no;
		private String name;
		private long price;
		private long stock;
		private long remainStock;

}
