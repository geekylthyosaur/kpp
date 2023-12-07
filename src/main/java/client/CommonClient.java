package main.java.client;

public class CommonClient implements Client {
    int id;
    String name;
    ClientState state;
    int desiredTicketsCount;
    int priority;

    public CommonClient(int id, String name, int desiredTicketsCount, int priority) {
        this.id = id;
        this.name = name;
        this.desiredTicketsCount = desiredTicketsCount;
        this.priority = priority;
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
    public int desiredTicketsCount() {
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
