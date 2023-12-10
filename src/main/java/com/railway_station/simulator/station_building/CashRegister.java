package com.railway_station.simulator.station_building;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.railway_station.simulator.client.Client;
import com.railway_station.simulator.config.Configuration;
import com.railway_station.simulator.server.ServerRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;


public class CashRegister {
    private int id;
    private ConcurrentSkipListMap<Integer, Client> clients = new ConcurrentSkipListMap<>((a, b) -> b.compareTo(a)); // Descending order
    private Thread runningThread;


    public CashRegister(int id) {
        this.id = id;
    }

    private synchronized boolean thereAreClients() {
        return !clients.isEmpty();
    }

    public synchronized int getId() {
        return id;
    }

    public void open(){
        runningThread = new Thread(this::serve);
        runningThread.start();
    }
    public void close(){
        if (runningThread != null) {
            runningThread.interrupt();
        }
    }
    public void serve () {
        while (true) {
            if (!thereAreClients()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(Configuration.getInstance().getMinClientServingTime());
                } catch (InterruptedException e) {
                    return;
                }
                continue;
            }

            Map.Entry<Integer, Client> entry = getClientToServe();
            Client client = entry.getValue();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();;
            CurrentTime startTime = new CurrentTime();

            Configuration config = Configuration.getInstance();
            long servingTime = (config.getMaxClientServingTime() + config.getMinClientServingTime()) / 2;
            int desiredTicketsCount = client.getDesiredTicketsCount();
            try {
                TimeUnit.MILLISECONDS.sleep(servingTime * desiredTicketsCount);
            } catch (InterruptedException e) {
                return;
            }
            CurrentTime endTime = new CurrentTime();

            Map<String, Object> clientData = new HashMap<>();
            clientData.put("clientId", client.getId());
            clientData.put("cashRegisterId", getId());
            clientData.put("startTime", startTime);
            clientData.put("endTime", endTime);

            String json = gson.toJson(clientData);
            ServerRunner server = ServerRunner.getInstance();
            server.socketEmit("client_served", json);
            StationBuilding.getInstance().decreaseClientsInsideCount();
        }
    }

    private synchronized Map.Entry<Integer, Client> getClientToServe() {
        return clients.pollFirstEntry();
    }

    public synchronized void addClient(Client client) {
        Integer lastKey = client.getPriority();
        while (clients.containsKey(lastKey)) {
            lastKey--;
        }
        clients.put(lastKey, client);
    }

    public synchronized void setClients(ConcurrentSkipListMap<Integer, Client> clients) {
        this.clients = clients;
    }

    public synchronized ConcurrentSkipListMap<Integer, Client> getClients() {
        return this.clients;
    }
}

class CurrentTime {
    private LocalDateTime currentTime;

    public CurrentTime() {
        this.currentTime = LocalDateTime.now();
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }
}

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(formatter.format(value));
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String stringValue = in.nextString();
        return LocalDateTime.parse(stringValue, formatter);
    }
}
