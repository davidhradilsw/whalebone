package ui.playwright;

import com.microsoft.playwright.Page;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UitapSampleAppTests extends PlaywrightBase {

    private static final String SAMPLE_APP = BASE + "/sampleapp";

    private String loginStatus(Page page) {
        return page.locator("#loginstatus").innerText();
    }

    private void fillCredentials(Page page, String username, String password) {
        page.locator("input[name='UserName']").fill(username);
        page.locator("input[name='Password']").fill(password);
    }

    @Test
    public void sampleApp_loginSuccess_andLogout() {
        Page page = context.newPage();
        page.navigate(BASE);

        clickLink(page, "Sample App");

        fillCredentials(page, "david", "pwd");
        clickButton(page, "Log In");

        assertThat(loginStatus(page)).contains("Welcome, david");

        clickButton(page, "Log Out");
        assertThat(loginStatus(page)).contains("User logged out");
    }

    @Test
    public void sampleApp_loginInvalidPassword() {
        Page page = context.newPage();
        page.navigate(SAMPLE_APP);

        fillCredentials(page, "david", "wrong");
        clickButton(page, "Log In");

        assertThat(loginStatus(page)).contains("Invalid username/password");
    }

    @Test
    public void sampleApp_loginEmptyUsername_shouldFail() {
        Page page = context.newPage();
        page.navigate(SAMPLE_APP);

        fillCredentials(page, "", "pwd");
        clickButton(page, "Log In");

        assertThat(loginStatus(page)).contains("Invalid");
    }
}
