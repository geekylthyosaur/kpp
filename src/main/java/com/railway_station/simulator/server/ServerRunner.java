package com.railway_station.simulator.server;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.railway_station.simulator.client.Client;
import com.railway_station.simulator.client.CommonClient;
import com.railway_station.simulator.client.decorator.Disabled;
import com.railway_station.simulator.client.decorator.Military;
import com.railway_station.simulator.client.decorator.WithChild;
import com.railway_station.simulator.client.generator.ClientGenerator;
import com.railway_station.simulator.config.Configuration;
import com.railway_station.simulator.server.payload_models.ClientReachedCashRegisterEventObject;
import com.railway_station.simulator.server.payload_models.ConfigPayload;
import com.railway_station.simulator.server.payload_models.StopGeneration;
import com.railway_station.simulator.station_building.CashRegister;
import com.railway_station.simulator.station_building.StationBuilding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServerRunner implements CommandLineRunner {

    private static ServerRunner instance;

    private final SocketIOServer server;



    @Autowired
    public ServerRunner(SocketIOServer server) {
        this.server = server;
        ServerRunner.instance = this;
        this.server.addConnectListener(onConnected());
        this.server.addDisconnectListener(onDisconnected());
        this.server.addEventListener("configuration", ConfigPayload.class, onConfiguration());
        this.server.addEventListener("client_reached_cash_register", ClientReachedCashRegisterEventObject.class, onClientReachedCashRegister());
        this.server.addEventListener("stop_generation", StopGeneration.class, onStopGeneration());
    }

    private ConnectListener onConnected() {
        return client -> {
            System.out.println("Connected");
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            System.out.println("Disconnected");
        };
    }

    @Override
    public void run(String... args) throws Exception {
        server.start();
        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }

    private DataListener<ConfigPayload> onConfiguration() {
        return (client, configPayload, ackSender) -> {
            Configuration config = Configuration.getInstance();
            StationBuilding stationBuilding = StationBuilding.getInstance();
            Thread reservecCashRegisterThread = stationBuilding.getReservedCashRegisterThread();
            reservecCashRegisterThread.interrupt();
            List<CashRegister> cashRegisters = stationBuilding.getCashRegisters();
            for (var cashRegister: cashRegisters) {
                cashRegister.close();
            }
            Thread clientGenerationThread = config.getClientGenerationThread();
            clientGenerationThread.interrupt();

            int cashRegisterCount = configPayload.cashRegisterCount;
            int minServingTime = configPayload.minServingTime;
            int maxServingTime = configPayload.maxServingTime;
            String generationStrategy = configPayload.generationStrategy;

            config.setEntryCount(cashRegisterCount);
            config.setServiceTimeMin(minServingTime);
            config.setServiceTimeMax(maxServingTime);
            config.setClientGenerationStrategy(generationStrategy);
            config.setMaxClientsInside(200);

            stationBuilding.createCashRegisters(cashRegisterCount);
            ClientGenerator clientGenerator = new ClientGenerator(config.getClientGenerationStrategy());
            Thread newCientGenerationThread = new Thread(clientGenerator::generate);
            config.setClientGenerationThread(newCientGenerationThread);
            newCientGenerationThread.start();
        };
    }

    private DataListener<ClientReachedCashRegisterEventObject> onClientReachedCashRegister() {
        return (client, eventPayload, ackSender) -> {
            int cashRegisterId = eventPayload.cashRegisterId;
            int clientId = eventPayload.clientId;
            String clientName = eventPayload.clientName;
            int desiredTicketsCount = eventPayload.desiredTicketsCount;
            String clientType = eventPayload.clientType;

            Client stationClient = new CommonClient(clientId, clientName, desiredTicketsCount);
            if (clientType == "disabled") {
                stationClient = new Disabled(stationClient);
            }
            if (clientType == "military") {
                stationClient = new Military(stationClient);
            }
            if (clientType == "with_child") {
                stationClient = new WithChild(stationClient);
            }
            CashRegister cashRegister = StationBuilding.getInstance().getCashRegister(cashRegisterId);
            cashRegister.addClient(stationClient);
        };
    }

    private DataListener<StopGeneration> onStopGeneration() {
        return (client, eventPayload, ackSender) -> {
            Configuration config = Configuration.getInstance();
            StationBuilding stationBuilding = StationBuilding.getInstance();
            Thread reservecCashRegisterThread = stationBuilding.getReservedCashRegisterThread();
            reservecCashRegisterThread.interrupt();
            List<CashRegister> cashRegisters = stationBuilding.getCashRegisters();
            for (var cashRegister: cashRegisters) {
                cashRegister.close();
            }
            Thread clientGenerationThread = config.getClientGenerationThread();
            clientGenerationThread.interrupt();
        };
    }

    public void socketEmit(String eventName, String jsonifiedData) {
        server.getBroadcastOperations().sendEvent(eventName, jsonifiedData);
    }

    public static ServerRunner getInstance() {
        return instance;
    }

}

