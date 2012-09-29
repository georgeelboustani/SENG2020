package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	testAssertion.class,
	testStorage.class,
	testShelf.class,
	testDatabase.class
})

public class _TestSuite {

}
