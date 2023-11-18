package main.java.client;

public interface Client {
    int getId();
    String getName();
    int desiredTicketsCount();
    int getPriority();
    ClientState getState();

}
