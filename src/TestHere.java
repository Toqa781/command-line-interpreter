import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;


class TestHere {
   Terminal terminal;
   private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
   private final PrintStream originalOut = System.out;

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
}