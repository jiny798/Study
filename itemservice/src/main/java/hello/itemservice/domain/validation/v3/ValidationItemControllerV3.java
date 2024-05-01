package hello.itemservice.domain.validation.v3;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.validation.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {
	private final ItemRepository itemRepository;

	// private final ItemValidator itemValidator;
	//
	// @InitBinder // @InitBinder 해당 컨트롤러에만 영향을 준다. 글로벌 설정은 별도로 해야한다.
	// public void init(WebDataBinder dataBinder) {
	// 	log.info("init binder {}", dataBinder);
	// 	dataBinder.addValidators(itemValidator);
	// }

	@PostMapping("/add")
	public String addItem(@Validated @ModelAttribute Item item, BindingResult
		bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.info("errors={}", bindingResult);
			return "validation/v3/addForm";
		}
		//성공 로직
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v3/items/{itemId}";
	}

	@GetMapping
	public String items(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);
		return "validation/v3/items";
	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable("itemId") Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/v3/item";
	}

	@GetMapping("/add")
	public String addForm(@ModelAttribute Item item) {
		return "validation/v3/addForm";
	}
}
