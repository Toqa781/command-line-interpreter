# Command Line Interpreter

**A custom command-line interpreter** built in Java, supporting shell-like commands.  
It allows directory navigation, file manipulation, and content display in a simulated CLI environment.

## Features

* **Directory Navigation**
* **File Manipulation**
* **Content Display**
* **Error Handling**

## Commands

| Command  | Description                            |
|----------|----------------------------------------|
| `cd`     | Change directory                       |
| `pwd`    | Print working directory                |
| `mkdir`  | Create a new directory                 |
| `rmdir`  | Remove an unempty directory            |
| `ls`     | List files and directories             |
| `cat`    | Display or edit file contents          |
| `touch`  | Create a new file                      |
| `rm`     | Remove files or dicrectories           |
| `mv`     | Move or rename files\directories       |
| `help`   | Display available commands and usage   |
| `exit`   | Exit the interpreter                   |

### Usage Example

```bash
$ pwd
/home/user/command-line-interpreter

$ mkdir test
$ cd test
$ touch example.txt
$ ls
example.txt

$ cat > example.txt
Hello, this is a test file.
@exit
$ cat example.txt
Hello, this is a test file.
```

## Installation

1- Clone the repository:
```bash
git clone https://github.com/Toqa781/command-line-interpreter.git
cd command-line-interpreter
```

2-Compile the project (assuming Java is installed):
```bash
javac -d bin src/*.java
```

3-Run the interpreter:
```bash
java -cp bin Main
```

## Testing

The project includes unit tests for each command to ensure functionality. Run tests with a JUnit-compatible IDE (like IntelliJ) or use Maven if available.
