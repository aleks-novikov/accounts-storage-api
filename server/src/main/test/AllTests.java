import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApiTests.class,
        ProgramExitTest.class
})
public class AllTests {
}