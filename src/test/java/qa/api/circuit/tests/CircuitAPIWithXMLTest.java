package qa.api.circuit.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.utils.XmlPathUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CircuitAPIWithXMLTest extends BaseTest{
	
	@Test
	public void getCircuitInfoTest() {
		Response response =
				restClient.get(BASE_URL_CIRCUIT, CIRCUIT_ENDPOINT, null, null, AuthType.NO_AUTH, ContentType.ANY);
		
		List<String> circuitNames=XmlPathUtil.readList(response, "MRData.CircuitTable.Circuit.CircuitName");
		System.out.println(circuitNames);
		
		for(String e:circuitNames) {
			Assert.assertNotNull(e);
		}
		
		String americaLoc= XmlPathUtil.read(response, "**.find{it.circuitId == 'americas'}.Location.Locality");
		Assert.assertEquals(americaLoc, "Austin");
	}

}
