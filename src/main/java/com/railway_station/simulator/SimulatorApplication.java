package com.railway_station.simulator;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SimulatorApplication {
	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setHostname("localhost");
		config.setPort(9092);

		final SocketIOServer server = new SocketIOServer(config);
		return server;
	}

	public static void main(String[] args) {
		SpringApplication.run(SimulatorApplication.class, args);
	}

}
