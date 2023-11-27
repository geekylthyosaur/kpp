package com.railway_station.simulator.client;

public class CommonClient implements Client {
    int id;
    String name;
    ClientState state;
    int desiredTicketsCount;
    int priority = 1;
    public CommonClient(int id, String name, int desiredTicketsCount) {
        this.id = id;
        this.name = name;
        this.desiredTicketsCount = desiredTicketsCount;
        this.state = ClientState.Spawned;
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDesiredTicketsCount() {
        return desiredTicketsCount;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public ClientState getState() {
        return state;
    }
}
