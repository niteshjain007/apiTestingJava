package functional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

import utility.GenericFunctions;

public class WorkFlowTest1 extends GenericFunctions
{
	//reusable variable
	public String url = null;
	public ArrayList<String> row = null;
	String FailureReportFile=null;
	public static List <String> notGoodSearchList=new ArrayList<String>();
	public static List <String> GoodSearchList=new ArrayList<String>();

	@BeforeClass
	public void DataInputAndReportSetup() throws InterruptedException
	{
		TestfileName="src\\test\\resources\\FunctionalTestcase.xls";
		sheetNumber = 0;
		DateFormat dateformat = new SimpleDateFormat("MMMdd_HHmm");
		Date date = new Date();
		String var = dateformat.format(date);
		extReport = new ExtentReports("C:\\reports\\Result_" + var + ".html", true); 				  		
		FailureReportFile= "C:\\reports\\failure"+var+".txt";
	}
	@AfterClass
	public void endReport() throws InterruptedException
	{
		extReport.flush();	
	}
	@AfterMethod
	public void afterEachMethodReport(ITestResult result)
	{
		if(result.getStatus()==ITestResult.FAILURE)
		{
			logger.log(LogStatus.INFO,"Probably ResponseCode Not Matching or response null");
			logger.log(LogStatus.FAIL,logger.getTest().getName());
			
		}
		extReport.endTest(logger);
		
	}
	String fileId=null;
	@Test(priority=1)
	public  void verifyAddress_components() throws IOException, InterruptedException, EncryptedDocumentException, InvalidFormatException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException
	{
		notGoodSearchList.clear();
		logger=extReport.startTest("verifyAddress_components");
		row= getRow("verifyAddress_components");
		url=row.get(3)+row.get(4);
		String responseString=null;
		
		responseString=doHttpsGetandGetResponse(url);
		
		System.out.println("responseString::"+responseString);
		Assert.assertFalse(responseString==null, "Get response coming as null");
		
		String responseCode=responseString.substring(0,3);
		Assert.assertTrue(responseCode.contains("200"), "Response code is not 200");
		
		
		responseString=responseString.substring(responseString.indexOf("{"));
		JsonParser jsonParser = new JsonParser();
		JsonObject purchaseObj= (JsonObject) jsonParser.parse(responseString.toString());
		logger.log(LogStatus.INFO,"Response json is taken");
		String addressArea=null;
		try {

	        JsonParser jsonParser1 = new JsonParser();
	        JsonObject jo = (JsonObject)jsonParser1.parse(responseString);
	        JsonArray jsonArr = jo.getAsJsonArray("results");
	    	for(int i=0;i<jsonArr.size();i++)
			{
	    		addressArea=jsonArr.get(i).getAsJsonObject().get("address_components")
	    				.getAsJsonArray().get(0).getAsJsonObject().get("long_name").toString();
	    		
		
			}
			      
        
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		logger.log(LogStatus.INFO,"detaile are fetched");
		Assert.assertTrue(addressArea.contains("Lonkar Nagar"), "addressArea not coming correctly");
		logger.log(LogStatus.PASS,"verifyAddress_components working fine");		
	}


	
}
