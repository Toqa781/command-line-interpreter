import java.io.File;
public class Terminal {
    public void pwd(){
        System.out.println(Main.currentDirectory);
    }
    public void mkdir(String directoryName){
        if(directoryName.length()==0)
            System.out.println("mkdir takes a directory name parameter!");
        else{
            File newDirectory=new File(directoryName);  //make a new file in the current directory
            if(!newDirectory.exists()){
                newDirectory.mkdir();
                System.out.println(directoryName+" directory created.");
            }
            else{
                System.out.println("The directory is already exist!");
            }
        }
    }

    public void rmdir(String directoryName){
        File targetDirectory=new File(directoryName);
        if(directoryName.length()==0){
            System.out.println("rmdir takes a directory name parameter!");
        }
        else{
            if(!targetDirectory.exists()){
                System.out.println("This directory is not exit");
            }
            else{
                if(targetDirectory.delete())
                    System.out.println(targetDirectory+" directory deleted.");
                else
                    System.out.println("This directory is not empty!");
            }
        }
    }

    private static void listFiles(File directory, boolean showAll, boolean recursive) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                //(if ls -a)
                if (showAll || !file.getName().startsWith(".")) { 
                    System.out.println(file.getName());
                }
                //(if ls -r)
                if (recursive && file.isDirectory()) { 
                    System.out.println("[" + file.getName() + "]:");
                    listFiles(file, showAll, true); 
                }
            }
        } else {
            System.out.println("Error accessing directory: " + directory.getPath());
        }
    }
}
