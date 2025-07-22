package com.bigbank.test.pages;

import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;

public class Form {

    public final SelenideElement formPage = $(".bb-application-page");
    public final SelenideElement calculatorButton = $(".bb-edit-amount");
    public final SelenideElement loanAmount = $(".bb-edit-amount__amount");

    public void openCalculator() {
        calculatorButton.click();
    }
}
