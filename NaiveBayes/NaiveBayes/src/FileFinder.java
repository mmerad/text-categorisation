import java.io.File;

public class FileFinder {

	public FileFinder() {
		super();
	}
	
	public  File[] findFiles(String directoryPath) {
		File directory = new File(directoryPath);
		if(!directory.exists()){
			System.out.println("Le fichier/répertoire '"+directoryPath+"' n'existe pas");
		}else if(!directory.isDirectory()){
			System.out.println("Le chemin '"+directoryPath+"' correspond à un fichier et non à un répertoire");
		}else{
			File[] subfiles = directory.listFiles();
			String message = "Le répertoire '"+directoryPath+"' contient "+ subfiles.length+" fichier"+(subfiles.length>1?"s":"");
			System.out.println(message);
			return subfiles;
		}
		return null;
	}

}
