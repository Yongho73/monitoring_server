package kr.co.bizmarvel.ccdm.controller.dashboard;

import kr.co.bizmarvel.ccdm.component.utils.StringUtils;
import kr.co.bizmarvel.ccdm.mapper.dashboard.DashboardMapper;
import kr.co.bizmarvel.ccdm.vo.common.PaginationVO;
import kr.co.bizmarvel.ccdm.vo.common.ResponseVO;
import kr.co.bizmarvel.ccdm.vo.dashboard.DashboardVO;
import kr.co.bizmarvel.ccdm.vo.dashboard.ObservedRegiVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mongo/dashboard")
@Slf4j
public class DashboardMongoController {

    @Autowired
    DashboardMapper dashboardMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/getDeviceDetail")
    public ResponseVO getDeviceDetail(DashboardVO param, HttpServletRequest request) throws Exception {
        String deviceIdnfrParam = request.getParameter("deviceIdnfr");
        ResponseVO returnData = new ResponseVO();
        Map<String, Object> result = new HashMap<>();
        Query query = new Query();
        int cnt = 0;

        try {
            if (deviceIdnfrParam != null) {
                long deviceIdnfr = Long.parseLong(deviceIdnfrParam);
                // 조건
                query.addCriteria(Criteria.where("deviceIdnfr").is(deviceIdnfr));
                // 전체 건수 조회
                cnt = (int) mongoTemplate.count(Query.of(query), "observation_info");
                // 페이징 처리
                PaginationVO pagination = new PaginationVO(cnt, param.getPagePerSize(), param.getPageIndex());
                param.setStartIndex(pagination.getStartIndex());
                param.setLastIndex(pagination.getLastIndex());
                Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPagePerSize(), Sort.unsorted()); // page index is 0,1,2,3...
                // 정렬
                query.with(Sort.by(Sort.Direction.DESC, "observedDateOrg"));
                query.with(pageable);
                // 데이터 조회
                result.put("pagination", pagination);
                result.put("cnt", cnt);
                result.put("result", mongoTemplate.find(query, ObservedRegiVO.class));
                result.put("deviceInfo", dashboardMapper.selectDeviceInfo(deviceIdnfr));

                returnData.setCheck(true);
                returnData.setMessage("Success");
                returnData.setResponseData(result);

            } else {
                returnData.setCheck(false);
                returnData.setMessage("device info not found");
                returnData.setResponseData(result);
            }
        } catch (Exception e) {
            returnData.setCheck(false);
            returnData.setMessage(e.getMessage());
            returnData.setResponseData(result);
        }
        return returnData;
    }

    @PostMapping("/getDeviceDetailExcel")
    public ResponseEntity<InputStreamResource> getDeviceDetailExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String deviceIdnfrParam = request.getParameter("deviceIdnfr");
        String date = StringUtils.nvl(request.getParameter("date"), "");

        long deviceIdnfr = 0L;
        if(deviceIdnfrParam.isEmpty()) {
            return null;
        }

        deviceIdnfr = Long.parseLong(deviceIdnfrParam);

        Map<String, Object> deviceInfo = dashboardMapper.selectDeviceInfo(deviceIdnfr);

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = URLEncoder.encode(deviceInfo.get("deviceCode") + "_" + formatter.format(now), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        Map<String, Object> selectParam = new HashMap<>();
        selectParam.put("deviceCode", deviceInfo.get("deviceCode"));
        selectParam.put("date", date);

        Query query = new Query();
        query.addCriteria(Criteria.where("deviceIdnfr").is(deviceInfo.get("deviceIdnfr")));
        query.addCriteria(Criteria.where("observedDateOrg").regex("^" + date + ".*"));
        query.with(Sort.by(Sort.Direction.DESC, "observedDateOrg"));

        List<ObservedRegiVO> list = mongoTemplate.find(query, ObservedRegiVO.class);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(deviceInfo.get("deviceCode").toString());
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
        for (int i = 0; i < headerString.length; i++) {
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
        int cellIdx = 0;
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 2);
            cellIdx = 0;

            setExcelData(row, bodyStyle, list.get(i).getIn_Carbon(), cellIdx++);
            setExcelData(row, bodyStyle, convFlotToStr(list.get(i).getIn_Oxygen(), 2), cellIdx++);
            setExcelData(row, bodyStyle, list.get(i).getIn_AirCurrent(), cellIdx++);

            setExcelData(row, bodyStyle, list.get(i).getOut_Carbon(), cellIdx++);
            setExcelData(row, bodyStyle, convFlotToStr(list.get(i).getOut_Oxygen(), 2), cellIdx++);
            setExcelData(row, bodyStyle, list.get(i).getOut_AirCurrent(), cellIdx++);

            setExcelData(row, bodyStyle, (list.get(i).getIn_Carbon() - list.get(i).getOut_Carbon()), cellIdx++);
            setExcelData(row, bodyStyle, convFlotToStr(list.get(i).getOut_Oxygen() - list.get(i).getIn_Oxygen(), 2), cellIdx++);

            setExcelData(row, bodyStyle, list.get(i).getSt_Temp(), cellIdx++);
            setExcelData(row, bodyStyle, list.get(i).getSt_Humi(), cellIdx++);
            setExcelData(row, bodyStyle, list.get(i).getObservedDate(), cellIdx++);
        }

        for (int i = 0; i < bodyString.length; i++) sheet.autoSizeColumn(i);
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
            return ResponseEntity.ok().contentLength(tmpFile.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition", String.format("attachment;filename=\"%s.xlsx\";", fileName)).body(new InputStreamResource(res));
        } catch (Exception e1) {
            StringUtils.getStackTrace(e1);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    private String convFlotToStr(int num, int digit) {
        int convInt = num;
        double convDouble = Math.round(convInt) / Math.pow(10, digit);
        return String.format("%." + digit + "f", convDouble);
    }

    private Cell setExcelData(Row row, CellStyle bodyStyle, Object obj, int cellIdx) {
        Cell bCell = row.createCell(cellIdx);
        switch (obj.getClass().getName()) {
            case "java.lang.Long":
                bCell.setCellValue((long) obj);
                break;
            case "java.lang.Integer":
                bCell.setCellValue((int) obj);
                break;
            case "java.lang.String":
                bCell.setCellValue((String) obj);
                break;
            case "java.lang.Double":
                bCell.setCellValue((Double) obj);
                break;
            case "java.math.BigDecimal":
                bCell.setCellValue(String.valueOf(obj));
                break;
        }
        bCell.setCellStyle(bodyStyle);
        return bCell;
    }
}
