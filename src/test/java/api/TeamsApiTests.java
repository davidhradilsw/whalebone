package api;

import api.model.Team;
import api.model.TeamsResponse;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class TeamsApiTests {

    private static final String TEAMS_URL = "https://qa-assignment.dev1.whalebone.io/api/teams";

    private TeamsResponse getTeams() {
        // ignore certificate check
        RestAssured.useRelaxedHTTPSValidation();

        return given()
                .when().get(TEAMS_URL)
                .then().statusCode(200)
                .extract().as(TeamsResponse.class);
    }

    @Test
    public void verifyExpectedCountOfTeams() {
        TeamsResponse resp = getTeams();
        assertThat(resp.teams).hasSize(32);
    }

    @Test
    public void verifyOldestTeamIsMontrealCanadiens() {
        TeamsResponse resp = getTeams();

        Team oldest = resp.teams.stream()
                .min(Comparator.comparingInt(t -> t.founded))
                .orElseThrow();

        assertThat(oldest.name).isEqualTo("Montreal Canadiens");
        //assertThat(oldest.founded).isEqualTo(1909);
    }

    @Test
    public void verifyCityWithMoreThanOneTeam_andTheirNames() {
        TeamsResponse resp = getTeams();

        Map<String, List<Team>> byCity = resp.teams.stream()
                .collect(Collectors.groupingBy(t -> t.location));

        // find all cities where teams > 1
        Map<String, List<Team>> multi = byCity.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // we expect New York => Islanders + Rangers
        assertThat(multi).containsKey("New York");

        List<String> names = multi.get("New York").stream().map(t -> t.name).sorted().toList();
        assertThat(names).containsExactly(
                "New York Islanders",
                "New York Rangers"
        );
    }

    @Test
    public void verifyMetropolitanDivisionTeams() {
        TeamsResponse resp = getTeams();

        List<String> metro = resp.teams.stream()
                .filter(t -> t.division != null && "Metropolitan".equals(t.division.name))
                .map(t -> t.name)
                .toList();

        assertThat(metro).hasSize(8);

        List<String> expected = List.of(
                "Carolina Hurricanes",
                "Columbus Blue Jackets",
                "New Jersey Devils",
                "New York Islanders",
                "New York Rangers",
                "Philadelphia Flyers",
                "Pittsburgh Penguins",
                "Washington Capitals"
        );

        assertThat(metro).containsExactlyElementsOf(expected);
    }
}
