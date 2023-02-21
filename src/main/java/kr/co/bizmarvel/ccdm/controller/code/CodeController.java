package kr.co.bizmarvel.ccdm.controller.code;

import kr.co.bizmarvel.ccdm.mapper.code.CodeMapper;
import kr.co.bizmarvel.ccdm.vo.code.CodeVO;
import kr.co.bizmarvel.ccdm.vo.common.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/code")

public class CodeController {

     @Autowired
     CodeMapper codeMapper;

    @GetMapping("/getCommCodeList")
    public ResponseVO getCommCodeList(CodeVO param, HttpServletRequest request) throws Exception{

        ResponseVO returnData = new ResponseVO();
        Map<String, Object> result = new HashMap<>();

        try {

            List<CodeVO> test = codeMapper.getCommCodeList(param);

            result.put("result" , test);

            returnData.setCheck(true);
            returnData.setMessage("Success");
            returnData.setResponseData(result);

        }catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return returnData;
    }

    @GetMapping("/getDeviceCompanyList")
    public ResponseVO getDeviceCompanyList(CodeVO param, HttpServletRequest request) throws Exception{

        ResponseVO returnData = new ResponseVO();
        Map<String, Object> result = new HashMap<>();

        try {

            List<CodeVO> test = codeMapper.getDeviceCompanyList(param);

            result.put("result" , test);

            returnData.setCheck(true);
            returnData.setMessage("Success");
            returnData.setResponseData(result);

        }catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return returnData;
    }
}
