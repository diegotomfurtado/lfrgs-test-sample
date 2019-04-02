package com.liferay.gs.test.functional.selenium.rule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Andrew Betts
 */
public class SimpleRuleChain implements TestRule {

	public SimpleRuleChain(TestRule ... rules) {
		_rules = rules;
	}

	@Override
	public Statement apply(Statement base, Description description) {
		for (TestRule rule : _rules) {
			base = rule.apply(base, description);
		}

		return base;
	}

	private TestRule[] _rules;

}