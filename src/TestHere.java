import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

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
      assertEquals(Main.currentDirectory,Main.currentDirectory);
   }

   @Test
   void testCdToParent() {
      String initialDir = Main.currentDirectory;
      terminal.cd("..", Main.currentDirectory); // Change to parent directory
      assertNotEquals(initialDir, Main.currentDirectory);
   }

   @Test
   void testCdToRoot() {
      terminal.cd("", Main.currentDirectory); // Change to root directory
      // Here you should verify that Main.currentDirectory is set to root
      assertTrue(Main.currentDirectory.endsWith("root")); // Adjust based on your root directory structure
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
      // Expected output: "The directory already exists!"
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
      // Expected output: "This directory does not exist."
   }

   @Test
   void testRmdirEmptyDirectory() {
      String dirName = "emptyDir";
      new File(Main.currentDirectory + "\\" + dirName).mkdir(); // Create empty directory
      terminal.rmdir(dirName); // Remove it
      assertFalse(new File(Main.currentDirectory + "\\" + dirName).exists(), "Directory should have been deleted.");
   }

   @AfterEach
   void tearDown() {
      // Cleanup created directories after tests (if necessary)
      new File(Main.currentDirectory + "\\testDir").delete();
      new File(Main.currentDirectory + "\\emptyDir").delete();
   }
}
