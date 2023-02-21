package kr.co.bizmarvel.ccdm.mapper.dashboard;

import kr.co.bizmarvel.ccdm.vo.dashboard.DashboardVO;
import kr.co.bizmarvel.ccdm.vo.device.DeviceExcelVO;
import kr.co.bizmarvel.ccdm.vo.device.DeviceVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DashboardMapper {

    int getDeviceListCnt(DashboardVO param ) throws DataAccessException;

    List<DeviceVO> getDeviceList(DashboardVO param) throws DataAccessException;

    List<DeviceExcelVO> getDeviceExcelList(DashboardVO param) throws DataAccessException;

    int getObservationCnt(DashboardVO param ) throws Exception;

    List<Map<String, Object>> getObservationList(DashboardVO param) throws Exception;
    List<Map<String, Object>> getObservationListForExcel(Map<String, Object> param) throws Exception;

    Map<String, Object> selectDeviceInfo(long deviceIdnfr) throws DataAccessException;
}
