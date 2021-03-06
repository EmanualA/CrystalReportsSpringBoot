package com.lister.Project.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.businessobjects.samples.CRJavaHelper;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;

/**
 * @author souvik.p
 *
 */
public class GenerateReport {
	private static final String db_user="system";
	private static final String db_pwd="kroger";
	private static final String db_url="jdbc:oracle:thin:@localhost:1521:xe";
	private static final String db_driver="oracle.jdbc.driver.OracleDriver";
	
	/**
	 * @return
	 * @throws ReportSDKException
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public boolean generate() throws ReportSDKException, IOException{
		ReportClientDocument rcd=new ReportClientDocument();
	    rcd.open("D://Report Templates/sample1.rpt", 0);
	    CRJavaHelper crj=new CRJavaHelper();
	    crj.changeDataSource(rcd, db_user, db_pwd, db_url, db_driver, "");
		crj.logonDataSource(rcd, db_user, db_pwd);
	    rcd.checkDatabaseAndUpdate();
	    rcd.refreshReportDocument();
	    ByteArrayInputStream bais=(ByteArrayInputStream)rcd.getPrintOutputController().export(ReportExportFormat.PDF);
	    rcd.close();
	    try{
	    	String currentDate=new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_a'.pdf'").format(new Date());
	    	String fname="Employee"+"_"+currentDate;
	    	String directory="D://GeneratedReports";
	    	String path=directory+"/"+fname;
			File file = new File(path);
		    FileOutputStream fileOutputStream = new FileOutputStream(file);
		    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bais.available());
		    byte[] byteArray=new byte[bais.available()];
			int x = bais.read(byteArray, 0, bais.available());
		    byteArrayOutputStream.write(byteArray, 0, x);
		    byteArrayOutputStream.writeTo(fileOutputStream);
		    fileOutputStream.close();
		    return true;
	    }
	    catch(FileNotFoundException fnfe){
	    	fnfe.printStackTrace();
	    	return false;
	    }
	}
}
