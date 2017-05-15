# Calculator
A calculator program in Java that evaluates expressions in a very simple integer expression language. The program takes an input on the command line, computes the result, and prints it to the console

# Requirments
* Java 5+
* Maven 3+

# Build Instructions
* git clone https://github.com/mflintoff/calculator.git
* cd calculator
* mvn clean install

# Usage
After compilation, an executable jar is created in the target directory. It can be called like:
*  ```java -jar target/calculator-1.0.jar <options> ```

To see a detailed explanation of options, use -h parameter for help.

 ```
java -jar target/calculator-1.0.jar -h
usage: calculator
 -e,--expression <expression>   expression to evaluate
 -f,--logfile <logfile>         log file (optional) -- the path to the log
                                file to use. File is created if it doesn't
                                already  exist, otherwise it's appended
                                to. If omitted, logs to stdout instead
 -h,--help                      prints the usage instructions
 -l,--loglevel <loglevel>       log level (optional) -- supported values
                                are INFO, ERROR, and DEBUG. Default value
                                is INFO
 ```
 
 ## Examples
For simple addition, with log level INFO (default) to stdout (default):
* ```java -jar target/calculator-1.0.jar -e "add(1, 2)"```

For a more complex expression, with log level DEBUG to a log.txt file in the current working directory:
* ```java -jar target/calculator-1.0.jar -e "let(a, 5, let(b, mult(a, 10), add(b, a)))" -l DEBUG -f log.txt```




