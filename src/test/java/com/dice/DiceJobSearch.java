package com.dice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DiceJobSearch {
	public static void main(String[] args) throws IOException {
		// Set up chrome driver path
		WebDriverManager.chromedriver().setup();
		;
		// invoke selenium webdriver
		WebDriver driver = new ChromeDriver();
		// fullScreen
		driver.manage().window().maximize();
		// set wait time in case web page is slow
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		/*
		 * step 1: Launch browser and navigate to https://dice.com expected: dice home
		 * page should be displayed
		 */
		String url = "https://dice.com";
		driver.get(url);

		String actualTitle = driver.getTitle();
		String expectedTitle = "Job Search for Technology Professionals | Dice.com";

		if (actualTitle.equals(expectedTitle)) {
			System.out.println("Pass");
		} else {
			System.out.println("Fail");
			throw new RuntimeException("Step Failed");
		}

		FileReader fr = new FileReader("KEYWORDS.txt");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> keywords = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			keywords.add(br.readLine());
		}
		
		ArrayList<String> content = new ArrayList<>();
		String keyword;
		for (int i = 0; i < keywords.size(); i++) {
			keyword = keywords.get(i);
			driver.findElement(By.id("search-field-keyword")).clear();
			driver.findElement(By.id("search-field-keyword")).sendKeys(keyword);

			String location = "21117";
			driver.findElement(By.id("search-field-location")).clear();
			driver.findElement(By.id("search-field-location")).sendKeys(location);

			driver.findElement(By.id("findTechJobs")).click();

			String count = driver.findElement(By.id("posiCountId")).getText();

			int countResult = Integer.parseInt(count.replaceAll(",", ""));
			
			content.add("Keyword : " + keyword + " | Number of Jobs Available " + countResult);

			if (countResult > 0) {
				System.out.println("Step Pass: Keyword " + keyword + " search returned " + countResult + " results int "
						+ location);
			} else {
				System.out.println("Step failed: Keyword " + keyword + " search returned " + countResult
						+ " results int " + location);
			}

			driver.navigate().back();
		}

		 driver.close();
		 System.out.println("Test Completed - " + LocalDateTime.now());
		 
		 for(int i = 0; i < content.size(); i++ ) {
			 System.out.println(content);
		 }
	}

}
