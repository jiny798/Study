package hello.itemservice.subject.validation.v1;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.subject.demo.Item;
import hello.itemservice.subject.demo.ItemRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {
	private final ItemRepository itemRepository;

	@PostMapping("/add")
	public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

		//검증 오류 결과를 보관
		Map<String, String> errors = new HashMap<>();
		//검증 로직
		if (!StringUtils.hasText(item.getItemName())) {
			errors.put("itemName", "상품 이름은 필수입니다.");
		}
		if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
			errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
		}
		if (item.getQuantity() == null || item.getQuantity() > 9999) {
			errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
		}
		//특정 필드가 아닌 복합 룰 검증
		if (item.getPrice() != null && item.getQuantity() != null) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if (resultPrice < 10000) {
				errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
			}
		}
		//검증에 실패하면 다시 입력 폼으로
		if (!errors.isEmpty()) {
			model.addAttribute("errors", errors);
			return "validation/v1/addForm";
		}

		//성공 로직
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v1/items/{itemId}";
	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable("itemId") Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/v1/item";
	}

	@GetMapping("/add")
	public String addForm(@ModelAttribute Item item) {
		return "validation/v1/addForm";
	}
}
