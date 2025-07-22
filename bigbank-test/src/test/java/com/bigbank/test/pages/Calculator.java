package com.bigbank.test.pages;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import com.codeborne.selenide.SelenideElement;

public class Calculator {

    // lehe elemendid, mida kasutame kalkulaatori modaalaknas
    public final SelenideElement calculatorModal = $(".bb-modal");
    public final SelenideElement closeButton = $(".bb-modal__close");
    public final SelenideElement loanAmount = $(By.name("header-calculator-amount"));
    public final SelenideElement months = $(By.name("header-calculator-period"));
    public final SelenideElement monthlyPayment = $(".bb-labeled-value__value");
    public final SelenideElement jatkaButton = $(".bb-calculator-modal__submit-button");

    public void closeCalculator() {
        closeButton.click();
    }

    // suurendame laenusummat ja perioodi, mille kasutaja sisestab
    // kasutame executeJavaScript, et eemaldada eelnev väärtus sisestusväljadel
    public void increaseLoanAmount(@Nullable String amount) {
        executeJavaScript("arguments[0].value = '';", $(By.name("header-calculator-amount")));
        loanAmount.setValue(amount);
    }

    public void increaseMonths(@Nullable String amount) {
        executeJavaScript("arguments[0].value = '';", $(By.name("header-calculator-period")));
        months.setValue(amount);
    }

    public void saveAmounts() {
        jatkaButton.click();
    }
}