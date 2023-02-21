package kr.co.bizmarvel.ccdm.vo.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RestResult {
    private boolean success;
    private String message;
    private Map<String, Object> data;
    private List<?> list;
}
