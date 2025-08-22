package com.qa.api.runner;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.testng.TestNG;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Executes TestNG XML files from testrunners directory with simplified API
 */
public class TestRunner {
	private static final Logger LOG = LogManager.getLogger(); 
	private static final String TESTRUNNERS_PATH = "src/test/resources/testrunners";
	private static final String DEFAULT_XML = "testng_e2e_regression.xml";

	public static void main(String[] args) {
		String xmlFile = args.length > 0 ? args[0] : DEFAULT_XML;
		new TestRunner().execute(xmlFile);
	}

	/**
	 * Main execution entry point
	 */
	public void execute(String xmlFile) {
		initializeEnvironment();
		executeTestSuite(xmlFile);
		LOG.info("=== TestRunner Completed ===");
	}

	/**
	 * Execute a single test suite or all suites if null
	 */
	public void executeTestSuite(String xmlFileName) {
		if (xmlFileName == null) {
			executeAllSuites();
		} else {
			executeSingleSuite(xmlFileName);
		}
	}

	/**
	 * Execute all XML files in testrunners directory
	 */
	public void executeAllSuites() {
		LOG.info("=== Executing All Test Suites ===");

		List<String> xmlFiles = findAllXmlFiles();
		if (xmlFiles.isEmpty()) {
			LOG.warn("No XML files found in: " + TESTRUNNERS_PATH);
			return;
		}

		logFoundFiles(xmlFiles);
		runTestNG(xmlFiles);
	}

	/**
	 * Execute a specific XML file
	 */
	private void executeSingleSuite(String xmlFileName) {
		LOG.info("=== Executing Test Suite: " + xmlFileName + " ===");

		String xmlPath = getXmlPath(xmlFileName);
		if (!isValidXmlFile(xmlPath)) {
			handleInvalidFile(xmlFileName);
			return;
		}

		LOG.info("Found: " + xmlFileName);
		runTestNG(Arrays.asList(xmlPath));
	}

	/**
	 * Initialize environment for test execution
	 */
	private void initializeEnvironment() {
		setWorkingDirectory();
		setupClasspath();
	}

	/**
	 * Set working directory to project root
	 */
	private void setWorkingDirectory() {
		String projectRoot = new File("").getAbsolutePath();
		System.setProperty("user.dir", projectRoot);
	}

	/**
	 * Setup classpath for test execution
	 */
	private void setupClasspath() {
		try {
			File testClasses = new File("target/test-classes");
			File mainClasses = new File("target/classes");

			if (!testClasses.exists() || !mainClasses.exists()) {
				LOG.warn("WARNING: Compiled classes not found. Run 'mvn test-compile' first.");
				return;
			}

			URL[] urls = { testClasses.toURI().toURL(), mainClasses.toURI().toURL() };
			URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
			Thread.currentThread().setContextClassLoader(classLoader);

			LOG.info("Classpath configured for test execution");
		} catch (Exception e) {
			LOG.error("WARNING: Could not setup test classpath: " + e.getMessage());
		}
	}

	/**
	 * Execute TestNG with provided XML files
	 */
	private void runTestNG(List<String> xmlFiles) {
		LOG.info("\n=== Starting Test Execution ===");

		TestNG testng = new TestNG();
		testng.setTestSuites(xmlFiles);
		testng.run();

		LOG.info("\n=== Test Execution Completed ===");
	}

	/**
	 * Find all XML files in testrunners directory
	 */
	private List<String> findAllXmlFiles() {
		try {
			Path runnersDir = Paths.get(TESTRUNNERS_PATH);

			if (!Files.exists(runnersDir)) {
				System.out.println("ERROR: Testrunners directory not found: " + TESTRUNNERS_PATH);
				return Arrays.asList();
			}

			return Files.list(runnersDir).filter(Files::isRegularFile)
					.filter(path -> path.toString().toLowerCase().endsWith(".xml")).sorted().map(Path::toAbsolutePath)
					.map(Path::toString).collect(Collectors.toList());

		} catch (Exception e) {
			LOG.error("ERROR: Could not scan testrunners directory: " + e.getMessage());
			return Arrays.asList();
		}
	}

	/**
	 * Get full path for XML file
	 */
	private String getXmlPath(String xmlFileName) {
		return TESTRUNNERS_PATH + "/" + xmlFileName;
	}

	/**
	 * Check if XML file exists
	 */
	private boolean isValidXmlFile(String xmlPath) {
		return Files.exists(Paths.get(xmlPath));
	}

	/**
	 * Handle invalid file scenario
	 */
	private void handleInvalidFile(String xmlFileName) {
		LOG.error("ERROR: XML file not found: " + xmlFileName);
		LOG.info("\nAvailable XML files:");

		findAllXmlFiles().stream().map(path -> Paths.get(path).getFileName().toString())
				.forEach(name -> System.out.println("  - " + name));
	}

	/**
	 * Log found XML files
	 */
	private void logFoundFiles(List<String> xmlFiles) {
		LOG.info("Found " + xmlFiles.size() + " XML files:");
		xmlFiles.stream().map(path -> Paths.get(path).getFileName().toString())
				.forEach(name -> System.out.println("  - " + name));
	}
}