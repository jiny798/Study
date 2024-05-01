package hello.itemservice.domain.item;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Item {
	private Long id;
	@NotBlank // 빈값 + 공백만 있는 경우를 허용하지 않는다.
	private String itemName;
	@NotNull // null 을 허용하지 않는다.
	@Range(min = 1000, max = 1000000) // 범위 안의 값이어야 한다.
	private Integer price;
	@NotNull
	@Max(9999) //  최대 9999까지만 허용한다.
	private Integer quantity;
	public Item() {
	}
	public Item(String itemName, Integer price, Integer quantity) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}
}