package kr.co.bizmarvel.ccdm.mapper.monitoring;

import kr.co.bizmarvel.ccdm.vo.dashboard.DashboardExcelVO;
import kr.co.bizmarvel.ccdm.vo.monitoring.MonitoringVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MonitoringMapper {

    int getMonitoringListCnt(MonitoringVO param) throws DataAccessException;
    List<MonitoringVO> getMonitoringList(MonitoringVO param ) throws DataAccessException;

    List<DashboardExcelVO> getMonitoringExcelList(MonitoringVO param) throws DataAccessException;


}
