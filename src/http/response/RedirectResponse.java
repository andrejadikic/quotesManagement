package http.response;

public class RedirectResponse extends  Response{
    @Override
    public String getResponseString() {
        String response = "HTTP/1.1 301 OK\r\nLocation: /quotes\r\n\r\n";
        return response;
    }
}
