package ohtu.systemTests;

import org.junit.ClassRule;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = "pretty",
        features = "src/test/resources/ohtu",
        snippets = SnippetType.CAMELCASE
)
public class RunCukesTest {

    @ClassRule
    public static ServerRule server = new ServerRule(8080);
}
