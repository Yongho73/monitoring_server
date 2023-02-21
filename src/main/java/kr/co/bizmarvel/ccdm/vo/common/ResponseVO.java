package kr.co.bizmarvel.ccdm.vo.common;

import java.util.Map;

public class ResponseVO {
    private boolean check ;
    private String message;
    private Map<String, Object> responseData;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getResponseData() {
        return responseData;
    }

    public void setResponseData(Map<String, Object> responseData) {
        this.responseData = responseData;
    }
}
