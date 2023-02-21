package kr.co.bizmarvel.ccdm.controller.monitoring;

import kr.co.bizmarvel.ccdm.mapper.monitoring.MonitoringMapper;
import kr.co.bizmarvel.ccdm.vo.common.PaginationVO;
import kr.co.bizmarvel.ccdm.vo.common.ResponseVO;
import kr.co.bizmarvel.ccdm.vo.dashboard.DashboardExcelVO;
import kr.co.bizmarvel.ccdm.vo.monitoring.MonitoringVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MonitoringController {

    @Autowired
    MonitoringMapper monitoringMapper;

    @GetMapping("/getMonitoringList")
    public ResponseVO getMonitoringList(MonitoringVO param, HttpServletRequest request) throws Exception{
        ResponseVO returnData = new ResponseVO();
        Map<String, Object> result = new HashMap<>();
        try {

            int cnt = monitoringMapper.getMonitoringListCnt(param);

            PaginationVO pagination = new PaginationVO(cnt , param.getPagePerSize() , param.getPageIndex());

            param.setStartIndex(pagination.getStartIndex());
            param.setLastIndex(pagination.getLastIndex());

            List<MonitoringVO> test = monitoringMapper.getMonitoringList(param);

            result.put("pagination" , pagination);
            result.put("cnt" , cnt);
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

    @GetMapping("/getMonitoringExcelList")
    public ResponseVO getMonitoringExcelList( MonitoringVO param, HttpServletRequest request) throws Exception{
        ResponseVO returnData = new ResponseVO();
        Map<String, Object> result = new HashMap<>();
        try {

            List<DashboardExcelVO> test = monitoringMapper.getMonitoringExcelList(param);
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
