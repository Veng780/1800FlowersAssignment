package eng.victor.flowers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import eng.victor.flowers.api.FlowerComment;
import eng.victor.flowers.api.PatchOperation;


@SpringBootApplication
public class Application {
	private static Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
		
		
		setup1800FlowersAssignmentRun();
		
		loadFlowerFeed();
		countUpTotalUserIds();
		FlowerComment flowerComment= modify4thJson();
		log.info("the modified element now looks like {}", flowerComment);
		
		log.info("\n\n\n -------  all done, but leaving this service up for fun");
	}
	
	
	private static RestTemplate restTemplate;
	private static String baseUrl;
	
	private static void setup1800FlowersAssignmentRun() throws Exception{
		log.info("\n1800 flowers assignment...\n\n");
		Thread.sleep(3000);
		
		//establish baseUrl
		baseUrl = "http://localhost:8080";
		ResourceBundle rb = ResourceBundle.getBundle("application");
		if (rb.containsKey("server.port")) {
			String port = rb.getString("server.port");
			log.info("running explicitly set to port :"+port);
			baseUrl = "http://localhost:"+port.trim();
		} 
		log.info("calls will be made to {}", baseUrl);
		
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		restTemplate = new RestTemplate(requestFactory);
		log.info("created restTemplate");
	}
	
	
	
	private static void loadFlowerFeed() throws IOException {
		log.info("\n\n\nload the feed. I don't know what type of object these are, I refer to it as FlowerComment");
		
		String feed = null;
		boolean readFeedOk = false;
		try {
			String feedUrl ="http://jsonplaceholder.typicode.com/posts";
			log.debug("read the remote url feed ...{}", feedUrl);
			HttpHeaders fHdrs = new HttpHeaders();
			fHdrs.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
			
			ParameterizedTypeReference<List<FlowerComment>> typeRef  
				= new ParameterizedTypeReference<List<FlowerComment>>() {};			
			ResponseEntity<List<FlowerComment>> respFeed = restTemplate.exchange(feedUrl, HttpMethod.GET, null, typeRef, fHdrs);
			readFeedOk = respFeed.getStatusCode().is2xxSuccessful();
			log.debug("reading feed response {}", respFeed.getStatusCode());
			ObjectMapper mapper = new ObjectMapper();
			feed = mapper.writeValueAsString(respFeed.getBody());
			
		} catch (Exception e) {
			log.error("for some reason I couldn't read your feed!!! ", e);			
			readFeedOk = false;
		}
		if (! readFeedOk) {
			log.error("I'm using local file copy as the feed because something went wrong, just letting you know");
			InputStream in = Application.class.getClassLoader().getResourceAsStream("user_flowercomments_feed.json");
			ByteArrayOutputStream baos =new ByteArrayOutputStream();
			IOUtils.copy(in, baos);
			feed =  baos.toString();
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);		
		String url = baseUrl+"/flowerComments";
		
		ResponseEntity<String> respE = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<String>(feed, headers), String.class);
		log.debug("call {} {}", url, respE.getStatusCode());
	}
	
	
	private static void countUpTotalUserIds() {
		log.info("\n\n\ncount up total unique userIds...");				
		String url = baseUrl+"/users/count";
		
		ResponseEntity<Long> respE = restTemplate.getForEntity(url, Long.class);
		log.debug("call {} {}", url, respE.getStatusCode());
		log.info("userId count {}", respE.getBody());
	}
	
	
	private static FlowerComment modify4thJson() {
		log.info("\n\n\nmodify 4th json...");		
		String url = baseUrl+"/flowerComments/4";
		
		HttpHeaders headers = new HttpHeaders();		
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));		
		ResponseEntity<FlowerComment> respE = restTemplate.getForEntity(url, FlowerComment.class, headers);
		log.info("the object starts out like this:{}", respE.getBody());
		
		Collection<PatchOperation> ops = Arrays.asList(
				PatchOperation.AddOp("title", "1800Flowers"),
				PatchOperation.AddOp("body", "1800Flowers")
		);
		HttpHeaders headers2 = new HttpHeaders();		
		headers2.setContentType(new MediaType("application", "json-patch+json"));
		HttpEntity<Collection<PatchOperation>> patchesEntity = new HttpEntity<Collection<PatchOperation>>(ops, headers2);
		ResponseEntity<FlowerComment> respE2 = restTemplate.exchange(url, HttpMethod.PATCH, patchesEntity, FlowerComment.class);
		log.debug("call {} {}", url, respE2.getStatusCode());
		return respE2.getBody();		 
	}
}
