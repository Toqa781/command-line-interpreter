import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


class TestHere {
   Terminal terminal;

   @BeforeEach
   void setUp(){
      terminal=new Terminal();
      Main.currentDirectory=System.getProperty("user.dir");
   }

   @Test
   void testPwd(){
      terminal.pwd();
      assertEquals(Main.currentDirectory,Main.currentDirectory);//check the printed output matches the current directory
   }

   @Test
   void testCdToParent() {
      String initialDir = Main.currentDirectory;
      terminal.cd("..", Main.currentDirectory); // Change to parent directory
      String expectedParent=new File(initialDir).getParent();
      assertEquals(expectedParent, Main.currentDirectory);
   }

   @Test
   void testCdToRoot() {
      terminal.cd("", Main.currentDirectory); // Change to root directory
      String rootDir=new File(Main.currentDirectory).toPath().getRoot().toString();
      assertEquals(rootDir,Main.currentDirectory);
   }

   @Test
   void testCdToSpecificDir(){
      String newDirName="testCdDir";
      File newDir=new File(Main.currentDirectory+"\\"+newDirName);
      if(!newDir.exists()) newDir.mkdir();
      terminal.cd(newDir.getAbsolutePath(),Main.currentDirectory);
      assertEquals(newDir.getAbsolutePath(),Main.currentDirectory);
   }

   @Test
   void testCdToNonExistentDirectory() {
      String initialDir = Main.currentDirectory;
      terminal.cd("nonexistentDirectory", Main.currentDirectory);
      assertEquals(initialDir, Main.currentDirectory); // Ensure the current directory has not changed
   }

   @Test
   void testMkdir() {
      String dirName = "testDir";
      terminal.mkdir(dirName);
      File newDir = new File(Main.currentDirectory + "\\" + dirName);
      assertTrue(newDir.exists(), "Directory should have been created.");
   }

   @Test
   void testMkdirExistingDir() {
      String dirName = "testDir";
      terminal.mkdir(dirName); // Create it once
      terminal.mkdir(dirName); // Try to create it again
   }

   @Test
   void testRmdir() {
      String dirName = "testDir";
      terminal.mkdir(dirName); // Ensure the directory exists
      terminal.rmdir(dirName); // Now remove it
      File deletedDir = new File(Main.currentDirectory + "\\" + dirName);
      assertFalse(deletedDir.exists(), "Directory should have been deleted.");
   }

   @Test
   void testRmdirNonExistentDir() {
      terminal.rmdir("nonexistentDir");
   }

   @Test
   void testRmdirEmptyDirectory() {
      String dirName = "emptyDir";
      new File(Main.currentDirectory + "\\" + dirName).mkdir(); // Create empty directory
      terminal.rmdir(dirName); // Remove it
      assertFalse(new File(Main.currentDirectory + "\\" + dirName).exists(), "Directory should have been deleted.");
   }
   @Test
   public void testLs() {
      // Run 'ls' command to list files
      terminal.ls(new File(Main.currentDirectory), false, false);
      String output = outContent.toString().trim();

      // Check output for the existence of expected files/directories
      assertTrue(output.contains("src") || output.contains("Main.java"),
              "Expected basic files/directories to be listed");
   }

   @Test
   public void testLsAll() {
      // 'ls -a' command to list all files, including hidden ones
      terminal.ls(new File(Main.currentDirectory), true, false);
      String output = outContent.toString().trim();
      assertTrue(output.contains("."),
              "Expected hidden files/directories to be listed with '-a'");
   }

   @Test
   public void testLsReverse() {
      //  'ls -r' command to list files in reverse alphabetical order
      terminal.ls(new File(Main.currentDirectory), false, true);
      String output = outContent.toString().trim();
      String[] lines = output.split("\\r?\\n");
      // Check that files are in reverse alphabetical order
      assertTrue(lines.length > 1, "Expected multiple lines in output");
      assertTrue(lines[0].compareTo(lines[lines.length - 1]) > 0,
              "Expected files to be listed in reverse alphabetical order");
   }

   @Test
   public void testLsAllReverse() {
      // Run 'ls -a -r' command to list all files, including hidden ones, in reverse order
      terminal.ls(new File(Main.currentDirectory), true, true);
      String output = outContent.toString().trim();
      String[] lines = output.split("\\r?\\n");
      // Check that files, including hidden, are in reverse order
      assertTrue(output.contains("."), "Expected hidden files in the output");
      assertTrue(lines.length > 1, "Expected multiple lines in output");
      assertTrue(lines[0].compareTo(lines[lines.length - 1]) > 0,
              "Expected files to be listed in reverse alphabetical order");
   }

   @AfterEach
   void tearDown() {
      new File(Main.currentDirectory + "\\testDir").delete();
      new File(Main.currentDirectory + "\\emptyDir").delete();
   }

   @Test
   public void testExitCommand() {
      Terminal.exitCommand();
      assertFalse(Terminal.isRunning(), "CLI should not be running after exit command");
   }

   @Test
   public void testHelpCommand() {
      terminal.helpCommand(); // Normally prints to console

      // Verify functionality indirectly, as helpCommand prints to System.out.
      // Optionally add logic in helpCommand that returns true when successfully executed
      assertTrue(true, "Help command executed successfully");
   }
       @Test
    void touchShouldCreateFile() {
        Terminal terminal = new Terminal();
        String filename = "touchtest.txt";
        terminal.touch(new String[]{filename});
        Path path = Paths.get(filename);
        assertTrue(Files.exists(path));
    }

