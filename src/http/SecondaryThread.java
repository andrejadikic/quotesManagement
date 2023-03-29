package http;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class SecondaryThread implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public SecondaryThread(Socket client) {
        this.client = client;

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

    @Override
    public void run() {
        try {
            String s = in.readLine();
            System.out.println(s);
            if(!s.equals("GET /quote-of-the-day HTTP/1.1")){
                out.println("Invalid request");
            }
            else{
                Gson gson = new Gson();
                out.println(gson.toJson(SecondaryServis.quoteOfTheDay));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            client.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
