## 검증 

### 1. BindingResult, FieldError

```JAVA
public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult)
```
! BindingResult 파라미터는 검증하는 ModelAttribute 뒤에 와야한다.


[예시]
```java
if (!StringUtils.hasText(item.getItemName())) {
    bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
}
```
이런식으로 직접 코드로 검증하고, BindingResult 에 에러를 추가하는 방법

```java
public FieldError(String objectName, String field, String defaultMessage) {} 
```
- ojbectName 은  @ModelAttribute가 적용된 변수 이름
- field는 오류가 발생한 필드의 변수이름
- defaultMessage 는 기본 오류 메시지를 설정한다.

<br>
특정 필드가 아닌, 특정 조건일 경우 ObjectError를 추가한다.

[예시]
```java
bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야합니다. 현재 값 = " + resultPrice));
```
```java
public ObjectError(String objectName, String defaultMessage) {}
```
- ojbectName 은  @ModelAttribute가 적용된 변수 이름
- defaultMessage 는 기본 오류 메시지를 설정한다.

타임리프

[예시]
```html
```html
<form action="item.html" th:action th:object="${item}" method="post">
    <div th:if="${#fields.hasGlobalErrors()}">
        <p class="field-error" th:each="err : ${#fields.globalErrors()}"
           th:text="${err}">글로벌 오류 메시지</p>
    </div>

    <div>
        <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
        <input type="text" id="itemName" th:field="*{itemName}"
               th:errorclass="field-error" class="form-control" placeholder="이름을 입력하세요">
        <div class="field-error" th:errors="*{itemName}">
            상품명 오류
        </div>
    </div>

```
- #fields : #fields.hasGlobalErrors() 로 BindingResult 가 제공하는 검증 오류에 접근할 수 있다.
- th:errors : 해당 필드에 오류가 있는 경우에 태그를 출력한다. (th:if와 유사)
- th:errorclass : th:field 에서 지정한 필드에 오류가 있으면 class 정보를 추가한다