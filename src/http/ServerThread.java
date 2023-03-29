package http;


import app.RequestHandler;
import http.response.Response;
import model.Quote;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerThread implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public static List<Quote> quotes = new CopyOnWriteArrayList<>();

    public ServerThread(Socket sock) {
        this.client = sock;

        try {
            //inicijalizacija ulaznog toka
            in = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream()));

            //inicijalizacija izlaznog sistema
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    client.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            // uzimamo samo prvu liniju zahteva, iz koje dobijamo HTTP method i putanju
            String requestLine = in.readLine();

            StringTokenizer stringTokenizer = new StringTokenizer(requestLine);

            String method = stringTokenizer.nextToken();
            String path = stringTokenizer.nextToken();
            int len = 0;
            System.out.println("\nHTTP ZAHTEV KLIJENTA:\n");
            do {
                if(requestLine.startsWith("Content-Length") && method.equals(HttpMethod.POST.toString())){
                    len=Integer.parseInt(requestLine.substring(requestLine.length()-2));
                }
                System.out.println(requestLine);
                requestLine = in.readLine();
            } while (!requestLine.trim().equals(""));

            if (method.equals(HttpMethod.POST.toString())) {
                char[] buffer = new char[len];
                in.read(buffer);
                String s  = new String(buffer);
                System.out.println(s);
                Quote quote = new Quote();
                quote.parseQuote(s);
                quotes.add(quote);
                SecondaryServis.addQuote(quote);
            }
            Request request = new Request(HttpMethod.valueOf(method), path);

            RequestHandler requestHandler = new RequestHandler();
            Response response = requestHandler.handle(request);

            System.out.println("\nHTTP odgovor:\n");
            System.out.println(response.getResponseString());

            out.println(response.getResponseString());

            in.close();
            out.close();
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
