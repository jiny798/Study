package hello.itemservice.subject.demo.basic;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.subject.demo.Item;
import hello.itemservice.subject.demo.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
	private final ItemRepository itemRepository;

	/**
	 * 컨트롤러의 의존 관계가 모두 주입되고, 호출된다. -> 초기화 용도
	 */
	@PostConstruct
	public void init() {
		itemRepository.save(new Item("testA", 10000, 10));
		itemRepository.save(new Item("testB", 20000, 20));
	}

	@GetMapping
	public String items(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);
		return "basic/items";
	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable("itemId") Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "basic/item";
	}

	@GetMapping("/add")
	public String addForm() {
		return "basic/addForm";
	}

	// @PostMapping("/add")
	// public String addItemV1(@RequestParam String itemName,
	// 	@RequestParam int price,
	// 	@RequestParam Integer quantity,
	// 	Model model) {
	// 	Item item = new Item();
	// 	item.setItemName(itemName);
	// 	item.setPrice(price);
	// 	item.setQuantity(quantity);
	// 	itemRepository.save(item);
	// 	model.addAttribute("item", item);
	// 	return "basic/item";
	// }

	/**
	 * @ModelAttribute("item") Item item
	 * model.addAttribute("item", item); 자동 추가
	 * 필드명이 같다면 name 생략 가능, ModelAttribute 전체 생략은 부트3.0 이상에서 추가 설정 필요
	 */
	// @PostMapping("/add")
	// public String addItemV2(@ModelAttribute("item") Item item, Model model) {
	// 	itemRepository.save(item);
	// 	//model.addAttribute("item", item); //자동 추가, 생략 가능
	// 	// return "basic/item";
	// 	return "redirect:/basic/items/" + item.getId();
	// }

	/**
	 * RedirectAttributes
	 */
	@PostMapping("/add")
	public String addItemV6(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/basic/items/{itemId}";
	}

	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "basic/editForm";
	}

	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
		itemRepository.update(itemId, item);
		return "redirect:/basic/items/{itemId}";
	}

}