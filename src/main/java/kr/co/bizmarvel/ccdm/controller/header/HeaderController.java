package kr.co.bizmarvel.ccdm.controller.header;

import com.fasterxml.jackson.databind.ObjectMapper;
//import kr.co.bizmarvel.ccdm.scheduler.WeatherScheduler;
import kr.co.bizmarvel.ccdm.vo.common.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/header")
public class HeaderController {

    @RequestMapping("/getWeather")
    public ResponseVO getWeather(HttpServletRequest req){
        ResponseVO responseVO = new ResponseVO();

        String city = req.getParameter("city");

        ObjectMapper objectMapper = new ObjectMapper();
        //responseVO.setResponseData(objectMapper.convertValue(WeatherScheduler.weatherInfo.get(city), Map.class));

        return responseVO;
    }
}
