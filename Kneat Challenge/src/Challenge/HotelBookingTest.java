package Challenge;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.text.SimpleDateFormat;

import org.junit.Assert;

public class HotelBookingTest {
	private WebDriver driver;

	private static final String WEBSITE_URL = "https://www.booking.com/";

	private static final String SEARCH_BUTTON_CLASSNAME = "sb-searchbox__button";

	private static final String LOCATIION_INPUT_ID = "ss";

	private static final String HOTEL_LOCATIION = "Limrick";

	private static final String DATE_CLASSNAME = "xp__dates";

	private static final String NEXT_PAGE_BUTTON_CLASSNAME = "bui-calendar__control--next";

	private static final String FORMATTED_STRING_PATTERN = "yyyy-MM-dd";

	private static final String CHROME_DRIVER = "webdriver.chrome.driver";

	private static final String CHROME_DRIVER_PATH = "C:\\chromedriver_win32 (1)/chromedriver.exe";
	
	private static final String EXPAND_FILTER_OPTIONS = "collapsed_partly_link";
	
	private static final String SAUNA_CSS_SELECTOR = "a[data-value=\"54\"]";
	
	private static final String FIVE_STAR_CSS_SELECTOR = "a[data-value=\"5\"]";
	
	private static final String SPA_STRING = "Spa";
	
	private static final String FIVE_STAR_STRING = "5 stars";
		
	private static final String SELECTED_BUTTON_CSS_SELECTOR = "active";
	
	private static final String HOTEL_CLASSNAME = "sr-hotel__name";
	
	private static final String DATE_CSS_SELECTOR_BEGINNING = "td[data-date=\"";
	
	private static final String DATE_CSS_SELECTOR_END = "\"]";
	
	private static final String SAUNA_TEST_PASS = " has a sauna listed";
	
	private static final String SAUNA_TEST_FAIL = " does not have a sauna listed";
	
	private static final String FIVE_STAR_TEST_PASS = " has a five star rating";
	
	private static final String FIVE_STAR_TEST_FAIL = " does not have a five star rating";
	
	private static final String TEST_PASSED = "Tests passed - ";
	
	private static final int SLEEP_TIMER = 4000;
	
	private static final String TESTS_ALL_PASSED = "All tests passed successfully";

	private static final String TEST_HOTEL_NAME_1 = "Limerick City Hotel";

	private static final String TEST_HOTEL_NAME_2 = "George Limerick Hotel";
	
	private static final String TEST_HOTEL_NAME_3 = "The Savoy Hotel";
	
	private static final String TEST_HOTEL_NAME_4 = "Clayton Limerick Hotel";

	public void launchBrowser() {
		System.setProperty(CHROME_DRIVER, CHROME_DRIVER_PATH);
		driver = new ChromeDriver();
		driver.get(WEBSITE_URL);
	}

	public void inputLocation() {
		driver.findElement(By.id(LOCATIION_INPUT_ID)).click();
		driver.findElement(By.id(LOCATIION_INPUT_ID)).sendKeys(HOTEL_LOCATIION);
	}

	public void inputDates() {
		driver.findElement(By.className(DATE_CLASSNAME)).click();
		for (int i = 0; i < 2; i++) {
			driver.findElement(By.className(NEXT_PAGE_BUTTON_CLASSNAME)).click();
		}

		String pattern = FORMATTED_STRING_PATTERN;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, +3);
		String firstDate = simpleDateFormat.format(cal.getTime());
		cal.add(Calendar.DATE, +1);
		String secondDate = simpleDateFormat.format(cal.getTime());

