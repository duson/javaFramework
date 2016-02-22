package duson.java.solutionConf.springmvc;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * 依赖：poi
 * 使用：
 * Controller返回值   
 return new ModelAndView(new ViewExcel(), model); 
 *
 */
public class ViewExcel extends AbstractExcelView {
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		PageList<DeviceWorktimeLogsDTO> list = new PageList<DeviceWorktimeLogsDTO>();
		if(model.containsKey("list"))
			list = (PageList<DeviceWorktimeLogsDTO>) model.get("list");
		
		HSSFSheet sheet = workbook.createSheet("Sheet");  
        sheet.setDefaultColumnWidth((short) 12);  
        
        getCell(sheet, 0, 0).setCellValue("列1");  
        getCell(sheet, 0, 1).setCellValue("列2");
        
        //HSSFCellStyle dateStyle = workbook.createCellStyle();  
        //dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-mm-dd"));
        
        HSSFRow sheetRow;
        int rowIndex = 1;
        //sheetRow = sheet.createRow(list.size());
        for () {
        	sheetRow = sheet.createRow(rowIndex++);
        	
            sheetRow.createCell(0).setCellValue("");
        }
        
        String filename = "test.xls";
        filename = encodeFilename(filename, request);
        response.setContentType("application/vnd.ms-excel");   
        response.setHeader("Content-disposition", "attachment;filename=" + filename);   
        OutputStream ouputStream = response.getOutputStream();   
        workbook.write(ouputStream);   
        ouputStream.flush();   
        ouputStream.close();
	}

	/** 
     * 设置下载文件中文件的名称 
     *  
     * @param filename 
     * @param request 
     * @return 
     */  
	public static String encodeFilename(String filename, HttpServletRequest request) {
		String agent = request.getHeader("USER-AGENT");
		try {
			if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
				String newFileName = URLEncoder.encode(filename, "UTF-8");
				newFileName = StringUtils.replace(newFileName, "+", "%20");
				if (newFileName.length() > 150) {
					newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
					newFileName = StringUtils.replace(newFileName, " ", "%20");
				}
				return newFileName;
			}
			if ((agent != null) && (-1 != agent.indexOf("Mozilla")))
				return MimeUtility.encodeText(filename, "UTF-8", "B");

			return filename;
		} catch (Exception ex) {
			return filename;
		}
	}
	
}
