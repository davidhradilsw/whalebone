package ui.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UitapLoadDelayTests extends PlaywrightBase {

    private static final String BASE = "http://uitestingplayground.com";
    private static final int TIMEOUT_MS = 15000;

    // --- small helpers (readability) ---
    private Locator delayedButton(Page page) {
        // there is "Load Delay" button on after delay page
        return page.locator("button:has-text('Button Appearing After Delay')");
    }

    @Test
    public void loadDelay_shouldLoadUnder15Seconds() {
        Page page = context.newPage();
        long startMs = System.currentTimeMillis();

        page.navigate(BASE);
        clickLink(page, "Load Delay");

        delayedButton(page).waitFor(new Locator.WaitForOptions().setTimeout(TIMEOUT_MS));

        long elapsedMs = System.currentTimeMillis() - startMs;
        assertThat(elapsedMs)
                .as("Load Delay page should reveal the button within %s ms, took %s ms", TIMEOUT_MS, elapsedMs)
                .isLessThan(TIMEOUT_MS);
    }
}
