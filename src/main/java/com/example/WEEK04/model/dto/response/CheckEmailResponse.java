package com.example.WEEK04.model.dto.response;

public class CheckEmailResponse {
    private final String message;
    private final Data data;
    private final Error error;

    public CheckEmailResponse(String message, Data data, Error error) {
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public String getMessage() { return message; }
    public Data getData() { return data; }
    public Error getError() { return error; }

    public static class Data {
        private final boolean available;
        public Data(boolean available) { this.available = available; }
        public boolean isAvailable() { return available; }
    }

    public static class Error {
        private final String code;
        private final String detail;
        public Error(String code, String detail) {
            this.code = code;
            this.detail = detail;
        }
        public String getCode() { return code; }
        public String getDetail() { return detail; }
    }
}
