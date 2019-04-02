package com.liferay.gs.test.functional.selenium.rule;

import com.liferay.gs.test.functional.selenium.BaseTest;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.Verifier;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

/**
 * @author Andrew Betts
 */
@RunWith(JUnit4.class)
public class SimpleRuleChainTest extends BaseTest {

	@Rule
	public SimpleRuleChain simpleRuleChain = new SimpleRuleChain(
		_temporaryFolder, _stopWatch, _verifier);

	@Test
	public void testSimpleRuleChain() throws Exception {
		File file = _temporaryFolder.newFile();

		Assert.assertNotNull(file);
	}

	private static Stopwatch _stopWatch = new Stopwatch() {

		/**
		 * Invoked when a test method finishes (whether passing or failing)
		 */
		protected void finished(long nanos, Description description) {
			_verifier.called = true;
		}

	};

	private static class RuleChainVerifier extends Verifier {

		private boolean called;

		protected void verify() throws Throwable {
			if (!called) {
				Assert.fail();
			}
		}

	}

	private static TemporaryFolder _temporaryFolder = new TemporaryFolder();
	private static RuleChainVerifier _verifier = new RuleChainVerifier();

}