package kr.co.bizmarvel.ccdm.mapper.code;

import kr.co.bizmarvel.ccdm.vo.code.CodeVO;
import kr.co.bizmarvel.ccdm.vo.weather.SidoVo;
import kr.co.bizmarvel.ccdm.vo.weather.WeatherVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CodeMapper {
    List<CodeVO> getCommCodeList(CodeVO param);

    List<CodeVO> getDeviceCompanyList(CodeVO param);
    List<Map<String, Object>> getSidoList();
    int insertWeatherInfo(WeatherVo weatherVo);
}
