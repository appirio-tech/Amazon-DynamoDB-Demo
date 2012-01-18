package com.cloudspokes.dynamodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodb.model.ComparisonOperator;
import com.amazonaws.services.dynamodb.model.Condition;
import com.amazonaws.services.dynamodb.model.DeleteItemRequest;
import com.amazonaws.services.dynamodb.model.DeleteItemResult;
import com.amazonaws.services.dynamodb.model.GetItemRequest;
import com.amazonaws.services.dynamodb.model.GetItemResult;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.PutItemRequest;
import com.amazonaws.services.dynamodb.model.PutItemResult;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.amazonaws.services.dynamodb.model.UpdateItemRequest;
import com.amazonaws.services.dynamodb.model.UpdateItemResult;
import com.cloudspokes.dynamodb.domain.Loan;

@Controller
public class HomeController {

	static AmazonDynamoDBClient dynamoDB;
	private String tableName = "kiva-loans";
	// aws keys
	public static final String ACCESSKEY = "YOURKEY";
	public static final String SECRETKEY = "YOURSECRET";

	public HomeController() {
		try {
			setup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Displays the list of loans from the table
	 */
	@RequestMapping(value = "/loans", method = RequestMethod.GET)
	public String loans(Locale locale,
			@RequestParam(value = "keyword", required = false) String keyword,
			Model model) {

		ArrayList<Loan> loans = new ArrayList<Loan>();
		ScanRequest scanRequest = new ScanRequest(tableName);

		if (keyword != null) {
			HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
			Condition condition = new Condition().withComparisonOperator(
					ComparisonOperator.EQ.toString()).withAttributeValueList(
					new AttributeValue().withS(keyword));
			scanFilter.put("country", condition);
			scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
		}
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		for (int i = 0; i < scanResult.getCount(); i++) {
			HashMap<String, AttributeValue> item = (HashMap<String, AttributeValue>) scanResult
					.getItems().get(i);
			Loan loan = new Loan();
			loan.setActivity(item.get("activity").getS());
			loan.setCountry(item.get("country").getS());
			loan.setFunded_amount(Double.parseDouble(item.get("funded_amount")
					.getN()));
			loan.setId(Integer.parseInt(item.get("id").getN()));
			loan.setName(item.get("name").getS());
			loan.setStatus(item.get("status").getS());
			loan.setUse(item.get("use").getS());
			loans.add(loan);
		}

		model.addAttribute("loans", loans);
		return "loans";
	}

	/**
	 * Displays a loan item
	 */
	@RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
	public String show(@PathVariable String id, Locale locale, Model model) {
		model.addAttribute("loan", getLoan(id));
		return "show";
	}

	/**
	 * Displays a form to create a new loan item
	 */
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newLoan(Locale locale, Model model) {
		model.addAttribute("loan", new Loan());
		return "new";
	}

	/**
	 * Inserts a new loan item into dynamodb
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String addLoan(@ModelAttribute("loan") Loan loan,
			BindingResult result) {

		// populate an item with the data to put
		HashMap<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("id", new AttributeValue().withN(String.valueOf(loan.getId())));
		item.put("activity", new AttributeValue().withS(loan.getActivity()));
		item.put("country", new AttributeValue().withS(loan.getCountry()));
		item.put("funded_amount", new AttributeValue().withN(String
				.valueOf(loan.getFunded_amount())));
		item.put("name", new AttributeValue().withS(loan.getName()));
		item.put("status", new AttributeValue().withS(loan.getStatus()));
		item.put("use", new AttributeValue().withS(loan.getUse()));

		// put the item to the table
		try {
			PutItemRequest req = new PutItemRequest(tableName, item);
			PutItemResult res = dynamoDB.putItem(req);
		} catch (AmazonServiceException ase) {
			System.err.println("Failed to create item in " + tableName);
		}

		return "redirect:show/" + loan.getId();
	}

	/**
	 * Displays the item for editing
	 */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editLoan(@PathVariable String id, Locale locale, Model model) {
		model.addAttribute("loan", getLoan(id));
		return "edit";
	}

	/**
	 * Submits the updates loan data to dynamodb
	 */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String updateLoan(@PathVariable String id,
			@ModelAttribute("loan") Loan loan, BindingResult result) {

		Key key = new Key().withHashKeyElement(new AttributeValue().withN(id));
		HashMap<String, AttributeValueUpdate> updates = new HashMap<String, AttributeValueUpdate>();

		AttributeValueUpdate update = new AttributeValueUpdate().withValue(
				new AttributeValue(loan.getStatus())).withAction("PUT");
		updates.put("status", update);

		// update the item to the table
		try {
			UpdateItemRequest req = new UpdateItemRequest(tableName, key,
					updates);
			UpdateItemResult res = dynamoDB.updateItem(req);
		} catch (AmazonServiceException ase) {
			System.err.println("Failed to update item: " + ase.getMessage());
		}

		return "redirect:../show/" + id;
	}

	/**
	 * Fetches loan data from Kiva an inserts it into dynamodb
	 */
	@RequestMapping(value = "/loadData", method = RequestMethod.GET)
	public String loadData(Locale locale, Model model) {

		// delete all of the current loans
		deleteAllLoans();

		// make the REST call to kiva
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(
				"http://api.kivaws.org/v1/loans/newest.json");
		getRequest.addHeader("accept", "application/json");
		HttpResponse response;
		String payload = "";

		try {
			response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));
			payload = br.readLine();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject json = (JSONObject) JSONSerializer.toJSON(payload);
		// get the array of loans
		JSONArray loans = json.getJSONArray("loans");

		for (int i = 0; i < loans.size(); ++i) {
			JSONObject loan = loans.getJSONObject(i);
			// populate an item with the data to put
			HashMap<String, AttributeValue> item = new HashMap<String, AttributeValue>();
			item.put("id", new AttributeValue().withN(loan.getString("id")));
			item.put("name", new AttributeValue().withS(loan.getString("name")));
			item.put("status",
					new AttributeValue().withS(loan.getString("status")));
			item.put("funded_amount",
					new AttributeValue().withN(loan.getString("funded_amount")));
			item.put("activity",
					new AttributeValue().withS(loan.getString("activity")));
			item.put("use", new AttributeValue().withS(loan.getString("use")));
			item.put("country", new AttributeValue().withS(loan.getJSONObject(
					"location").getString("country")));

			try {
				PutItemRequest req = new PutItemRequest(tableName, item);
				PutItemResult res = dynamoDB.putItem(req);
				System.out.println("Put result: " + res);
			} catch (AmazonServiceException ase) {
				System.err.println("Failed to create item in " + tableName);
			}

		}

		httpClient.getConnectionManager().shutdown();

		return "redirect:loans";
	}

	/**
	 * Displays the home page
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}

	/**
	 * Fetches a specific loan item
	 */
	private Loan getLoan(String id) {
		Key key = new Key().withHashKeyElement(new AttributeValue().withN(id));
		GetItemRequest req = new GetItemRequest(tableName, key);
		GetItemResult res = dynamoDB.getItem(req);
		HashMap<String, AttributeValue> item = (HashMap<String, AttributeValue>) res
				.getItem();

		Loan loan = new Loan();
		loan.setActivity(item.get("activity").getS());
		loan.setCountry(item.get("country").getS());
		loan.setFunded_amount(Double.parseDouble(item.get("funded_amount")
				.getN()));
		loan.setId(Integer.parseInt(item.get("id").getN()));
		loan.setName(item.get("name").getS());
		loan.setStatus(item.get("status").getS());
		loan.setUse(item.get("use").getS());
		return loan;
	}

	/**
	 * Deletes all items from dynamodb
	 */
	private void deleteAllLoans() {
		ScanRequest scanRequest = new ScanRequest(tableName);
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		for (int i = 0; i < scanResult.getItems().size(); i++) {
			HashMap<String, AttributeValue> item = (HashMap<String, AttributeValue>) scanResult
					.getItems().get(i);
			try {
				Key key = new Key()
						.withHashKeyElement(new AttributeValue("id"))
						.withHashKeyElement(item.get("id"));
				DeleteItemRequest request = new DeleteItemRequest(tableName,
						key);
				DeleteItemResult result = dynamoDB.deleteItem(request);
				System.out.println("Result: " + result);
			} catch (AmazonServiceException ase) {
				System.err.println("Failed to delete item in " + tableName);
			}
		}
	}

	private void setup() throws Exception {
		BasicAWSCredentials creds = new BasicAWSCredentials(ACCESSKEY,
				SECRETKEY);
		dynamoDB = new AmazonDynamoDBClient(creds);
		dynamoDB.setEndpoint("http://dynamodb.us-east-1.amazonaws.com");
	}

}
