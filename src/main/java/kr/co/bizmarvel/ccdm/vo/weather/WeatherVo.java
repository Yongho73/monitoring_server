package kr.co.bizmarvel.ccdm.vo.weather;

import lombok.Data;
import org.apache.ibatis.annotations.ConstructorArgs;

import java.math.BigDecimal;

@Data
public class WeatherVo implements Cloneable{
    private String city;

    /*날씨*/
    private String weather;
    private String description;
    private String icon;
    private String icon_title;
    private double temp;
    private int humidity;

    /*대기오염*/
    private double co;
    private double no;
    private double no2;
    private double o3;
    private double so2;
    private double pm2_5;
    private double pm10;
    private double nh3;

    /*CO2*/
    private int carbonIntensity;
    private double fossilFuelPercentage;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}