package kr.co.bizmarvel.ccdm.vo.monitoring;

import kr.co.bizmarvel.ccdm.vo.common.CommonVO;

public class MonitoringVO extends CommonVO {

    private String deviceIdnfr;
    private String oxygen;
    private String carbon;
    private String methane;
    private String airCurrent;
    private String lat;
    private String lng;

    private int pagePerSize;

    private int pageIndex;

    public String getDeviceIdnfr() {
        return deviceIdnfr;
    }

    public void setDeviceIdnfr(String deviceIdnfr) {
        this.deviceIdnfr = deviceIdnfr;
    }

    public String getOxygen() {
        return oxygen;
    }

    public void setOxygen(String oxygen) {
        this.oxygen = oxygen;
    }

    public String getCarbon() {
        return carbon;
    }

    public void setCarbon(String carbon) {
        this.carbon = carbon;
    }

    public String getMethane() {
        return methane;
    }

    public void setMethane(String methane) {
        this.methane = methane;
    }

    public String getAirCurrent() {
        return airCurrent;
    }

    public void setAirCurrent(String airCurrent) {
        this.airCurrent = airCurrent;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getPagePerSize() {
        return pagePerSize;
    }

    public void setPagePerSize(int pagePerSize) {
        this.pagePerSize = pagePerSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
