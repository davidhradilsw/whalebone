package ui.playwright;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static com.microsoft.playwright.options.AriaRole.LINK;

public abstract class PlaywrightBase {
    protected static final String BASE = "http://uitestingplayground.com";
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;

    @BeforeClass
    public void setUp() {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1400, 900));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    // --- small helpers (readability) ---
    protected void clickLink(Page page, String name) {
        page.getByRole(LINK, new Page.GetByRoleOptions().setName(name)).click();
    }

    protected void clickButton(Page page, String name) {
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName(name)).click();
    }
}
