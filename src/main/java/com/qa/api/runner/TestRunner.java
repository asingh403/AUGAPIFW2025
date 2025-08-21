package com.qa.api.runner;

import org.testng.TestNG;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TestNG XML Runner for API Tests Executes TestNG XML files from testrunners
 * directory
 */
public class TestRunner {

	private static final String TESTRUNNERS_PATH = "src/test/resources/testrunners";

	public static void main(String[] args) {
		File projectRoot = new File("").getAbsoluteFile();
		System.setProperty("user.dir", projectRoot.getAbsolutePath());

		setupTestClasspath();

		TestRunner runner = new TestRunner();

		runner.runSingleXml("testng_full_regression.xml");
		System.out.println("=== TestRunner Main Method Completed ===");
	}
	/**
	 * Setup classpath to include test classes
	 */
	private static void setupTestClasspath() {
		try {
			File testClasses = new File("target/test-classes");
			File mainClasses = new File("target/classes");

			if (testClasses.exists() && mainClasses.exists()) {
				URL[] urls = { testClasses.toURI().toURL(), mainClasses.toURI().toURL() };

				URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
				Thread.currentThread().setContextClassLoader(classLoader);

				System.out.println("Classpath configured for test execution");
			} else {
				System.out.println("WARNING: Compiled classes not found. Run 'mvn test-compile' first.");
			}
		} catch (Exception e) {
			System.out.println("WARNING: Could not setup test classpath: " + e.getMessage());
		}
	}
	/**
	 * Run all TestNG XML files in testrunners directory
	 */
	public void runAllXmlFiles() {
		System.out.println("=== Running All TestNG XML Files ===");

		List<String> xmlFiles = findAllXmlFiles();
		if (xmlFiles.isEmpty()) {
			System.out.println("No XML files found in: " + TESTRUNNERS_PATH);
			return;
		}
		System.out.println("Found " + xmlFiles.size() + " XML files:");
		xmlFiles.forEach(file -> System.out.println("  - " + new File(file).getName()));

		TestNG testng = new TestNG();
		testng.setTestSuites(xmlFiles);

		System.out.println("\n=== Starting Test Execution ===");
		testng.run();
		System.out.println("\n=== Test Execution Completed ===");
	}

	/**
	 * Run specific TestNG XML files provided by user
	 */
	public void runSpecificXmlFiles(List<String> xmlFileNames) {
		System.out.println("=== Running Specific XML Files ===");

		List<String> validXmlPaths = new ArrayList<>();
		List<String> invalidFiles = new ArrayList<>();

		for (String xmlFileName : xmlFileNames) {
			String xmlPath = TESTRUNNERS_PATH + "/" + xmlFileName;
			File xmlFile = new File(xmlPath);

			if (xmlFile.exists()) {
				validXmlPaths.add(xmlPath);
				System.out.println("Found: " + xmlFileName);
			} else {
				invalidFiles.add(xmlFileName);
				System.out.println("Not found: " + xmlFileName);
			}
		}
		if (!invalidFiles.isEmpty()) {
			System.out.println("\nERROR: Some XML files not found:");
			invalidFiles.forEach(file -> System.out.println("  - " + file));
			System.out.println("\nAvailable XML files:");
			findAllXmlFiles().forEach(file -> System.out.println("  - " + new File(file).getName()));
			return;
		}
		if (validXmlPaths.isEmpty()) {
			System.out.println("No valid XML files to execute!");
			return;
		}
		TestNG testng = new TestNG();
		testng.setTestSuites(validXmlPaths);

		System.out.println("\nExecuting " + validXmlPaths.size() + " XML file(s):");
		validXmlPaths.forEach(path -> System.out.println("  - " + new File(path).getName()));
		System.out.println("\n=== Starting Test Execution ===");
		testng.run();
		System.out.println("\n=== Test Execution Completed ===");
	}

	/**
	 * Run single TestNG XML file or all files
	 * @param xmlFileName - if provided, runs only that file; if null, runs all
	 * files
	 */
	public void runSingleXml(String xmlFileName) {
		System.out.println("xml file name : " + xmlFileName);
		if (xmlFileName != null) {
			runSpecificXmlFiles(Arrays.asList(xmlFileName));
		} else {
			runAllXmlFiles();
		}
	}

	/**
	 * Find all XML files in testrunners directory
	 */
	private List<String> findAllXmlFiles() {
		List<String> xmlFiles = new ArrayList<>();
		File runnersDir = new File(TESTRUNNERS_PATH);

		if (!runnersDir.exists()) {
			System.out.println("ERROR: Testrunners directory not found: " + TESTRUNNERS_PATH);
			return xmlFiles;
		}
		File[] files = runnersDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

		if (files != null) {
			Arrays.sort(files);
			for (File file : files) {
				xmlFiles.add(file.getAbsolutePath());
			}
		}
		return xmlFiles;
	}
}