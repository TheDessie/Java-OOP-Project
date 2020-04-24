package server.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Card implements Serializable {
    private String cardNumber;
    private String cardToken;

    public Card() {
        this.cardNumber = "";
        this.cardToken = "";
    }

    public Card(String cardNumber, String cardToken) {
        this.cardNumber = cardNumber;
        this.cardToken = cardToken;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }
}
