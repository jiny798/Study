package hello.itemservice.domain.validation.v2;

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
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {
	private final ItemRepository itemRepository;

	private final ItemValidator itemValidator;

	@InitBinder // @InitBinder 해당 컨트롤러에만 영향을 준다. 글로벌 설정은 별도로 해야한다.
	public void init(WebDataBinder dataBinder) {
		log.info("init binder {}", dataBinder);
		dataBinder.addValidators(itemValidator);
	}

	// BindingResult FieldError ObjectError 사용
	// @PostMapping("/add")
	public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (!StringUtils.hasText(item.getItemName())) {
			bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
		}

		if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >
			1000000) {
			bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
		}

		if (item.getQuantity() == null || item.getQuantity() >= 10000) {
			bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
		}
		//특정 필드 예외가 아닌 전체 예외
		if (item.getPrice() != null && item.getQuantity() != null) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
			}
		}
		if (bindingResult.hasErrors()) {
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}
		//성공 로직
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v2/items/{itemId}";
	}

	// 오류 발생시, 필드값 유지 
	// @PostMapping("/add")
	public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,
		RedirectAttributes redirectAttributes) {
		if (!StringUtils.hasText(item.getItemName())) {
			bindingResult.addError(new FieldError("item", "itemName",
				item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
		}
		if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >
			1000000) {
			bindingResult.addError(new FieldError("item", "price", item.getPrice(),
				false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
		}
		if (item.getQuantity() == null || item.getQuantity() >= 10000) {
			bindingResult.addError(new FieldError("item", "quantity",
				item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다."));
		}
		//특정 필드 예외가 아닌 전체 예외
		if (item.getPrice() != null && item.getQuantity() != null) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량 의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
			}
		}
		if (bindingResult.hasErrors()) {
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}
		//성공 로직
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v2/items/{itemId}";
	}

	// errors.properties 에 있는 코드 사용
	// @PostMapping("/add")
	public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult,
		RedirectAttributes redirectAttributes) {
		if (!StringUtils.hasText(item.getItemName())) {
			bindingResult.addError(new FieldError("item", "itemName",
				item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
		}
		if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >
			1000000) {
			bindingResult.addError(new FieldError("item", "price", item.getPrice(),
				false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
		}
		if (item.getQuantity() == null || item.getQuantity() > 10000) {
			bindingResult.addError(new FieldError("item", "quantity",
				item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]
				{9999}, null));
		}
		//특정 필드 예외가 아닌 전체 예외
		if (item.getPrice() != null && item.getQuantity() != null) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.addError(new ObjectError("item", new String[]
					{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
			}
		}
		if (bindingResult.hasErrors()) {
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}
		//성공 로직
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v2/items/{itemId}";
	}

	// rejectValue() , reject() 사용하여
	// errors.properties 에 있는 코드를 직접 입력하지 않았도 작동
	// @PostMapping("/add")
	public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult,
		RedirectAttributes redirectAttributes) {
		log.info("objectName={}", bindingResult.getObjectName());
		log.info("target={}", bindingResult.getTarget());
		if (!StringUtils.hasText(item.getItemName())) {
			bindingResult.rejectValue("itemName", "required");
		}
		if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >
			1000000) {
			bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000},
				null);
		}
		if (item.getQuantity() == null || item.getQuantity() > 10000) {
			bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
		}
		//특정 필드 예외가 아닌 전체 예외
		if (item.getPrice() != null && item.getQuantity() != null) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.reject("totalPriceMin", new Object[]{10000,
					resultPrice}, null);
			}
		}
		if (bindingResult.hasErrors()) {
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}
		//성공 로직
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v2/items/{itemId}";
	}


	//검증기 직접 추가
	// @PostMapping("/add")
	public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult,
		RedirectAttributes redirectAttributes) {
		itemValidator.validate(item, bindingResult);

		if (bindingResult.hasErrors()) {
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}
		//성공 로직
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v2/items/{itemId}";
	}

	// Validated -> WebDataBinder 에 등록한 검증기를 찾아서 실행한다
	// 그런데 여러 검증기를 등록한다
	// 면 그 중에 어떤 검증기가 실행되어야 할지 구분이 필요하다. 이때 supports() 가 사용된다. 여기서는
	// supports(Item.class) 호출되고, 결과가 true 이므로 ItemValidator 의 validate() 가 호출된다
	 @PostMapping("/add")
	public String addItemV6(@Validated @ModelAttribute Item item, BindingResult
		bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}
		//성공 로직
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v2/items/{itemId}";
	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable("itemId") Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/v2/item";
	}

	@GetMapping("/add")
	public String addForm(@ModelAttribute Item item) {
		return "validation/v2/addForm";
	}
}
