import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Random;

public class Shopping {
    WebDriver driver;

    @BeforeTest
    @Parameters("browser")
    public void setup(String browser) throws Exception {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();

        } else if (browser.equalsIgnoreCase("Edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
            driver.manage().window().maximize();
        } else {
            throw new Exception("Browser is not correct");
        }
    }

    @Test
    public void CustomerExperience() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.location = 'http://automationpractice.com/index.php'");


            //Move to 'Women' and select 'T-shirts'
            Actions action = new Actions(driver);
            WebElement women = driver.findElement(By.xpath("//a[@title='Women']"));
            action.moveToElement(women).perform();
            driver.findElement(By.xpath("//ul[@class='submenu-container clearfix first-in-line-xs']//a[text()='T-shirts' ]")).click();


            //Click on the button 'Quick view', which is visible on mouse hover on the returned card image
            new WebDriverWait(driver, 3).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='Faded Short Sleeve T-shirts']")));
            WebElement img = driver.findElement(By.xpath("//img[@alt='Faded Short Sleeve T-shirts']"));
            action.moveToElement(img).perform();
            driver.findElement(By.xpath("//span[text()='Quick view']")).click();


            //In the opened alert window move to the all small images and check that big image changes
            new WebDriverWait(driver, 10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));

            WebElement images = driver.findElement(By.id("thumbs_list_frame"));
            List<WebElement> smallImages = images.findElements(By.tagName("li"));

            WebElement bigImg = driver.findElement(By.id("bigpic"));
            String bigImgSrc = bigImg.getAttribute("src");
            int smallImgQuantity = smallImages.size();
            int a = 1;
            for (WebElement i : smallImages) {
                action.moveToElement(i).perform();
                String bigImgSrcChange = bigImg.getAttribute("src");
                if (!bigImgSrc.equals(bigImgSrcChange)) {
                    a++;
                }
            }
            if (smallImgQuantity == a) {
                System.out.println("Big Image Changes Every Time");
            } else {
                System.out.println("Big Image Not Changes Every time");
            }


            //Add two M size to the cart and click on 'Continue Shopping' button
            WebElement quantityInput = driver.findElement(By.id("quantity_wanted"));
            quantityInput.clear();
            quantityInput.sendKeys("2");

            Select size = new Select(driver.findElement(By.id("group_1")));
            size.selectByVisibleText("M");
            driver.findElement(By.cssSelector("p[id='add_to_cart']")).click();


            new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.id("layer_cart")));
            driver.findElement(By.cssSelector("span[title='Continue shopping']")).click();


            //Move to the main page and select 'Casual Dresses' from the 'Dresses'
            driver.findElement(By.xpath("//a[@title='My Store']")).click();
            WebElement dresses = driver.findElement(By.cssSelector("ul[class*='sf-menu'] > li:nth-child(2)"));
            action.moveToElement(dresses).perform();
            driver.findElement(By.cssSelector("ul[class*='sf-menu'] > li:nth-child(2) ul li a[title*='Casual']")).click();


            //Move to the returned element and Add to cart and click on 'Continue Shopping' button
            WebElement product = driver.findElement(By.xpath("//div[@class='product-container']"));
            js.executeScript("arguments[0].scrollIntoView();", product);
            action.moveToElement(product).perform();

            driver.findElement(By.xpath("//a[@title='Add to cart']")).click();
            WebElement continueShopping1 = driver.findElement(By.cssSelector("span[title='Continue shopping']"));
            new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(continueShopping1));
            continueShopping1.click();


            //Move to the Cart and Check out
            WebElement cart = driver.findElement(By.cssSelector("a[title='View my shopping cart']"));
            action.moveToElement(cart).perform();

            WebElement addToCart = driver.findElement(By.id("button_order_cart"));
            new WebDriverWait(driver, 3).until(ExpectedConditions.elementToBeClickable(addToCart));
            addToCart.click();


            //Check by description that all items are added in cart
            List<WebElement> Elements = driver.findElements(By.xpath("//tr//p[@class='product-name']"));
            System.out.println("\n Items in Cart are:\n ");
            for (WebElement i : Elements) {
                System.out.println(" " + i.getText());
            }
            System.out.println(" ");


            //Click on 'Proceed to checkout' and sign up for a new account
            driver.findElement(By.xpath("//a[@class='button btn btn-default standard-checkout button-medium' and @title='Proceed to checkout']")).click();

            Random rand = new Random();
            StringBuilder email = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                char randomizedCharacter = (char) (rand.nextInt(26) + 'a');
                email.append(randomizedCharacter);
            }

            driver.findElement(By.id("email_create")).sendKeys(email.toString() + rand.nextInt(2000) + "@gmail.com");
            driver.findElement(By.id("SubmitCreate")).click();


            new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("id_gender1")));
            WebElement gender = driver.findElement(By.id("id_gender1"));
            gender.click();
            driver.findElement(By.id("customer_firstname")).sendKeys("Davide");
            driver.findElement(By.id("customer_lastname")).sendKeys("Vardanidze");
            driver.findElement(By.id("passwd")).sendKeys("pass123");
            driver.findElement(By.id("days")).sendKeys("2");
            driver.findElement(By.id("months")).sendKeys("February");
            driver.findElement(By.id("years")).sendKeys("2000");
            driver.findElement(By.id("address1")).sendKeys("BadriShubladzis 3");
            driver.findElement(By.id("city")).sendKeys("Tbilisi");
            driver.findElement(By.id("id_state")).sendKeys("Georgia");
            driver.findElement(By.id("postcode")).sendKeys("11111");
            driver.findElement(By.id("id_country")).sendKeys("United States");
            driver.findElement(By.id("phone_mobile")).sendKeys("555555453");
            js.executeScript("document.getElementById('submitAccount').click()");

            //Click on 'Proceed to checkout' and leave Address data the default
            driver.findElement(By.xpath("//button[contains(@name,'processAddress')]")).click();


            //Click on 'Proceed to checkout' and try to click to 'Proceed to checkout' button in Shipping tab without accepting 'Terms of service'
            driver.findElement(By.xpath("//button[@name='processCarrier']")).click();


            //Catch error window and accept 'Terms of service'
            new WebDriverWait(driver, 3).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='Close']")));
            driver.findElement(By.xpath("//a[@title='Close']")).click();
            driver.findElement(By.id("cgv")).click();
            driver.findElement(By.xpath("//button[@name='processCarrier']")).click();

            //In 'Payment' Tab choose 'Pay by check'
            String totalPrice = js.executeScript("return document.getElementById('total_price').innerText;").toString();
            driver.findElement(By.className("cheque")).click();


            //Check that total amount is correct and click on 'Confirm my order'
            String checkAmount = js.executeScript("return document.getElementById('amount').innerText;").toString();
            if (totalPrice.equalsIgnoreCase(checkAmount)) {
                System.out.println("Total amount is correct");
            } else {
                System.out.println("Total amount is incorrect");
            }
            driver.findElement(By.xpath("//span[text()='I confirm my order']")).click();


            //Click on 'customer service department' link
            driver.findElement(By.xpath("//a[text()='customer service department.']")).click();


            //Choose heading and order reference, upload file, add message text and Send
            Select heading = new Select(driver.findElement(By.id("id_contact")));
            heading.selectByValue("2");

            Select reference = new Select(driver.findElement(By.xpath("//select[@name='id_order']")));
            reference.selectByValue("0");

            WebElement fileUpload = driver.findElement(By.id("fileUpload"));
            File file = new File("src/Image/CaptainMocha.png");
            try {
                fileUpload.sendKeys(file.getAbsolutePath());
            } catch (InvalidArgumentException e) {
                System.out.println("InvalidArgumentException !!");
            }


            driver.findElement(By.id("message")).sendKeys("Totally dissapointed with product");
            driver.findElement(By.id("submitMessage")).click();


        } catch (NoSuchElementException e) {
            System.out.println("Element not found !");
            throw e;
        } catch (TimeoutException e) {
            System.out.println("TimeOut. Try Again!");
            throw e;
        } catch (NoSuchWindowException e) {
            System.out.println("NoSuchWindow. Try Again!");
            throw e;
        }
        driver.close();
    }
}

/*

3 different JS executor commands
Line 41 (window.location)
Line 161 (click())
Line 178 (innerText)

3 different approaches to find element with relative XPath
Line 52 (Tag and Attribute)
Line 55 (Text())
Line 164 (Contains)

3 different approaches to find element with CSS selectors
Line 93(Tag and Attribute)
Line 98(Child Element)
Line 100(Contains)

3 different methods to get element using  the `By` class
By.xpath
By.cssSelector
By.id

 */