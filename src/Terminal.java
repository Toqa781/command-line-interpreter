import java.io.File;
import java.util.Comparator;
public class Terminal {
    public void pwd(){
        System.out.println(Main.currentDirectory);
    }

    public void cd(String args,String address){
        if(args.equalsIgnoreCase("..")){
            try {
                int lastSlash=address.lastIndexOf("\\");
                String addressParent=address.substring(0,lastSlash);
                Main.currentDirectory=addressParent;
            }
            catch (Exception e){
                System.out.println("You are at the root!");
            }
        }
        else if(args.equalsIgnoreCase("")){
            try {
                int lastSlash=address.indexOf("\\");
                String root=address.substring(0,lastSlash);
                Main.currentDirectory=root;
            }
            catch (Exception e){
                System.out.println("You are at the root!");
            }
        }
        else{
            File targetDir=new File(args);
            if (targetDir.exists() && targetDir.isDirectory()) {
                Main.currentDirectory = targetDir.getAbsolutePath();
            } else {
                System.out.println("The specified path does not exist or is not a directory.");
            }
        }
    }
    public void mkdir(String directoryName){
        if(directoryName.length() == 0) {
            System.out.println("mkdir takes a directory name parameter!");
        } else {
            File newDirectory = new File(Main.currentDirectory + "\\" + directoryName);
            if(!newDirectory.exists()){
                newDirectory.mkdir();
                System.out.println(directoryName + " directory created.");
            } else {
                System.out.println("The directory already exists!");
            }
        }
    }

    public void rmdir(String directoryName){
        File targetDirectory = new File(Main.currentDirectory + "\\" + directoryName);
        if(directoryName.length() == 0) {
            System.out.println("rmdir takes a directory name parameter!");
        } else {
            if(!targetDirectory.exists()){
                System.out.println("This directory does not exist.");
            } else {
                if(targetDirectory.delete())
                    System.out.println(directoryName + " directory deleted.");
                else
                    System.out.println("This directory is not empty!");
            }
        }
    }

    public void ls (File directory, boolean showAll, boolean reverse) {
        File[] files = directory.listFiles();
        if (files != null) {
            // (ls -r)
            if (reverse) {
                java.util.Arrays.sort(files, (a, b) -> b.getName().compareTo(a.getName()));
            } else {
                java.util.Arrays.sort(files, Comparator.comparing(File::getName));
            }
            //(ls -a)
            for (File file : files) {
                if (showAll || !file.getName().startsWith(".")) {
                    System.out.println(file.getName());
                }
            }
        } else {
            System.out.println("Error accessing directory: " + directory.getPath());
        }
    }
}
