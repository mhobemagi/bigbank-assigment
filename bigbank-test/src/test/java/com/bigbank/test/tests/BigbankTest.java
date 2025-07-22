package com.bigbank.test.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bigbank.test.pages.Calculator;
import com.bigbank.test.pages.Form;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import com.codeborne.selenide.Configuration;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

public class BigbankTest {

    // määrame testide seadistused
    // baseUrl on Bigbanki laenutaotluse leht, mida testime
    static {
        Configuration.baseUrl = "https://laenutaotlus.bigbank.ee/?amount=5000&period=60&productName=SMALL_LOAN&loanPurpose=DAILY_SETTLEMENTS";
        Configuration.browser = "chrome";
        Configuration.headless = false;
    }

    // lehe elemendid, mida kasutame kalkulaatori ja vormi testimiseks
    private Calculator calculator;
    private Form form;

    // enne igat testi loome uue Calculator ja Form objekti ning avame lehe
    // kontrollime, et kalkulaatori modaalaken on nähtav
    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
        form = new Form();
        open("/");
        calculator.calculatorModal.shouldBe(visible);
    }   

    @Test
    public void amountsNotSaved_whenCalculatorIsClosed() {
        String defaultLoanAmount = "5,000";
        String defaultMonths = "60";
        String loanAmount = "10000";
        String months = "24";

        sleep(2000);
        String monthlyPayment = $(".bb-labeled-value__value").getText(); // salvestame algse igakuise makse
        calculator.increaseLoanAmount(loanAmount);
        calculator.increaseMonths(months);
        calculator.loanAmount.shouldHave(value("10,000"));
        calculator.months.shouldHave(value(months));
        calculator.closeCalculator();
        // sulgeme kalkulaator, kontrollime, et laenusumma pole salvestatud
        form.formPage.shouldBe(visible);
        form.loanAmount.shouldHave(text("5000"));
        form.openCalculator();
        // avame kalkulaatori uuesti ja kontrollime, et laenusumma on sama nagu algselt
        calculator.calculatorModal.shouldBe(visible);
        calculator.loanAmount.shouldHave(value(defaultLoanAmount));
        calculator.months.shouldHave(value(defaultMonths));
        calculator.monthlyPayment.shouldHave(text(monthlyPayment)); // kontrollime, et igakuine makse on sama nagu algselt
    }

    @Test
    public void amountsAreSaved_whenJatkaButtonIsClicked() {
        String loanAmount = "10000";
        String months = "24";
        calculator.increaseLoanAmount(loanAmount);
        calculator.increaseMonths(months);
        calculator.loanAmount.shouldHave(value("10,000"));
        calculator.months.shouldHave(value(months));
        calculator.monthlyPayment.click();

        sleep(2000);
        String newMonthlyPayment = $(".bb-labeled-value__value").getText(); // salvestame uue igakuise makse
        calculator.saveAmounts();
        // vajutame Jätka nuppu, kontrollime, et laenusumma on salvestatud
        form.formPage.shouldBe(visible);
        form.loanAmount.shouldHave(text(loanAmount));
        form.openCalculator();
        // avame kalkulaatori uuesti ja kontrollime, et laenusumma on sama mis sisestasime
        calculator.calculatorModal.shouldBe(visible);
        calculator.loanAmount.shouldHave(value("10,000"));
        calculator.months.shouldHave(value(months));
        calculator.monthlyPayment.shouldHave(text(newMonthlyPayment)); // kontrollime, et igakuine makse on samuti uuendatud
    }

    @Test
    public void negativeAmounts_areNotAllowed() {
        String loanAmount = "-2500";
        String months = "-10";
        calculator.increaseLoanAmount(loanAmount);
        calculator.increaseMonths(months);
        calculator.monthlyPayment.click();
        calculator.loanAmount.shouldHave(value("2,500"));
        calculator.months.shouldHave(value("10"));
    }

    @Test
    public void belowMinimumAmounts_areNotAllowed() {
        String minimumLoanAmount = "500";
        String minimumMonths = "6";
        String loanAmount = "250";
        String months = "1";
        calculator.increaseLoanAmount(loanAmount);
        calculator.increaseMonths(months);
        calculator.monthlyPayment.click();
        calculator.loanAmount.shouldHave(value(minimumLoanAmount));
        calculator.months.shouldHave(value(minimumMonths));
    }

    @Test
    public void aboveMaximumAmounts_areNotAllowed() {
        String maximumLoanAmount = "30,000";
        String maximumMonths = "120";
        String loanAmount = "100000";
        String months = "300";
        calculator.increaseLoanAmount(loanAmount);
        calculator.increaseMonths(months);
        calculator.monthlyPayment.click();
        calculator.loanAmount.shouldHave(value(maximumLoanAmount));
        calculator.months.shouldHave(value(maximumMonths));
    }

    @Test
    public void characters_areNotAllowed() {
        String loanAmount = "abc";
        String months = "xyz";
        calculator.increaseLoanAmount(loanAmount);
        calculator.increaseMonths(months);
        calculator.monthlyPayment.click();
        calculator.loanAmount.shouldNotHave(value(loanAmount));
        calculator.months.shouldNotHave(value(months));
    }
}