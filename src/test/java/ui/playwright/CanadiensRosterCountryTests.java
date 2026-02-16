package ui.playwright;

import api.model.Team;
import api.model.TeamsResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class CanadiensRosterCountryTests extends PlaywrightBase {

    private static final String TEAMS_URL = "https://qa-assignment.dev1.whalebone.io/api/teams";

    @Test
    public void verifyMoreCanadianPlayersThanUSAOnOldestTeamRoster() {
        RestAssured.useRelaxedHTTPSValidation(); // ignore certification validation
        TeamsResponse resp = given().get(TEAMS_URL).then().statusCode(200).extract().as(TeamsResponse.class);

        Team oldest = resp.teams.stream()
                .min(Comparator.comparingInt(t -> t.founded))
                .orElseThrow();

        // use roster URL
        URI base = URI.create(oldest.officialSiteUrl.endsWith("/") ? oldest.officialSiteUrl : oldest.officialSiteUrl + "/");
        String rosterUrl = base.resolve("roster").toString();

        Page page = context.newPage();
        page.navigate(rosterUrl);
        page.waitForLoadState();

        // first we need to wait for loading roster - during debugging there was issue, that roster was not loaded
        Locator rosterLocator = page.locator("table tbody tr");
        page.waitForCondition(() ->
                        rosterLocator.count() > 0,
                new Page.WaitForConditionOptions().setTimeout(15000)
        );
        List<String> rowTexts = rosterLocator.allTextContents();
        assertThat(rowTexts).isNotEmpty();

        int can = 0;
        int usa = 0;

        for (String t : rowTexts) {
            String upper = t.toUpperCase();

            if (upper.contains(" CAN")) can++;
            if (upper.contains(" USA")) usa++;
        }

        assertThat(can)
                .as("Expected more CAN players than USA on roster: " + rosterUrl)
                .isGreaterThan(usa);
    }
}
