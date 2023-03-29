package app;

import model.Quote;
import com.google.gson.Gson;
import http.Request;
import http.ServerThread;
import http.response.HtmlResponse;
import http.response.RedirectResponse;
import http.response.Response;

import java.io.*;
import java.net.Socket;

public class QuotesController extends Controller {

    private String ret;
    public QuotesController(Request request) {
        super(request);
        ret= new String();
    }

    @Override
    public Response doGet() {
        Socket socket=null;
        BufferedReader in=null;
        PrintWriter out=null;
        Quote citatDana= new Quote();

        try {
            socket = new Socket("127.0.0.1",8081);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.println("GET /citat-dana HTTP/1.1");
            String m =in.readLine();
            Gson gson = new Gson();
            citatDana = gson.fromJson(m,Quote.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String htmlBody = "" +
                "<form method=\"POST\"  action=\"/save-quote\" >" +
                "<label>Autor: </label><input name=\"autor\" type=\"autor\"><br><br>" +
                "<label>Citat: </label><input name=\"citat\" type=\"citat\"><br><br>"+
                "<button>Submit</button>" +
                "</form>"+
                "<label>Citat dana:</label><br><br>"+citatDana.toHtml()+
                "<label>Citati:</label><br><br>"
                ;
        htmlBody=htmlBody+getCitati();
        String content = "<html><head><title>Odgovor servera</title></head>\n";
        content += "<body>" + htmlBody + "</body></html>";

        return new HtmlResponse(content);
    }

    @Override
    public Response doPost() {
        // TODO: obradi POST zahtev
        /*ServerThread.citati.iterator().forEachRemaining((Citat x) ->{
        });*/

        return new RedirectResponse();
    }

    private String getCitati(){
        ret ="";
        ServerThread.quotes.iterator().forEachRemaining((Quote x)->{
            ret=ret+x.toHtml();
        });
        return ret;
    };
}
