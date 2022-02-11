import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.Cookie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;


public class TestDemowebshop {

    @BeforeAll
    static void initBaseURIandURL() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }

    @Test
    @DisplayName("Создание отзыва на товар")
    void leaveReviewOnProductTest() {
        step("Авторизация на сайте и полученение кук", () -> {
                    String authorizationCookie =
                            given()
                                    .filter(customLogFilter().withCustomTemplates())
                                    .log().uri()
                                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                                    .formParam("Email", "tttt@mail.ru")
                                    .formParam("Password", "tttt66")
                                    .when()
                                    .post("/login")
                                    .then()
                                    .statusCode(302)
                                    .extract()
                                    .cookie("NOPCOMMERCE.AUTH");

                    step("Загрузка минимального контента для установки кук в браузер", () ->
                        open("/Themes/DefaultClean/Content/images/logo.png"));

                        step("Установка кук в браузер", () ->
                                getWebDriver().manage().addCookie(
                                        new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
        });

        step("Открытие страницы товара", () ->
                open("/simple-computer"));

        step("Нажатие на кнопку, чтобы оставить отзыв", () ->
                $x("//a[text()='Add your review']").click());

        step("Заполнить поле для ввода заголовка отзыва", () ->
                $(".review-title").setValue("Заголовок"));

        step("Ввести текст отзыва в поле", () ->
                $(".review-text").setValue("Текст отзыва"));

        step("Нажать на кнопку 'Оставить отзыв'", () ->
                $("input[name='add-review']").click());

        step("Проверить, что отобразился текст об успешном добавлении отзыва", () ->
                $(".result").shouldHave(text("Product review is successfully added")));

    }
}
