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
        public void rm(String[] fileNames) throws IOException {
        for (String fileName : fileNames) {
            Path path = Paths.get(fileName);
            if (Files.exists(path)) {
                if (Files.isDirectory(path)) {
                    Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }
                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                    System.out.println("Removed directory: " + fileName);
                } else {
                    Files.delete(path);
                    System.out.println("Removed file: " + fileName);
                }
            } else {
                System.out.println("File or directory not found: " + fileName);
            }
        }
    }
    public static void mv(String... paths) {
        if (paths.length < 2) {
            System.out.println("Insufficient arguments provided.");
            return;
        }

        Path targetPath = Paths.get(paths[paths.length - 1]);
        boolean isDirectory = Files.isDirectory(targetPath);

        try {
            if (paths.length == 2 && !isDirectory) {
                // Case 1: Move contents of file1 into file2 and delete file1
                Path sourcePath = Paths.get(paths[0]);
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File renamed and moved successfully");
            } else {
                // Case 2: Move all files into the target directory
                if (!isDirectory) {
                    System.out.println("Last argument must be a directory if moving multiple files.");
                    return;
                }
                for (int i = 0; i < paths.length - 1; i++) {
                    Path sourcePath = Paths.get(paths[i]);
                    Path destinationPath = targetPath.resolve(sourcePath.getFileName());
                    Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Moved " + sourcePath.getFileName() + " to " + targetPath);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to move the file(s): " + e.getMessage());
        }
    }


     public void cat(String[] fileNames) throws IOException {
        BufferedReader br;
        String inputString = "";
        Scanner in = new Scanner(System.in);

        if (fileNames.length == 0) {
            System.out.println("No files provided.");
            return;
        }

        if (fileNames[0].equalsIgnoreCase(">")) {
            if (fileNames.length < 2) {
                System.out.println("Usage: > <filename>");
                return;
            }
            String filename = fileNames[1];
            try {
                File myObj = new File(filename);
                if (myObj.createNewFile()) {
                    System.out.println("File Created: " + myObj.getName());
                } else {
                    System.out.println("File already exists");
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(myObj))) {
                    System.out.println("Write what you want in this file and type @exit to close the file:");
                    while (!inputString.equalsIgnoreCase("@exit")) {
                        inputString = in.nextLine();
                        if (!inputString.equalsIgnoreCase("@exit")) {
                            bw.write(inputString);
                            bw.newLine();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("An Error occurred: " + e.getMessage());
            }
        } else if (fileNames[0].equalsIgnoreCase(">>")) {
            if (fileNames.length < 2) {
                System.out.println("Usage: >> <filename>");
                return;
            }
            String filename = fileNames[1];
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
                System.out.println("Write what you want in this file and type @exit to close the file:");
                while (!inputString.equalsIgnoreCase("@exit")) {
                    inputString = in.nextLine();
                    if (!inputString.equalsIgnoreCase("@exit")) {
                        out.println(inputString);
                    }
                }
            } catch (IOException e) {
                System.out.println("Can't write to this file: " + e.getMessage());
            }
        } else {
            for (String fileName : fileNames) {
                try {
                    br = new BufferedReader(new FileReader(fileName));
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    br.close(); // Close the BufferedReader
                } catch (FileNotFoundException e) {
                    System.out.println("File not found: " + fileName);
                } catch (IOException e) {
                    System.out.println("Can't read file: " + fileName);
                }
            }
        }
    }

    public void touch(String[] fileNames) {
        if (fileNames.length == 0) {
            System.out.println("No file names provided for touch command.");
            return;
        }

        for (String fileName : fileNames) {
            if (fileName == null || fileName.trim().isEmpty()) {
                System.out.println("Invalid file name: " + fileName);
                continue;
            }

            Path path = Paths.get(fileName);
            System.out.println("Attempting to create file at: " + path.toAbsolutePath());

            try {
                Path parentDir = path.getParent();
                if (parentDir != null && !Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                    System.out.println("Created directories: " + parentDir.toAbsolutePath());
                }
                if (Files.notExists(path)) {
                    Files.createFile(path);
                    System.out.println("File created: " + fileName);
                } else {
                    System.out.println("File already exists: " + fileName);
                }
            } catch (IOException e) {
                System.out.println("Failed to create file " + fileName + ": " + e.getMessage());
            }
        }
    }
}
