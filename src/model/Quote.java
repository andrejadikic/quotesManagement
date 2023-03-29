package model;

public class Quote {
    private String author;
    private String text;

    public Quote(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public Quote() {
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public void parseQuote(String s){
        s=s.replace("+"," ");
        s=s.replace("%21","!");
        s=s.replace("%3F","?");
        s=s.replace("%2C",".");
        s=s.replace("%3B",";");

        String autor="";
        String tekst="";
        int i=6;
        while(s.charAt(i)!='&'){
            autor=autor+s.charAt(i);
            i++;
        }
        this.author=autor;
        i=i+7;
        while(i<s.length()){
            tekst=tekst+s.charAt(i);
            i++;
        }
        this.text=tekst;
    }
    public String  toHtml(){
        String ret="";
        ret=ret+"<label>"+author+"</label>"+"<label>:"+"\""+text+"\""+"</label>"+"<br><br>";
        return ret;
    }
}