		driver.findElement(By.cssSelector(DATE_CSS_SELECTOR_BEGINNING + firstDate.toString() + DATE_CSS_SELECTOR_END)).click();
		driver.findElement(By.cssSelector(DATE_CSS_SELECTOR_BEGINNING + secondDate.toString() + DATE_CSS_SELECTOR_END)).click();
	}

	public void search() {
		driver.findElement(By.className(SEARCH_BUTTON_CLASSNAME)).click();
	}

	public boolean hasSaunaListed(String hotelName) throws InterruptedException {
		clickSaunaButton();
		boolean result = isHotelNameOnScreen(hotelName);
		deselectCheckBox();
		return result;
	}
	
	public void clickSaunaButton() throws InterruptedException {	
		if (driver.findElement(By.className(EXPAND_FILTER_OPTIONS)).isEnabled()) {
			driver.findElement(By.className(EXPAND_FILTER_OPTIONS)).click();
		}

		final List<WebElement> saunaSelectors = driver.findElements(By.cssSelector(SAUNA_CSS_SELECTOR));
		for (WebElement selector : saunaSelectors) {
			if (selector.getText().contains(SPA_STRING)){
				Actions actions = new Actions(driver);
				actions.moveToElement(selector);
				selector.click();
				break;
			}
		}
		Thread.sleep(SLEEP_TIMER);
	}


	public boolean hotelHasFiveStarRating(String hotelName) throws InterruptedException {
		clickFiveStarButton();
		boolean result = isHotelNameOnScreen(hotelName);
		deselectCheckBox();
		return result;
	}
	
	public void clickFiveStarButton() throws InterruptedException {
		final List<WebElement> fiveStarSelectors = driver.findElements(By.cssSelector(FIVE_STAR_CSS_SELECTOR));
		for (WebElement selector : fiveStarSelectors) {
			if (selector.getText().contains(FIVE_STAR_STRING)){
				Actions actions = new Actions(driver);
				actions.moveToElement(selector);
				selector.click();
				break;
			}
		}
		Thread.sleep(SLEEP_TIMER);
	}
	
	public boolean isHotelNameOnScreen(String hotelName) throws InterruptedException {
		final List<WebElement> listOfHotels = driver.findElements(By.className(HOTEL_CLASSNAME));				
		for (WebElement element : listOfHotels) {
			if (element.getText().equals(hotelName)) {
				return true;
			}
		}
		return false;
	}
	
	public void deselectCheckBox() throws InterruptedException {
		driver.findElement(By.className(SELECTED_BUTTON_CSS_SELECTOR)).click();
		Thread.sleep(SLEEP_TIMER);
	}
	
	public void manageCookiePreferences() throws InterruptedException {
		Thread.sleep(SLEEP_TIMER/4);
		driver.findElement(By.cssSelector("button[data-gdpr-consent=\"accept\"]")).click();
	}

	public void closeBrowser() {
		driver.quit();
	}

	public static void main(String[] args) throws InterruptedException {
		HotelBookingTest obj = new HotelBookingTest();
		obj.launchBrowser();
		obj.inputLocation();
		obj.inputDates();
		obj.manageCookiePreferences();
		obj.search();

		Assert.assertFalse(obj.hasSaunaListed(TEST_HOTEL_NAME_1));
		System.out.println(TEST_PASSED + TEST_HOTEL_NAME_1 + SAUNA_TEST_FAIL);
		
		Assert.assertFalse(obj.hasSaunaListed(TEST_HOTEL_NAME_2));
		System.out.println(TEST_PASSED + TEST_HOTEL_NAME_2 + SAUNA_TEST_FAIL);
	
		Assert.assertTrue(obj.hasSaunaListed(TEST_HOTEL_NAME_3));
		System.out.println(TEST_PASSED + TEST_HOTEL_NAME_3 + SAUNA_TEST_PASS);

		Assert.assertFalse(obj.hasSaunaListed(TEST_HOTEL_NAME_4));
		System.out.println(TEST_PASSED + TEST_HOTEL_NAME_4 + SAUNA_TEST_FAIL);
		
		Assert.assertFalse(obj.hotelHasFiveStarRating(TEST_HOTEL_NAME_1));
		System.out.println(TEST_PASSED + TEST_HOTEL_NAME_1 + FIVE_STAR_TEST_FAIL);

		Assert.assertFalse(obj.hotelHasFiveStarRating(TEST_HOTEL_NAME_2));
		System.out.println(TEST_PASSED + TEST_HOTEL_NAME_2 + FIVE_STAR_TEST_FAIL);

		Assert.assertTrue(obj.hotelHasFiveStarRating(TEST_HOTEL_NAME_3));
		System.out.println(TEST_PASSED + TEST_HOTEL_NAME_3 + FIVE_STAR_TEST_PASS);

		Assert.assertFalse(obj.hotelHasFiveStarRating(TEST_HOTEL_NAME_4));
		System.out.println(TEST_PASSED + TEST_HOTEL_NAME_4 + FIVE_STAR_TEST_FAIL);

		System.out.println(TESTS_ALL_PASSED);

		obj.closeBrowser();
	}
}