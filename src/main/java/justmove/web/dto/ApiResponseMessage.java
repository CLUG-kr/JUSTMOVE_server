package justmove.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class ApiResponseMessage<T> {
    private final int statusCode;
    private String message;
    private T data;

    public ApiResponseMessage(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public ApiResponseMessage(T data) {
        this.data = data;
        this.statusCode = 200;
    }

    public static ApiResponseMessage<MessageResponse> success(int statusCode) {
        return new ApiResponseMessage<>(statusCode, "", new MessageResponse("success"));
    }

    public static ApiResponseMessage<MessageResponse> fail(int statusCode, String failureMessage) {
        return new ApiResponseMessage<>(statusCode, failureMessage, null);
    }

    public static <T> ApiResponseMessage<T> data(int statusCode, T data) {
        return new ApiResponseMessage<T>(statusCode, "", data);
    }

    @RequiredArgsConstructor
    @ToString
    static class MessageResponse {
        private final String msg;
    }

}