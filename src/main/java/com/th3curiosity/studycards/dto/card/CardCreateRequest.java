package com.th3curiosity.studycards.dto.card;

public class CardCreateRequest {
    private String front;
    private String back;

    public CardCreateRequest() {
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }
}
