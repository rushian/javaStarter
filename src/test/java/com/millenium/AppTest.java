package com.millenium;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.HashMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppTest 
{
    private WebDriver driver;
    JavascriptExecutor js;
    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        // Aponta onde está o Chrome Driver
        // System.setProperty("webdriver.chrome.driver", "drivers/chrome/chromedriver.exe");
        //abrir navegador
        driver = new ChromeDriver(options); 
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(15000));
        js = (JavascriptExecutor) driver;
        new HashMap<String, Object>();
    }
    @AfterEach
    public void tearDown() {
        driver.quit();
    }
    @Test
    public void comprarPassagem() throws IOException {
        //navegar ate pagina
        driver.get("https://blazedemo.com/");
        //maximizar janela
        driver.manage().window().maximize();
        //escolher origem e destino
        driver.findElement(By.name("fromPort")).click();
        driver.findElement(By.name("fromPort")).click();
        {
            WebElement dropdown = driver.findElement(By.name("fromPort"));
            dropdown.findElement(By.xpath("//option[. = 'São Paolo']")).click();
        }
        driver.findElement(By.name("toPort")).click();
        {
            WebElement dropdown = driver.findElement(By.name("toPort"));
            dropdown.findElement(By.xpath("//option[. = 'Berlin']")).click();
        }
        driver.findElement(By.cssSelector(".btn-primary")).click();
        //escolher voo
        (new Actions(driver)).pause(java.time.Duration.ofSeconds(2)).perform();
        driver.findElement(By.cssSelector("tr:nth-child(1) .btn")).click();
        //realizar pagamento
        driver.findElement(By.id("cardType")).click();
        {
            WebElement dropdown = driver.findElement(By.id("cardType"));
            dropdown.findElement(By.xpath("//option[. = 'American Express']")).click();
        }
        driver.findElement(By.id("rememberMe")).click();
        driver.findElement(By.cssSelector(".btn-primary")).click();

        (new Actions(driver)).pause(java.time.Duration.ofSeconds(2)).perform();
        //realizar validacoes
        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Thank you for your purchase today!"));
        assertThat(driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(2)")).getText(), is("555 USD"));
        //tirar print
        tiraPrint((TakesScreenshot)driver);
    }

    public static void tiraPrint(TakesScreenshot driver) throws IOException {
        File scrFile = driver.getScreenshotAs(OutputType.FILE);
        // Formata a data atual com hora
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String dataHora = sdf.format(new Date());

        // Concatena a data e hora ao nome do arquivo
        String fileName = "evidencias/evidencia_" + dataHora + ".png";
        FileUtils.copyFile(scrFile, new File(fileName));
    }
}