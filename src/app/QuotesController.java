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
        ret= "";
    }

    @Override
    public Response doGet() {
        Socket socket;
        BufferedReader in=null;
        PrintWriter out=null;
        Quote quoteOfTheDay= new Quote();

        try {
            socket = new Socket("127.0.0.1",8081);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.println("GET /quote-of-the-day HTTP/1.1");
            String m =in.readLine();
            Gson gson = new Gson();
            quoteOfTheDay = gson.fromJson(m,Quote.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String htmlBody = "" +
                "<!DOCTYPE html> <html lang=\"en\"> <head> <title>Well Groomed Crisp Rabbit</title> <meta property=\"og:title\" content=\"Well Groomed Crisp Rabbit\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /> <meta charset=\"utf-8\" /> <meta property=\"twitter:card\" content=\"summary_large_image\" />"+
                "<form method=\"POST\"  action=\"/save-quote\" >" +
                "<label>Author: </label><input name=\"autor\" type=\"autor\"><br><br>" +
                "<label>Quote: </label><input name=\"citat\" type=\"citat\"><br><br>"+
                "<button>Save quote</button>" +
                "</form>"+
                "<label>Quote of the day:</label><br><br>"+quoteOfTheDay.toHtml()+
                "<label>Saved quotes:</label><br><br>"
                ;
        htmlBody=htmlBody+ getQuotes();
        String content = "<html><head><title>Odgovor servera</title></head>\n";
        content += "<body>" + htmlBody + "</body></html>";

        return new HtmlResponse(content);
    }

    @Override
    public Response doPost() {
        // TODO: obradi POST zahtev

        return new RedirectResponse();
    }

    private String getQuotes(){
        ret ="";
        ServerThread.quotes.iterator().forEachRemaining((Quote x)->{
            ret=ret+x.toHtml();
        });
        return ret;
    };
}



//"<style data-tag=\"reset-style-sheet\">html {  line-height: 1.15;}body {  margin: 0;}* {  box-sizing: border-box;  border-width: 0;  border-style: solid;}p,li,ul,pre,div,h1,h2,h3,h4,h5,h6,figure,blockquote,figcaption {  margin: 0;  padding: 0;}button {  background-color: transparent;}button,input,optgroup,select,textarea {  font-family: inherit;  font-size: 100%;  line-height: 1.15;  margin: 0;}button,select {  text-transform: none;}button,[type=\"button\"],[type=\"reset\"],[type=\"submit\"] {  -webkit-appearance: button;}button::-moz-focus-inner,[type=\"button"]::-moz-focus-inner,[type="reset"]::-moz-focus-inner,[type="submit"]::-moz-focus-inner {  border-style: none;  padding: 0;}button:-moz-focus,[type="button"]:-moz-focus,[type="reset"]:-moz-focus,[type="submit"]:-moz-focus {  outline: 1px dotted ButtonText;}a {  color: inherit;  text-decoration: inherit;}input {  padding: 2px 4px;}img {  display: block;}html { scroll-behavior: smooth  }</style><style data-tag="default-style-sheet">
//        html {
//        font-family: Inter;
//        font-size: 16px;
//        }
//
//        body {
//        font-weight: 400;
//        font-style:normal;
//        text-decoration: none;
//        text-transform: none;
//        letter-spacing: normal;
//        line-height: 1.15;
//        color: var(--dl-color-gray-black);
//        background-color: var(--dl-color-gray-white);
//
//        }
//</style>
//<link
//      rel="stylesheet"
//              href="https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&amp;display=swap"
//              data-tag="font"
//              />
//<link rel="stylesheet" href="./style.css" />
//</head>
//<body>
//<div>
//<link href="./home.css" rel="stylesheet" />
//
//<div class="home-container">
//<form class="home-form">
//<label class="home-text">Author</label>
//<input type="text" placeholder="Author" class="home-input input" />
//<label class="home-text01">Quote</label>
//<input type="text" placeholder="Quote" class="home-textinput input" />
//<button class="home-button button">
//<span>
//<span>Save quote</span>
//<br />
//</span>
//</button>
//</form>
//<div class="home-container1">
//<h1 class="home-text05">
//<span>Quote of the day:</span>
//<br />
//</h1>
//<label class="home-text08">Label</label>
//<h1 class="home-text09">Saved quotes</h1>
//<label class="home-text10">Label</label>
//<label class="home-text11">Label</label>
//</div>
//</div>
//</div>
//</body>
//</html>"
