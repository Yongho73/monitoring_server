package kr.co.bizmarvel.ccdm.vo.dashboard;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "observation_info")
public class ObservedRegiVO {
    private String deviceCode;
    private int deviceIdnfr;
    private String accessToken;
    private int in_Oxygen;
    private int in_Carbon;
    private int in_Methane;
    private int in_AirCurrent;
    private int out_Oxygen;
    private int out_Carbon;
    private int out_Methane;
    private int out_AirCurrent;
    private int st_Temp;
    private int st_Humi;
    private String observedDate;
    private String regDate;
    private String observedDateOrg;
}