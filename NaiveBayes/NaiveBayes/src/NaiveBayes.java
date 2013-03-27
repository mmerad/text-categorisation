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
			algoNaives(b.contenu,"");
		}
		
	}
	
	//Appeler pour chaque body
	public void algoNaives(String b, String t)
	{
		b = b.replaceAll("\\.", "");//a tester
		String[] mots = b.split("\\ ");
		HashMap<String, Float> freq = new HashMap<String, Float>();
		
		int taille = mots.length;
		
		for(int i = 0; i < taille; i++)
		{
			float val = (freq.containsKey(mots[i]))? freq.get(mots[i]) : (float)0.0;
			freq.put(mots[i], val+1);
		}
		
		for(String a : freq.keySet()){
			System.out.println(a+" "+freq.get(a));
			
		}
		
		// TODO Voir si les TOPICS sont bien organisés <MOT><ESPACE><MOT><ESPACE><MOT>
		
		String[] topics = t.split("\\ ");
		
		System.out.println(topics[0]+" "+topics[1] );
		
		for(String mot : freq.keySet() )
		{
			for(int k = 0 ; k<topics.length ; k++){
				if(probaMot.containsKey(mot)){
					if(probaMot.get(mot).containsKey(topics[k])){
				
					float temp = probaMot.get(mot).get(topics[k]);
					temp = ((freq.get(mot)/taille)+temp)/2;
					freq.put(mot, temp);
					}else{
						freq.put(mot, (freq.get(mot)/taille));
						System.out.println(" Gros test : "+mot+" "+topics[k]+" "+freq.get(mot));
						}
					}else{
						System.out.println(" Gros test : "+mot+" "+topics[k]);
						freq.put(mot, (freq.get(mot)/taille));
						}

				}
		}
		
		
		for(String mot : freq.keySet()){
			for(int j = 0 ; j<topics.length ; j++){
				if(!probaMot.containsKey(mot)){
					probaMot.put(mot, new HashMap<String,Float>());
					probaMot.get(mot).put(topics[j], freq.get(mot));
				}
				
				if(!probaMot.get(mot).containsKey(topics[j])){
					probaMot.get(mot).put(topics[j], freq.get(mot));
					}
				}
			}
		}
		

			
		public static void main(String[] args) {
		// TODO Auto-generated method stub
		NaiveBayes nb = new NaiveBayes();
		nb.algoNaives("Adeline est gentille. Et Adeline est très très très gentille.","rose poisson");
		nb.algoNaives("Martin est gentil. Et Martin est très très très généreux.","poisson rouge");
		
		for(String mot : nb.probaMot.keySet()){
			for(String top : nb.probaMot.get(mot).keySet()){
				System.out.println(mot+" "+top+" "+nb.probaMot.get(mot).get(top));
				
			}		
		}
	}
}
