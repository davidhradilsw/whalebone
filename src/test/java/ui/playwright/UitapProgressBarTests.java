package ui.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UitapProgressBarTests extends PlaywrightBase {

    private static final String URL = "http://uitestingplayground.com/progressbar";
    private static final int MAX_WAIT_MS = 20000;

    private Locator progressBar(Page page) {
        return page.locator("#progressBar");
    }

    private int progressValue(Page page) {
        String value = progressBar(page).getAttribute("aria-valuenow");
        return Integer.parseInt(value);
    }

    @Test
    public void progressBar_stopAround75Percent() {
        Page page = context.newPage();
        page.navigate(URL);

        clickButton(page, "Start");

        long deadline = System.currentTimeMillis() + MAX_WAIT_MS;

        while (System.currentTimeMillis() < deadline) {
            int value = progressValue(page);

            if (value >= 75) {
                clickButton(page, "Stop");
                break;
            }
            page.waitForTimeout(50); // save CPU time
        }

        int stoppedValue = progressValue(page);

        assertThat(stoppedValue)
                .as("Progress bar should be stopped around 75%")
                .isBetween(75, 80);
    }
}
