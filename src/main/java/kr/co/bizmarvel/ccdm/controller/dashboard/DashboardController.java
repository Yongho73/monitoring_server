package kr.co.bizmarvel.ccdm.controller.dashboard;

import kr.co.bizmarvel.ccdm.component.utils.StringUtils;
import kr.co.bizmarvel.ccdm.mapper.dashboard.DashboardMapper;
import kr.co.bizmarvel.ccdm.vo.common.PaginationVO;
import kr.co.bizmarvel.ccdm.vo.common.ResponseVO;
import kr.co.bizmarvel.ccdm.vo.dashboard.DashboardVO;
import kr.co.bizmarvel.ccdm.vo.device.DeviceExcelVO;
import kr.co.bizmarvel.ccdm.vo.device.DeviceVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@Slf4j
public class DashboardController {

    @Autowired
    DashboardMapper dashboardMapper;

    @GetMapping("/getDeviceList")
    public ResponseVO getDeviceList(DashboardVO param, HttpServletRequest request) throws Exception{

        ResponseVO returnData = new ResponseVO();
        Map<String, Object> result = new HashMap<>();

        try {

            int cnt = dashboardMapper.getDeviceListCnt(param);

            PaginationVO pagination = new PaginationVO(cnt , param.getPagePerSize() , param.getPageIndex());

            param.setStartIndex(pagination.getStartIndex());
            param.setLastIndex(pagination.getLastIndex());

            List<DeviceVO> test = dashboardMapper.getDeviceList(param);

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

    @GetMapping("/getDeviceExcelList")
    public ResponseVO getDeviceExcelList( DashboardVO param, HttpServletRequest request) throws Exception{

        ResponseVO returnData = new ResponseVO();
        Map<String, Object> result = new HashMap<>();

        try {

            List<DeviceExcelVO> test = dashboardMapper.getDeviceExcelList(param);
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

    @GetMapping("/getDeviceDetail")
    public ResponseVO getDeviceDetail(DashboardVO param, HttpServletRequest request) throws Exception{

        ResponseVO returnData = new ResponseVO();
        Map<String, Object> result = new HashMap<>();

        String deviceIdnfrParam = request.getParameter("deviceIdnfr");

        try {
            if(StringUtils.isEmpty(deviceIdnfrParam)) {
                return  null;
            }
            long deviceIdnfr = Long.parseLong(deviceIdnfrParam);

            param.setDeviceIdnfr(deviceIdnfr);

            int cnt = dashboardMapper.getObservationCnt(param);

            PaginationVO pagination = new PaginationVO(cnt , param.getPagePerSize() , param.getPageIndex());

            param.setStartIndex(pagination.getStartIndex());
            param.setLastIndex(pagination.getLastIndex());

            List<Map<String, Object>> test = dashboardMapper.getObservationList(param);

            result.put("pagination" , pagination);
            result.put("cnt" , cnt);
            result.put("result" , test);
            result.put("deviceInfo", dashboardMapper.selectDeviceInfo(deviceIdnfr));

            returnData.setCheck(true);
            returnData.setMessage("Success");
            returnData.setResponseData(result);

        }catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return returnData;
    }

    @PostMapping("/getDeviceDetailExcel")
    public ResponseEntity<InputStreamResource> getDeviceDetailExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String deviceCode = request.getParameter("deviceCode");
        String date = request.getParameter("date");
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = URLEncoder.encode(deviceCode + "_" + formatter.format(now), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        Map<String, Object> selectParam = new HashMap<>();
        selectParam.put("deviceCode", deviceCode);
        selectParam.put("date", date);

        List<Map<String, Object>> list = dashboardMapper.getObservationListForExcel(selectParam);

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(deviceCode);

        Row row = sheet.createRow(0);
        Cell hCell = null;
        CellStyle headerStyle = workbook.createCellStyle();

        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        hCell = row.createCell(0);
        hCell.setCellValue("IN");
        hCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 5));
        hCell = row.createCell(3);
        hCell.setCellValue("OUT");
        hCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
        hCell = row.createCell(6);
        hCell.setCellValue("Variation");
        hCell.setCellStyle(headerStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
        hCell = row.createCell(8);
        hCell.setCellValue("Temp (°C)");
        hCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
        hCell = row.createCell(9);
        hCell.setCellValue("Humi (ppm)");
        hCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
        hCell = row.createCell(10);
        hCell.setCellValue("Date");
        hCell.setCellStyle(headerStyle);

        row = sheet.createRow(1);
        String[] headerString = {"CO₂ (ppm)", "O₂ (%)", "LPM (lpm)", "CO₂ (ppm)", "O₂ (%)", "APM (lpm)", "CO₂ (ppm)", "O₂ (%)"};
        for(int i = 0; i < headerString.length; i++) {
            hCell = row.createCell(i); //  위 0번째 로우에 i번째 셀을 생성하겠다
            hCell.setCellValue(headerString[i]);
            hCell.setCellStyle(headerStyle); // 스타일 객체넣어서 셀에 스타일 입히기
        }
        hCell = row.createCell(8);
        hCell.setCellStyle(headerStyle);
        hCell = row.createCell(9);
        hCell.setCellStyle(headerStyle);
        hCell = row.createCell(10);
        hCell.setCellStyle(headerStyle);

        CellStyle bodyStyle = workbook.createCellStyle();

        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        //bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        String[] bodyString = {"in_Carbon", "in_Oxygen", "in_AirCurrent", "out_Carbon", "out_Oxygen", "out_AirCurrent", "diff_oxygen", "diff_carbon", "st_Temp", "st_Humi", "observedDate"};
        Cell bCell = null;
        for(int i = 0; i < list.size(); i ++) {
            row = sheet.createRow(i + 2);
            for(int j = 0; j < bodyString.length; j++) {
                bCell = row.createCell(j);
                switch (list.get(i).get(bodyString[j]).getClass().getName()) {
                    case "java.lang.Long": bCell.setCellValue((long) list.get(i).get(bodyString[j]));break;
                    case "java.lang.Integer": bCell.setCellValue((int) list.get(i).get(bodyString[j]));break;
                    case "java.lang.String": bCell.setCellValue((String) list.get(i).get(bodyString[j]));break;
                    case "java.lang.Double": bCell.setCellValue((Double) list.get(i).get(bodyString[j]));break;
                    case "java.math.BigDecimal": bCell.setCellValue(String.valueOf(list.get(i).get(bodyString[j])));break;
                }
                bCell.setCellStyle(bodyStyle);
            }
        }

        for(int i = 0; i < bodyString.length; i++)
            sheet.autoSizeColumn(i);
        CellStyle style = workbook.createCellStyle();

        // 위에서 만든 Workbook 객체를 받아온다
        try {
            File tmpFile = File.createTempFile("TMP~", ".xlsx");
            try (OutputStream fos = new FileOutputStream(tmpFile)) {
                workbook.write(fos);
            }
            InputStream res = new FileInputStream(tmpFile) {
                @Override
                public void close() throws IOException {
                    super.close();
                    if (tmpFile.delete()) {
                        log.info("엑셀 임시파일 삭제완료");
                    }
                }
            };
            return ResponseEntity.ok()
                    .contentLength(tmpFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", String.format("attachment;filename=\"%s.xlsx\";", fileName))
                    .body(new InputStreamResource(res));
        } catch(Exception e1) {
            StringUtils.getStackTrace(e1);
            return ResponseEntity.internalServerError()
                    .body(null);
        }
    }
}
