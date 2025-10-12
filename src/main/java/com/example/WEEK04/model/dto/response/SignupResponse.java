package com.example.WEEK04.model.dto.response;

public class SignupResponse {
    private String message;
    private Object data;
    private Object error;

    public SignupResponse(String message, Object data, Object error) {
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public String getMessage() { return message; }
    public Object getData() { return data; }
    public Object getError() { return error; }
}
