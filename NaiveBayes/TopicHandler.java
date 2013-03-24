import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class TopicHandler extends DefaultHandler{

	//r�sultats de notre parsing
	private List<Topic> themes;

	private Topic topic;
	private boolean inthemes, inTopic;
	
	//buffer nous permettant de r�cup�rer les donn�es 
	private StringBuffer buffer;

	// simple constructeur
	public TopicHandler(){
		super();
	}
	//détection d'ouverture de balise
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
		if(qName.equals("themes")){
			themes = new LinkedList<Topic>();
			inthemes = true;
		}else if(qName.equals("BODY")){
			topic = new Topic();
			inTopic = true;
		}/*else {
			buffer = new StringBuffer();
			if(qName.equals("nom")){
				inNom = true;
			}else if(qName.equals("prenom")){
				inPrenom = true;
			}else if(qName.equals("adresse")){
				inAdresse = true;
			}else{
				//erreur, on peut lever une exception
				throw new SAXException("Balise "+qName+" inconnue.");
			}
		}*/
	}
	//détection fin de balise
	public void endElement(String uri, String localName, String qName)
			throws SAXException{
		if(qName.equals("themes")){
			inthemes = false;
		}else if(qName.equals("Body")){
			themes.add(topic);
			topic = null;
			inTopic = false;
		}/*else if(qName.equals("nom")){
			Body.setNom(buffer.toString());
			buffer = null;
			inNom = false;
		}else if(qName.equals("prenom")){
			Body.setPrenom(buffer.toString());
			buffer = null;
			inPrenom = false;
		}else if(qName.equals("adresse")){
			Body.setAdresse(buffer.toString());
			buffer = null;
			inAdresse = false;
		}else{
			//erreur, on peut lever une exception
			throw new SAXException("Balise "+qName+" inconnue.");
		}   */       
	}
	//détection de caract�res
	public void characters(char[] ch,int start, int length)
			throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);       
	}
	//début du parsing
	public void startDocument() throws SAXException {
		System.out.println("D�but du parsing");
	}
	//fin du parsing
	public void endDocument() throws SAXException {
		System.out.println("Fin du parsing");
		System.out.println("Resultats du parsing");
		
	}
	
	public List<Topic> getthemes() {
		return themes;
	}
	public void setthemes(List<Topic> themes) {
		this.themes = themes;
	}
}
