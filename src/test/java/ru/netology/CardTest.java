package ru.netology;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.Stopwatch;
import com.codeborne.selenide.conditions.Disabled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardTest {

    @Test
    void formTest() throws InterruptedException {
        String meetDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        open("http://localhost:9999");
        SelenideElement form = $(new By.ByTagName("form"));
        form.$("[data-test-id=city] input").setValue("Томск");
        form.$("[data-test-id=date] input").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue("Иценко Галина");
        form.$("[data-test-id=phone] input").setValue("+79344953233");
        form.$("[data-test-id=agreement]").click();
        form.$(new By.ByClassName("button")).click();

        if (form.$(new By.ByTagName("fieldset")).is(new Disabled())) {
            Assertions.fail("Форма должна быть в состоянии загрузки");
        }

        Stopwatch timeout = new Stopwatch(15000);

        SelenideElement notification;
        while (!(notification = $(new By.ByClassName("notification_visible"))).exists() || timeout.isTimeoutReached()) {
            // ожидаем сообщения
        }

        if (timeout.isTimeoutReached()) {
            Assertions.fail("Превышено время ожидания ответа");
        }

        notification.$("[class=notification__content]").shouldHave(exactText("Встреча успешно забронирована на " + meetDate));
    }
}
