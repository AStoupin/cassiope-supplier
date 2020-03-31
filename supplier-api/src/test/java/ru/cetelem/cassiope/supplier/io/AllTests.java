package ru.cetelem.cassiope.supplier.io;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CflTest.class, F120Debug.class, F910Debug.class,
		F150Debug.class, F920Debug.class, F940Debug.class, F950Debug.class,
		ICflDebug.class })
public class AllTests {

}
