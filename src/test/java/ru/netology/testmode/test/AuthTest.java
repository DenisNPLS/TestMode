package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jdk.jfr.Registered;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.util.DataGenerator;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static ru.netology.testmode.util.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.util.DataGenerator.Registration.getUser;
import static ru.netology.testmode.util.DataGenerator.getRandomLogin;
import static ru.netology.testmode.util.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//input[@name='login']").setValue(registeredUser.getLogin());
        $x("//input[@name='password']").setValue(registeredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//*[contains(text(),'Личный кабинет')]").shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//input[@name='login']").setValue(notRegisteredUser.getLogin());
        $x("//input[@name='password']").setValue(notRegisteredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[contains(text(),'Ошибка')]").shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $x("//input[@name='login']").setValue(blockedUser.getLogin());
        $x("//input[@name='password']").setValue(blockedUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@data-test-id='error-notification']").shouldBe(Condition.text("Ошибка! Пользователь заблокирован"), Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x("//input[@name='login']").setValue(wrongLogin);
        $x("//input[@name='password']").setValue(registeredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@data-test-id='error-notification']").shouldBe(Condition.text("Ошибка! Неверно указан логин или пароль"),Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x("//input[@name='login']").setValue(registeredUser.getLogin());
        $x("//input[@name='password']").setValue(wrongPassword);
        $x("//button[@data-test-id='action-login']").click();
        $x("//div[@data-test-id='error-notification']").shouldBe(Condition.text("Ошибка! Неверно указан логин или пароль"),Condition.visible);
    }
}
