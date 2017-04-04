package com.baidu.beidou.api.external.accountfile.output;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.Ostermiller.util.ExcelCSVPrinter;
import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;


/**
 * 
 * ClassName: CSVWriter  <br>
 * Function: CSV文件写入
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 5, 2012
 */
public class CSVWriter {
	
	private static final Log log = LogFactory.getLog(CSVWriter.class);
	
	private static final String encoding = AccountFileWebConstants.ACCOUNTFILE_CSV_ENCODING;
	
	private CSVWriter(){
		super();
	}
	
	private static final CSVWriter writer = new CSVWriter();
	
	public static CSVWriter getInstance(){
		return writer;
	}
	
	
	public void write(List<String[]> details, String[] headers, String file)
			throws IOException {	
		ExcelCSVPrinter printer = null;
		BufferedWriter writer = null;
		try{			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),encoding));
			printer = new ExcelCSVPrinter(writer);
			printer.changeDelimiter(AccountFileWebConstants.ACCOUNTFILE_CSV_SEPERATOR); // 设置csv文件的分隔符
			//输出表头
			printer.writeln(headers);
			
			//输出详情
			for(String[] detail: details){
				printer.writeln(detail);
			}	

		}catch(IOException e){
			log.error(e.getMessage(),e);
			throw e;
		}finally{
			try{
				if(printer != null){
					printer.close();
				}				
			}catch(Exception e){
				log.error(e.getMessage(),e);
			}
		}
	}

	
}

