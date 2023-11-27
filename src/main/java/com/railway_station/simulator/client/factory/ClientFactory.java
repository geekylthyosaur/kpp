package com.railway_station.simulator.client.factory;

import com.railway_station.simulator.client.Client;

import java.util.Random;

public abstract class ClientFactory {
    private static final String[] firstNames = {"Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Henry", "Ivy", "Jack", "Katherine", "Leo", "Mia", "Nathan", "Olivia"};
    private static final String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris"};
    private static final int minTicketsCount = 1;
    private static final int maxTicketsCount = 3;
    public abstract Client createClient(int id);

    public static String generateRandomName() {
        Random random = new Random();
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];
        return firstName + " " + lastName;
    }

    public static int generateDesiredTicketsCount() {
        Random random = new Random();
        return random.nextInt(maxTicketsCount - minTicketsCount + 1) + minTicketsCount;
    }
}
