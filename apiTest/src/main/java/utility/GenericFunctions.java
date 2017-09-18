package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class GenericFunctions 
{
	// extent report logger code
	public static ExtentReports extReport;
	public static ExtentTest logger;
	//extent report end
	
	public String TestfileName="src\\test\\resources\\testcase.xls";
	public int sheetNumber;
	public HSSFWorkbook filename;
	// below function is reusable to get data from excel sheet using apache poi

	////below list is for saving report /info and 
	
	//report list end
	
	public ArrayList<String> getRow(String cellContent)
			throws FileNotFoundException, IOException, EncryptedDocumentException, InvalidFormatException {
		FileInputStream file = new FileInputStream(new File(TestfileName));
		POIFSFileSystem fs = new POIFSFileSystem(file);
		filename = new HSSFWorkbook(fs);
		HSSFSheet sheet = filename.getSheetAt(sheetNumber);
		InputStream fileIn = new FileInputStream(TestfileName);
		Workbook wb = WorkbookFactory.create(fileIn);
		int flag = 0;
		ArrayList<String> cells = new ArrayList<String>();
		try {
			for (Row row : sheet) {
				for (Cell cell : row) {
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						// if
						// (cell.getRichStringCellValue().getString().trim().equals(cellContent))
						// {
						if (cell.getRichStringCellValue().getString().trim().contains(cellContent)) {
							flag = 1;
							cell = row.getCell(row.getRowNum());
							flag = 1;
							Iterator<Cell> cellItr = row.iterator();
							while (cellItr.hasNext()) {
								cells.add(cellItr.next().toString());
							}
							return cells;
						}
					}

					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						double temp = cell.getNumericCellValue();
						String temps = temp + "";
						if (temps.contains(cellContent) == true) {
							flag = 1;
							cell = row.getCell(row.getRowNum());
							flag = 1;
							Iterator<Cell> cellItr = row.iterator();
							while (cellItr.hasNext()) {
								cells.add(cellItr.next().toString());
							}
							return cells;
						}
					}

					wb.close();
				}
			}
			if (flag == 0) {
				System.out.println("No match found!");
			}
		} catch (Exception E) {
			System.out.println(E);
		}
		return null;
	}


	
		
		public String doGetAndGetResponse(String URI, CloseableHttpClient httpclient) throws ClientProtocolException, IOException {
			String responseString;
			try {

			        HttpGet httpget = new HttpGet(URI);

			       // System.out.println("executing request" + httpget.getRequestLine());

			        CloseableHttpResponse response = httpclient.execute(httpget);
			        try {
			        	
			            HttpEntity entity = response.getEntity();
			            responseString = EntityUtils.toString(entity, "UTF-8");
			           // System.out.println("responseString"+responseString);
			            
			           // System.out.println("----------------------------------------");
			      //      System.out.println(response.getStatusLine());
			            if (entity != null) {
			    //            System.out.println("Response content length: " + entity.getContentLength());
//			                System.out.println(entity.toString());
			  //              System.out.println(entity.getContent().toString());
			            }
			            EntityUtils.consume(entity);
			        } finally {
			            response.close();
			        }
			    } finally {
			        httpclient.close();
			    }

		return responseString;
	}
		
		


	public String doHttpsGetandGetResponse(String URI) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException, EncryptedDocumentException, InvalidFormatException, InterruptedException
	{

		int responseCode;
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
	 String responseString =null;
	 try {

	     HttpGet httpget = new HttpGet(URI);
	     httpget.setHeader("Authorization", "Basic cG9zdG1hbjpwYXNzd29yZA==");

	 	long startTime = System.currentTimeMillis();
		System.out.println("start: "+Thread.currentThread().toString()+startTime);
		 
	     //System.out.println("executing request" + httpget.getRequestLine());
	     CloseableHttpResponse response = httpClient.execute(httpget);
	     
	     long elapsedTime = System.currentTimeMillis() - startTime;
	     System.out.println("end: "+Thread.currentThread().toString()+elapsedTime);
		
	     try
	     {
	         HttpEntity entity = response.getEntity();
	         responseString = EntityUtils.toString(entity, "UTF-8");
	         responseCode=response.getStatusLine().getStatusCode();
	         EntityUtils.consume(entity);
	     } finally {
	         response.close();
	     }
	 } finally {
	     httpClient.close();
	 }
	 return responseCode+responseString;
		
	}
	
}
