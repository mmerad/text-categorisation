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

import org.omg.CORBA.TCKind;
import org.xml.sax.*;

public class NaiveBayes {

	/**
	 * @param args
	 */
	 
	private int nb_text;
	private int nb_success;
	//MOT -> TOPIC -> FREQUENCE
	
	Map<String,Map<String,Float>> probaMot;
	
	public NaiveBayes()
	{	
		probaMot = new HashMap<String, Map<String,Float>>();
	}
	
	private float calcule()
	{
		File[] fs = new FileFinder().findFiles("./train");
		
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
		
		nb_text = 0;
		nb_success = 0;
		File[] ftest = new FileFinder().findFiles("./test");
		for(int i = 0; i< ftest.length; i++)
		{
			
			try {
				parseXMLTest(fs[i]);
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
		float ratio = ((float)nb_text/(float)nb_success) * (float)100;
		System.out.println("Resultat : \n  "+ nb_text+" analysés,  "+ ratio+"% de success");
		return ratio;
	}
	
	//train
	private void parseXML(File f) throws ParserConfigurationException, SAXException, IOException
	{
		// crÃ©ation d'une fabrique de parseurs SAX
		SAXParserFactory fabrique = SAXParserFactory.newInstance();

		// crÃ©ation d'un parseur SAX
		SAXParser parseur = fabrique.newSAXParser();
		SAXParser parseurTopic = fabrique.newSAXParser();
		TopicHandler gestionnaireTopic = new TopicHandler();
		BodyHandler gestionnaire = new BodyHandler();
		parseur.parse(f, gestionnaire);
		parseurTopic.parse(f, gestionnaireTopic);
		List<Body> lb = gestionnaire.getArticles();
		List<LinkedList<Topic>> lt = gestionnaireTopic.getthemes();
		
		for(Body b : lb)
		{
			algoNaives(b.contenu,transformString(lt.get(lb.indexOf(b))));
		}
		
	}
	
	private String transformString(LinkedList<Topic> l)
	{
		String rep = "";
		int i = 0;
		for(Topic t : l)
		{ 
			i++;
			rep += t.contenuTheme;
			if(i != l.size())
				rep += " ";
		}
		return rep;
	}
	
	//test
	private void parseXMLTest(File f) throws ParserConfigurationException, SAXException, IOException
	{
		// crÃ©ation d'une fabrique de parseurs SAX
		SAXParserFactory fabrique = SAXParserFactory.newInstance();

		// crÃ©ation d'un parseur SAX
		SAXParser parseur = fabrique.newSAXParser();
		SAXParser parseurTopic = fabrique.newSAXParser();
		BodyHandler gestionnaire = new BodyHandler();
		TopicHandler gestionnaireTopic = new TopicHandler();
		
		parseur.parse(f, gestionnaire);
		parseurTopic.parse(f, gestionnaireTopic);
		List<Body> lb = gestionnaire.getArticles();
		List<LinkedList<Topic>> lt = gestionnaireTopic.getthemes();
		nb_text ++;
		for(Body b : lb)
		{
			int index = lb.indexOf(b);
			List<String> rep = association(b.contenu);
			List<String> topics = separateTopic(lt.get(index));
			for(String s : rep)
			{
				if(!topics.isEmpty())
				{
					if(topics.contains(s))
						nb_success ++;
					else
						System.out.println("Topic trouvé par notre algorithme "+ s+" liste des topics du texte "+ topics.get(0) );
				}
			}
		}
		
	}
	
	private List<String> separateTopic(LinkedList<Topic> l)
	{
		List<String> ls = new ArrayList<String>();
		for(Topic t : l)
		{
			ls.add(t.contenuTheme);
		}
		return ls;
	}
	
	/**
	 * Fonction algoNaives
	 * Calcul
	 * @param b
	 * @param t
	 */
	
	
	//Appeler pour chaque body
	public void algoNaives(String b, String t)
	{
		b = b.replaceAll("\\.", "");//a tester
		String[] mots = b.split("\\ ");
		HashMap<String, Float> freq = new HashMap<String, Float>();
		HashMap<String,Map<String,Float>> freq2 = new HashMap<String,Map<String,Float>>();
		
		int taille = mots.length;
		
		for(int i = 0; i < taille; i++)
		{
			float val = (freq.containsKey(mots[i]))? freq.get(mots[i]) : (float)0.0;
			freq.put(mots[i], val+1);
		}
		
		
		
		// TODO Voir si les TOPICS sont bien organisés <MOT><ESPACE><MOT><ESPACE><MOT>
		
		String[] topics = t.split("\\ ");
		
		//System.out.println(topics[0]+" "+topics[1] );
		
		
		for(String mot : freq.keySet() )
		{
			for(int k = 0 ; k<topics.length ; k++){
				if(probaMot.containsKey(mot)){
					if(probaMot.get(mot).containsKey(topics[k]))
					{
						
						float temp = probaMot.get(mot).get(topics[k]);
						temp = (((freq.get(mot)/taille)+temp)/2)*(float)1.25;// Arbitraire : pondération de fréquence pour un mot/topic 
						if( !freq2.containsKey(mot))
							freq2.put(mot,new HashMap<String, Float>());
						freq2.get(mot).put(topics[k], temp);
						//System.out.println("If1 : "+mot+" "+temp + "  "+topics[k] );
					}
					else
					{
						if( !freq2.containsKey(mot))
							freq2.put(mot,new HashMap<String, Float>());
						//System.out.println(mot);
						freq2.get(mot).put(topics[k], (freq.get(mot)/taille));
						//System.out.println("Else 1 : "+mot+" "+topics[k]+" "+freq2.get(mot).get(topics[k]));
					}
				}else{
					if(!freq2.containsKey(mot))
						freq2.put(mot,new HashMap<String, Float>());
					freq2.get(mot).put(topics[k], (freq.get(mot)/taille));
					//System.out.println("Else 2 : "+mot+" "+topics[k]+" "+freq2.get(mot));
				}

				}
		}
		
		/*for(String a : freq2.keySet()){
		System.out.println(a+" "+freq2.get(a));

		}*/
		
		for(String mot : freq2.keySet()){
			
			for(int j = 0 ; j<topics.length ; j++){
				//System.out.println(topics[j]);
				if(!probaMot.containsKey(mot)){
					probaMot.put(mot, new HashMap<String,Float>());
					probaMot.get(mot).put(topics[j], freq2.get(mot).get(topics[j]));
				}
				else { 
					probaMot.get(mot).put(topics[j], freq2.get(mot).get(topics[j]));
				}
				//System.out.println("voir le topic mot"+ mot+ " la valeur" +freq2.get(mot));
				/*if(!probaMot.get(mot).containsKey(topics[j])){
					//System.out.println("voir le topic "+ topics[j]+ " la valeur" +freq2.get(mot));
					probaMot.get(mot).put(topics[j], freq2.get(mot));
					}*/
				}
			}
		}
	
	
		
		/**
		 * Fonction Association
		 * Retourne les topics d'un texte fourni en paramètre
		 * @param text_test
		 */
	
		public List<String> association(String text_test){
			text_test = text_test.replaceAll("\\.", "");
			String[] text_test_tab = text_test.split("\\ ");
			int taille = text_test_tab.length;
			
			float max_freq_1 = (float)0.0;
			String max_tpc_1 = "";
			float max_freq_2 = (float)0.0;
			String max_tpc_2 = "";
			
			
			/*for(int i = 0 ; i< taille ; i++){
				if(probaMot.containsKey(text_test_tab[i])){
					for(String tpc : probaMot.get(text_test_tab[i]).keySet()){
						if(max_freq_1 < probaMot.get(text_test_tab[i]).get(tpc)){
							max_freq_1 = probaMot.get(text_test_tab[i]).get(tpc);
							max_tpc_1 = tpc;
						}else if (max_freq_2 < probaMot.get(text_test_tab[i]).get(tpc)) {
							max_freq_2 = probaMot.get(text_test_tab[i]).get(tpc);
							max_tpc_2 = tpc;
						}
						
					}
				}
			}*/
			
			HashMap<String,Integer> tpc_h = new HashMap<String,Integer>();
			
			for(int i = 0 ; i< taille ; i++){
				if(probaMot.containsKey(text_test_tab[i])){
					for(String tpc : probaMot.get(text_test_tab[i]).keySet()){
						if(max_freq_1 <= probaMot.get(text_test_tab[i]).get(tpc)){
							max_freq_1 = probaMot.get(text_test_tab[i]).get(tpc);
							max_tpc_1 = tpc;
							if(tpc_h.containsKey(tpc)){
								int temp = tpc_h.get(tpc);
								temp = temp + 1 ;
								tpc_h.put(tpc, temp);
							}else{
								tpc_h.put(tpc, 1);
							}
						}else if (max_freq_2 <= probaMot.get(text_test_tab[i]).get(tpc)) {
							max_freq_2 = probaMot.get(text_test_tab[i]).get(tpc);
							max_tpc_2 = tpc;
							if(tpc_h.containsKey(tpc)){
								int temp = tpc_h.get(tpc);
								temp = temp + 1 ;
								tpc_h.put(tpc, temp);
							}else{
								tpc_h.put(tpc, 1);
							}
						}
					}
				}
			}
			
			// permet de calculer les deux meilleurs topics pour un texte donnée
			int max_1 = 0;
			String tpc_1 = "";
			int max_2 = 0;
			String tpc_2 = "";
			
			for(String tpc_m : tpc_h.keySet()){
				if(max_1 <= tpc_h.get(tpc_m)){
					max_2 =max_1;
					tpc_2 = tpc_1;
					max_1 = tpc_h.get(tpc_m);
					tpc_1 = tpc_m;
				}else{
					if(max_2 < tpc_h.get(tpc_m)){
					max_2 = tpc_h.get(tpc_m);
					tpc_2 = tpc_m;
				
					}
				}
			}
			
			ArrayList<String> rep = new ArrayList<String>();
			rep.add(tpc_1);
			rep.add(tpc_2);
			//System.out.println("Le sujet de ce texte est "+tpc_1+" & "+tpc_2);
			return rep;
		}
			
		
		/**
		 * Fonction MAIN
		 * @param args
		 */
		
		public static void main(String[] args) {
		// TODO Auto-generated method stub
		NaiveBayes nb = new NaiveBayes();
		//nb.algoNaives("Adeline est gentille. Et Adeline est très très très gentille.","rose poisson");
		//nb.algoNaives("Martin est gentil. Et Martin est très très très généreux.","rouge poisson");
		
		/*for(String mot : nb.probaMot.keySet()){
			for(String top : nb.probaMot.get(mot).keySet()){
				System.out.println(mot+" "+top+" "+nb.probaMot.get(mot).get(top));
				
			}		
		}*/
		
		//nb.association("gentille Adeline Martin gentil");
		float ratio = nb.calcule();
	}
}
