package http;

import model.Quote;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SecondaryServis {
    public static final int TCP_PORT = 8081;
    private static List<Quote> quotes = new LinkedList<>();
    public static Quote quoteOfTheDay = new Quote();
    public static void main(String[] args) {
        quotes.add(new Quote("bojan","citat1"));
        quotes.add(new Quote("dario","citat2"));
        quotes.add(new Quote("relja","citat3"));
        quotes.add(new Quote("aleksa","citat4"));
        int i = new Random().nextInt(4);
        quoteOfTheDay = quotes.get(i);

        try {
            ServerSocket ss = new ServerSocket(TCP_PORT);
            while (true) {
                Socket sock = ss.accept();
                new Thread(new SecondaryThread(sock)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addQuote(Quote quote){
        quotes.add(quote);
    }

}
