import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

public class TopicHandler extends DefaultHandler{

	//r�sultats de notre parsing
	private List<Topic> themes;
	private List<LinkedList<Topic>> retour;
	private Topic topic;
	private boolean inthemes, inTopic;
	
	//buffer nous permettant de r�cup�rer les donn�es 
	private StringBuffer buffer;

	// simple constructeur
	public TopicHandler(){
		super();
		retour = new ArrayList<LinkedList<Topic>>();
	}
	//détection d'ouverture de balise
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
		if(qName.equals("TOPICS")){
			themes = new LinkedList<Topic>();
			inthemes = true;
			
		}else if(qName.equals("D")){
			topic = new Topic();
			inTopic = true;
			buffer = new StringBuffer();
		}
	}
	//détection fin de balise
	public void endElement(String uri, String localName, String qName)
			throws SAXException{
		if(qName.equals("TOPICS")){
			inthemes = false;
			retour.add((LinkedList<Topic>) themes);
		}else if(qName.equals("D")){
			topic.contenuTheme = buffer.toString();
			themes.add(topic);
			topic = null;
			inTopic = false;
			buffer = null;
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
		//System.out.println("D�but du parsing topic");
	}
	//fin du parsing
	public void endDocument() throws SAXException {
		//System.out.println("Fin du parsing");
		//System.out.println("Resultats du parsing");
		
	}
	
	public List<LinkedList<Topic>> getthemes() {
		return retour;
	}
	public void setthemes(List<Topic> themes) {
		this.themes = themes;
	}
}
