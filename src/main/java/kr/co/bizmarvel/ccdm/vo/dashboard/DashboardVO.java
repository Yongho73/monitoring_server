package kr.co.bizmarvel.ccdm.vo.dashboard;

import kr.co.bizmarvel.ccdm.vo.common.CommonVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DashboardVO extends CommonVO {
    private String searchArea;
    private String searchCompany;
    private String searchExhaustType;
    private String searchName;
    private int pagePerSize;
    private int pageIndex;
    private String deviceCode;
    private long deviceIdnfr;
}