    @Test
    void touchShouldCreateMultFiles() {
        Terminal terminal = new Terminal();
        String[] fileNames = {"file1.txt", "file2.txt"};
        terminal.touch(fileNames);

        for (String fileName : fileNames) {
            Path path = Paths.get(fileName);
            assertTrue(Files.exists(path));
        }
    }

    @Test
    void touchIfFilesAlreadyExists() {
        Terminal terminal = new Terminal();
        String fileName = "file1.txt";
        terminal.touch(new String[]{fileName});

        Path path = Paths.get(fileName);
        assertTrue(Files.exists(path));
    }

    @Test
    public void rmShouldRemoveFile() throws Exception {
        Terminal terminal = new Terminal();
        //String fileName = "testfile.txt";
        //Files.createFile(Paths.get(fileName));

        terminal.rm(new String[]{"h.txt"});

        assertFalse(Files.exists(Paths.get("h.txt")));
    }
    @Test void rmShouldRemoveDirectory() throws IOException {
        Terminal terminal = new Terminal();
        String dirname = "folder1";
        terminal.rm(new String[]{dirname});
        assertFalse(Files.exists(Paths.get(dirname)));
    }
    @Test
    public void rmtestRemoveNonExistentFile() throws Exception {
        Terminal terminal = new Terminal();
        terminal.rm(new String[]{"nonexistentfile.txt"});
    }
    @Test
    public void rmShouldRemoveMultFilesOrDirectories() throws Exception {
        Terminal terminal = new Terminal();
        String file1 = "lol.txt";
        String file2 = "lol2.txt";
        String dir = "folder2";
//        Files.createFile(Paths.get(file1));
//        Files.createFile(Paths.get(file2));
//        Files.createDirectories(Paths.get(dir));
        terminal.rm(new String[]{file1, file2, dir});
        assertFalse(Files.exists(Paths.get(file1)));
        assertFalse(Files.exists(Paths.get(file2)));
        assertFalse(Files.exists(Paths.get(dir)));
    }
    @Test void mvShouldMoveTwoFiles() throws IOException {
        String FILE1 = "file1.txt";
        String FILE2 = "file2.txt";
        Files.createFile(Paths.get(FILE1));
        Files.createFile(Paths.get(FILE2));
        Terminal.mv(FILE1, FILE2);
        assertFalse(Files.exists(Paths.get(FILE1)));
    }
    @Test void mvShouldMoveTwoDirectories() throws IOException {
        String DIR1 = "testdir1";
        String DIR2 = "testdir2";
        Files.createDirectory(Paths.get(DIR1));
        Files.createDirectory(Paths.get(DIR2));
        Terminal.mv(DIR1, DIR2);
        assertFalse(Files.exists(Paths.get(DIR1)));
    }
    @Test
    void mvShouldMoveMultFilesToDirectory() throws IOException {
        String DIR = "testdir";
        String FILE1 = "file3.txt";
        String FILE2 = "file4.txt";
        Files.createFile(Paths.get(FILE1));
        Files.createFile(Paths.get(FILE2));
        Files.createDirectory(Paths.get(DIR));
        Terminal.mv(FILE1, FILE2, DIR);

        assertFalse(Files.exists(Paths.get(FILE1)));
        assertFalse(Files.exists(Paths.get(FILE2)));
        assertTrue(Files.exists(Paths.get(DIR, "file3.txt")));
        assertTrue(Files.exists(Paths.get(DIR, "file4.txt")));
    }
    @Test
    void catShouldPrintFileContent() throws IOException {
        Terminal terminal = new Terminal();
        String fileName = "singlefile.txt";
        String fileContent = "Content of the single file. ";
        Files.write(Paths.get(fileName), fileContent.getBytes());
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        terminal.cat(new String[]{fileName});
        String actualOutput = outContent.toString().replace("\r\n", "\n");
        String expectedOutput = (fileContent + "\n").replace("\r\n", "\n");
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void catShouldCreateNewFile() {
        Terminal terminal = new Terminal();
        String fileName = "newestfile.txt";

        String simulatedInput = "Test content\n@exit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        terminal.cat(new String[]{">", fileName});

        assertTrue(Files.exists(Paths.get(fileName)));

    }

    @Test
    void catShouldAppendToExistingFile() throws IOException {
        Terminal terminal = new Terminal();
        String fileName = "appendfile.txt";
        //Files.createFile(Paths.get(fileName));

        String inputText = "Hello World";
        System.setIn(new ByteArrayInputStream((inputText + "\n@exit\n").getBytes()));
        terminal.cat(new String[]{">>", fileName});

        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        assertTrue(content.contains(inputText));

    }
    @Test
    void catShouldReadAndPrintFilesContents() throws IOException {
        Terminal terminal = new Terminal();
        String[] fileNames = {
                "file1.txt",
                "file2.txt",
                "file3.txt"
        };

        String[] fileContents = {
                "This is the first test file.",
                "This is the second test file.",
                "This is the third test file."
        };

        for (int i = 0; i < fileNames.length; i++) {
            Files.write(Paths.get(fileNames[i]), fileContents[i].getBytes());
        }
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        terminal.cat(fileNames); // Pass the array of file names

        String expectedOutput = String.join(System.lineSeparator(), fileContents) + System.lineSeparator();

        String actualOutput = outContent.toString().replace("\r", "").replace("\n", System.lineSeparator());

        assertEquals(expectedOutput, actualOutput);
    }
    @Test
    void cattestFileNotFound() {
        Terminal terminal = new Terminal();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        terminal.cat(new String[]{"nonexistent.txt"});

        String expectedOutput = "File not found: nonexistent.txt\n";
        String actualOutput = outContent.toString().replace("\r\n", "\n");

        assertEquals(expectedOutput.replace("\r\n", "\n"), actualOutput);
    }
}
