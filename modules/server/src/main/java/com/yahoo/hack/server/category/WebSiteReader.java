package com.yahoo.hack.server.category;

/**
 * Created by IntelliJ IDEA.
 * User: vikashk
 * Date: 10/11/11
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebSiteReader {
    public WebSiteReader(String url) {
        this.url = url;
    }

    public WebSiteReader() {
    }

    private String url;
    private Document doc = null;

    public Document getDoc() {
        return doc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void read() throws Exception {

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {

            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }
    }

  // will return a key value pair of "keywords"==> keyword , "Description"==>description, if they are not null
    public HashMap<String,String> getMetaKeywords_Description(){
       Elements metaTags= doc.select("meta[name=keywords]");
       Element firstTag=null;
       String keyWordscontent=null;
    //   String firstKeyWord=null;
       String descrptionWords=null;
    //   int findComma=-1;
       if(metaTags!=null){
           firstTag=metaTags.first();
           if(firstTag!=null)
               keyWordscontent=firstTag.attr("Content");

       }
       HashMap<String,String> h=new HashMap<String, String>();
       if(keyWordscontent !=null)
           h.put("keywords",keyWordscontent);
       metaTags=doc.select("meta[name=description]");
       if(metaTags!=null){
           firstTag=metaTags.first();
           if(firstTag!=null)
               descrptionWords= firstTag.attr("content");
           if(descrptionWords!=null)
               h.put("Description",descrptionWords);
       }
       return h;
    }


    public String getTitle(){
        Elements titles =doc.select("title");
        if(titles!=null){
            Element title=titles.get(0);
            return title.text();
        }
        return "";
    }


    // returns collection of Element which can be converted to text by .text() method
    public String[] getParagraphText() {
        Elements e = doc.getElementsByTag("p");
        int len = e.size();
        String[] paragraphs = new String[len];
        int i = 0;
        for (Element ee : e) {

            paragraphs[i++] = ee.text();

        }
        return paragraphs;
    }

    // returns collection of Element which can be converted to text by .text() method
    public List<String> getLinks() {
        Elements links = doc.select("a[href]");
        int len = links.size();
        //String[] linkArray=new String[len] ;
        List<String> link = new ArrayList<String>();

        for (Element ee : links) {
            String linkText = ee.text();
            if (linkText.contains("http"))
                link.add(linkText);

        }
        return link;
    }


    public HashMap<String, List<String>> getMedia() {
        Elements media = doc.select("[src]");
        HashMap<String, List<String>> linkToMedia = new HashMap<String, List<String>>();
        List<String> imageLinks = new ArrayList<String>();
        List<String> otherMediaLinks = new ArrayList<String>();

        for (Element src : media) {
            if (src.tagName().equals("img")) {
                imageLinks.add(src.attr("abs:src"));
            }
            /*    print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            */
            else {
                otherMediaLinks.add(src.tagName() + "=>" + src.attr("abs:src"));
            }

        }
        linkToMedia.put("image", imageLinks);
        linkToMedia.put("otherMedia", otherMediaLinks);
        return linkToMedia;
    }
}
/*  public static BufferedReader read(String url) throws Exception{
     return new BufferedReader(
         new InputStreamReader(
             new URL(url).openStream()));
 }
*/

/*
	public static void main (String[] args) throws Exception{
	//	BufferedReader reader = read("http://en.wikipedia.org/wiki/Steve_Jobs");
        String url= "http://en.wikipedia.org/wiki/Steve_Jobs";
        url=  "http://www.java-samples.com/showtutorial.php?tutorialid=226";
        url="http://economictimes.indiatimes.com/news/news-by-industry/services/education/22000-engineering-seats-find-no-takers-in-karnataka/articleshow/10230431.cms";
        WebSiteReader crawler=new WebSiteReader(url);
        crawler.read();
       //  String []para=crawler.getParagraphText();
       // List<String> links=crawler.getLinks();
         HashMap<String,String> h= crawler.getMetaKeywords_Description();
        System.out.println("keywords" +" ==>"+ h.get("keywords"));
        System.out.println("Description" +" ==>"+ h.get("Description"));

        String title= crawler.getTitle();
        System.out.println(title);

        for( String s : para ) {
            System.out.println(s);
        }


       for(String link : links){
            System.out.println(link);
        }

    }
*/
/*
Example for Extracting meta tag keywords:

<meta name="description" content="Open source Java HTML parser, with DOM,
CSS, and jquery-like methods for easy data extraction." />

Document doc = Jsoup.connect("http://jsoup.org/").get();
String desc = doc.select("meta[name=description]").first().attr("content");
// desc is "Open source Java HTML parser, with DOM, CSS, and jquery-like
methods for easy data extraction."

To break it down:
- doc.select("meta[name=description]") gets an array of elements that are
<meta> tags and have an attribute "name=description".
- .first() pulls the first result from the array (returns an Element)
- .attr("content") pulls the text value of the "content" attribute

*/



