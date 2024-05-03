package hello.itemservice.domain.item;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {
	MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

	@Test
	void messageCodesResolverObject() {
		String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        // errorCode -> required
        // objectName -> item

		assertThat(messageCodes).containsExactly("required.item", "required");
	}

	@Test
	void messageCodesResolverField() {
		String[] messageCodes = codesResolver.resolveMessageCodes("required",
			"item", "itemName", String.class);
        // errorCode -> required
        // objectName -> item
        // field itemName
		assertThat(messageCodes).containsExactly(
			"required.item.itemName",
			"required.itemName",
			"required.java.lang.String",
			"required"
		);
	}
}