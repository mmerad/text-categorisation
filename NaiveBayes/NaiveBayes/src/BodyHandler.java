import java.util.LinkedList;
import java.util.List;
//import java.util.jar.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;


public class BodyHandler extends DefaultHandler{

	//résultats de notre parsing
	private List<Body> articles;

	private Body body;
	private boolean inarticles, inBody;
	
	//buffer nous permettant de récupérer les données 
	private StringBuffer buffer;

	// simple constructeur
	public BodyHandler(){
		super();
	}
	
	//détection d'ouverture de balise
	public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) throws SAXException{
		
		if(qName.equals("xml")){
			articles = new LinkedList<Body>();
			inarticles = true;
		}else if(qName.equals("BODY")){
			body = new Body();
			inBody = true;
			buffer = new StringBuffer();
		}/*else {
			
			/*if(qName.equals("nom")){
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
		if(qName.equals("xml")){
			inarticles = false;
		}else if(qName.equals("BODY")){
			body.contenu = buffer.toString();
			articles.add(body);
			body = null;
			inBody = false;
			buffer = null;
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
	//détection de caractères
	public void characters(char[] ch,int start, int length)
			throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);    
	}
	//début du parsing
	public void startDocument() throws SAXException {
		//System.out.println("Début du parsing");
	}
	//fin du parsing
	public void endDocument() throws SAXException {
		//System.out.println("Fin du parsing");
		//System.out.println("Resultats du parsing");
		
	}
	
	public List<Body> getArticles() {
		return articles;
	}
	public void setArticles(List<Body> articles) {
		this.articles = articles;
	}
}
