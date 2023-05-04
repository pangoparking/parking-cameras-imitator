package org.parking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.parking.model.CarData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class ParkingCamerasImitatorApplicationTests {

	@Autowired
	OutputDestination consumer;
	
	@Test
	void imitator() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		for (int i = 0; i < 100; i++) {
			Message<byte[]> msg = consumer.receive(1100, "carData");
			assertNotNull(msg);
			System.out.println(mapper.readValue(msg.getPayload(), CarData.class));
		}
	}

}
