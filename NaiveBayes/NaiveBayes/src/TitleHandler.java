import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;


public class TitleHandler extends DefaultHandler{
	
	
	private String string;
	
	//r�sultats de notre parsing
	private List<String> titles;

	
	private boolean inthemes, inTitle;
	
	//buffer nous permettant de r�cup�rer les donn�es 
	private StringBuffer buffer;
	
	//boolean de présence de titre
	private boolean haveTitle = false;
	private boolean inTitles;
	
	// simple constructeur
	public TitleHandler(){
		super();

	}
	//détection d'ouverture de balise
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
		
		if(qName.equals("xml")){
			titles = new LinkedList<String>();
		}else if(qName.equals("TITLE")){
			inTitles = true;
			buffer = new StringBuffer();
			haveTitle = true;
		}
		else if(qName.equals("TEXT")){
			haveTitle = false;
		}
		
	}
	//détection fin de balise
	public void endElement(String uri, String localName, String qName)
			throws SAXException{
		
		if(qName.equals("REUTERS"))
		{
			//System.out.println(haveTitle);
			if(!haveTitle)
			{
				string = "";
				titles.add(string);
				string = null;
				inTitle = false;
			}
			else {
				haveTitle = false;
				titles.add(buffer.toString());
				inTitle = false;
				buffer = null;
			}
				
		}
		
	}
	//détection de caract�res
	public void characters(char[] ch,int start, int length)
			throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);       
	}
	//début du parsing
	public void startDocument() throws SAXException {
		//System.out.println("D�but du parsing Title");
	}
	//fin du parsing
	public void endDocument() throws SAXException {
		//System.out.println("Fin du parsing");
		//System.out.println("Resultats du parsing");
		
	}
	
	public List<String> getTitles() {
		return titles;
	}
	
}
