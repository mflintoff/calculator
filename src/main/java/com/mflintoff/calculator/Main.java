package com.mflintoff.calculator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains logic to parse and validate the command line arguments, initialize the logging, and evaluate
 * the expression via the ExpressionEvaluator class. It's main method is the entry point into the application.
 *
 * @author Malcolm Flintoff
 */
public class Main {

    private static final String APP_NAME = "calculator";
    private static Logger log; // initialized in initializeLogging method below

    /**
     * Parse the command line options. If there was an error parsing the options, or if the -h parameter was present,
     * this method will print any error message along with the usage instructions to stdout and return null.
     *
     * @param args the raw arguments array passed to the main method.
     * @return the parsed command line arguments, or null if there was an error or -h argument is present.
     */
    private static CommandLineArguments parseCommandLineOptions(String[] args) {
        Options options = new Options();
        Option expressionOpt = Option.builder("e")
                .argName("expression")
                .longOpt("expression")
                .desc("expression to evaluate")
                .hasArg()
                .build();
        options.addOption(expressionOpt);

        Option logLevelOpt = Option.builder("l")
                .argName("loglevel")
                .longOpt("loglevel")
                .desc("log level (optional) -- supported values are INFO, ERROR, and DEBUG. Default value is INFO")
                .hasArg()
                .build();
        options.addOption(logLevelOpt);

        Option logFileOpt = Option.builder("f")
                .argName("logfile")
                .longOpt("logfile")
                .desc("log file (optional) -- the path to the log file to use. File is created if it doesn't already " +
                        " exist, otherwise it's appended to. If omitted, logs to stdout instead")
                .hasArg()
                .build();
        options.addOption(logFileOpt);

        Option helpOpt = Option.builder("h")
                .argName("help")
                .longOpt("help")
                .desc("prints the usage instructions")
                .build();
        options.addOption(helpOpt);

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            // for general parsing errors, print the error and the usage instructions
            System.out.println(e.getMessage());
            formatter.printHelp(APP_NAME, options);
            return null;
        }

        // print usage instructions if requested
        if (cmd.hasOption("help")) {
            formatter.printHelp(APP_NAME, options);
            return null;
        }

        // parse expression - if missing, print error message and usage instructions
        String expression = null;
        if (cmd.hasOption("expression")) {
            expression = cmd.getOptionValue("expression").toString();
        } else {
            System.out.println("Missing required option: expression");
            formatter.printHelp(APP_NAME, options);
            return null;
        }

        // parse log level (optional parameter). Default is INFO. If invalid value provided, print error message and
        // usage instructions
        Level logLevel = Level.getLevel("INFO");
        if (cmd.hasOption("loglevel")) {
            String logLevelStr = cmd.getOptionValue("loglevel").toString();
            logLevel = Level.getLevel(logLevelStr.toUpperCase());
            if (logLevel == null) {
                System.out.println("invalid log level: " + logLevelStr);
                formatter.printHelp(APP_NAME, options);
                return null;
            }
        }

        // parse log file (optional parameters). If omitted, outputs to stdout instead.
        String logFile = null;
        if (cmd.hasOption("logfile")) {
            logFile = cmd.getOptionValue("logfile").toString();
        }

        return new CommandLineArguments(expression, logLevel, logFile);
    }

    /**
     * Initialize log4j2 logging framework. If logFileLocation is not null, output will be logged to a file, otherwise
     * output is logged to stdout.
     *
     * @param logLevel the log level. eg. DEBUG
     * @param logFileLocation the log file location (optional).
     */
    private static synchronized void initializeLogging(Level logLevel, String logFileLocation) {
        if (log != null) {
            return;
        }
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable");

        String appenderName = null;
        if (logFileLocation != null) {
            // create a file appender
            appenderName = "fileAppender";
            AppenderComponentBuilder appenderBuilder = builder.newAppender(appenderName, "FILE")
                    .addAttribute("fileName", logFileLocation)
                    .add(layoutBuilder);
            builder.add(appenderBuilder);
        } else {
            // create a console appender
            appenderName = "stdout";
            AppenderComponentBuilder appenderBuilder = builder.newAppender(appenderName, "CONSOLE")
                    .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
            appenderBuilder.add(layoutBuilder);
            builder.add(appenderBuilder);
        }

        // create root level logger
        builder.add(builder.newRootLogger(logLevel)
                .add(builder.newAppenderRef(appenderName)));

        Configurator.initialize(builder.build());
        log = LoggerFactory.getLogger(Main.class);
    }

    /**
     * Simple helper class to encapsulate parsed command line arguments.
     */
    private static class CommandLineArguments {
        public final String expression;
        public final Level logLevel;
        public final String logFile;

        public CommandLineArguments(String expression, Level logLevel, String logFile) {
            this.expression = expression;
            this.logLevel = logLevel;
            this.logFile = logFile;
        }
    }

    public static void main(String[] args) {
        CommandLineArguments commandLineArguments = parseCommandLineOptions(args);
        if (commandLineArguments == null) {
            // parseCommandLineOptions method will have already logged error and usage instructions to stdout
            return;
        }

        initializeLogging(commandLineArguments.logLevel, commandLineArguments.logFile);

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String result = null;
        try {
            result = evaluator.evaluate(commandLineArguments.expression);
        } catch (EvaluationException e) {
            log.error(e.getMessage());
        }

        if (result == null) {
            result = "Could not calculate result due to previous error -- see error logs for details";
        }
        System.out.println(result);
    }
}
