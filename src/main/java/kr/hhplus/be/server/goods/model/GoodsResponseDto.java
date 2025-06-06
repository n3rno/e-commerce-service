package kr.hhplus.be.server.goods.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

@AllArgsConstructor
@Getter
@Alias("Goods")
public class GoodsResponseDto {

    	private long no;
		private String name;
		private long price;
		private long stock;
		private long remainStock;

}
