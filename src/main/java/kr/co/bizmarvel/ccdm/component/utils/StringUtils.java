package kr.co.bizmarvel.ccdm.component.utils;

import kr.co.bizmarvel.ccdm.vo.device.DeviceCodeVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Clob;
import java.sql.SQLException;

public class StringUtils {

    /***
     * 디바이스 코드 객체화
     * @param deviceCode
     * @return
     */
    public static DeviceCodeVo convertDeviceCode(String deviceCode) throws IllegalArgumentException{
        if(!validDeviceCode(deviceCode)) {
            throw new IllegalArgumentException();
        }
        return new DeviceCodeVo(deviceCode);
    }

    /***
     * 디바이스 코드 검증
     * @param deviceCode
     * @return
     */
    public static boolean validDeviceCode(String deviceCode){
        String pattern = "^F" + // 모델구분
                "\\d{2}" + // 세대
                "\\d{2}" + // 설치 지역
                "[H|A|O|G|C|F|S|T|I]\\d{1}" + // 배출지역구분
                "\\d{4}" + // 일련번호
                "\\d{2}$"; // 거점번호

        if(deviceCode.matches(pattern)) {
            return true;
        } else{
            return false;
        }
    }

    /***
     * 문자열 비어있는지 여부
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if(str == null || str.equals("")) {
            return true;
        } else{
            return false;
        }
    }

    /***
     * 문자열 비어있지 않은지 여부
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        if(str == null || str.equals("")) {
            return false;
        } else{
            return true;
        }
    }

    /***
     * 문자열 null 체크 후 null일경우 대체 문자열로 대체
     * @param str
     * @param repStr
     * @return
     */
    public static String nvl(String str, String repStr) {
        if (null == str || "null".equals(str) || "".equals(str) || " ".equals(str)) {
            return repStr;
        } else {
            return ((String) str).trim();
        }
    }

    /***
     * 오브젝트 null 체크 후 null일경우 대체 문자열로 대체
     * @param str
     * @param repStr
     * @return
     */
    public static String nvl(Object str, String repStr) {
        if (str != null && str instanceof java.math.BigDecimal) {
            return ((BigDecimal) str).toString();
        }
        if (str != null && str instanceof java.lang.Long) {
            return ((Long) str).toString();
        }
        if (str != null && str instanceof java.lang.Integer) {
            return ((Integer) str).toString();
        }
        if (str != null && str instanceof java.lang.Double) {
            return ((Double) str).toString();
        }
        if (str != null && str instanceof Clob) {
            Clob tmpStr = (Clob) str;
            BufferedReader contentReader = null;
            try {
                contentReader = new BufferedReader(tmpStr.getCharacterStream());
                StringBuffer out = new StringBuffer();
                String dummy;
                while((dummy=contentReader.readLine()) != null) out.append(dummy);
                return out.toString();
            } catch (SQLException | IOException e) {
                return repStr;
            }
            finally {
                if (contentReader != null) {
                    try {
                        contentReader.close();
                    } catch (IOException e) {
                        return repStr;
                    }
                }
            }
        }
        if (null == str || "null".equals(str) || "".equals(str) || " ".equals(str)) {
            return repStr;
        } else {
            return ((String) str).trim();
        }
    }

    /***
     * 오류 스택을 String으로 변환
     * @param t
     * @return
     */
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /***
     * 랜덤 토큰 생성
     * @return
     */
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        SecureRandom rnd = new SecureRandom();
        while (salt.length() < 35) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

}
