/**
 * 
 * @author agranet
 * @detail Le fichier principal
 */
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.*;

public class NaiveBayes {

	/**
	 * @param args
	 */
	//MOT -> TOPIC -> FREQUENCE
	
	Map<String,Map<String,Float>> probaMot;
	
	public NaiveBayes()
	{	
		probaMot = new HashMap<String, Map<String,Float>>();
	}
	
	private void calcule()
	{
		File[] fs = new FileFinder().findFiles("NotreDOssier ICI");
		
		for(int i = 0; i< fs.length; i++)
		{
			
			try {
				parseXML(fs[i]);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void parseXML(File f) throws ParserConfigurationException, SAXException, IOException
	{
		// crÃ©ation d'une fabrique de parseurs SAX
		SAXParserFactory fabrique = SAXParserFactory.newInstance();

		// crÃ©ation d'un parseur SAX
		SAXParser parseur = fabrique.newSAXParser();
		BodyHandler gestionnaire = new BodyHandler();
		parseur.parse(f, gestionnaire);
		List<Body> lb = gestionnaire.getArticles();
		
		for(Body b : lb)
		{
			algoNaives(b.contenuBody,"");
		}
		
	}
	
	//Appeler pour chaque body
	public void algoNaives(String b, String t)
	{
		b = b.replaceAll("\\.", "");//a tester
		String[] mots = b.split("\\ ");
		HashMap<String, Float> freq = new HashMap<String, Float>();
		
		for(int i = 0; i < mots.length; i++)
		{
			float val = (freq.containsKey(mots[i]))? freq.get(mots[i]) : (float)0.0;
			freq.put(mots[i], val+1);
		}
		
		
		int taille = mots.length;
		for(String mot : freq.keySet() )
		{
			freq.put(mot, (freq.get(mot)/taille));
			probaMot.put(mot, null);
		}
		
		
		// TODO Voir si les TOPICS sont bien organisés <MOT><ESPACE><MOT><ESPACE><MOT>
		
		String[] topics = t.split("\\ ");
		
		for(String mot : freq.keySet()){
			for(int j = 0 ; j<topics.length ; j++){
				if(j == 0) probaMot.put(mot, new HashMap<String,Float>());
					probaMot.get(mot).put(topics[j], freq.get(mot));
				}
			}
		
		for(String mot : probaMot.keySet()){
			for(String top : probaMot.get(mot).keySet()){
				System.out.println(mot+" "+top+" "+probaMot.get(mot).get(top));
				}
			}		
		}
	
			
		public static void main(String[] args) {
		// TODO Auto-generated method stub
		NaiveBayes nb = new NaiveBayes();
		nb.algoNaives("Martin est gentil. Et Martin est très très très généreux.","poisson rouge");
	}
}
