package com.liferay.gs.test.functional.selenium.rule;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * @author Andrew Betts
 */
public abstract class AbstractTestWatcher extends TestWatcher {

	public AbstractTestWatcher(TestWatcherState... when) {
		for (TestWatcherState state : when) {
			_when[state.ordinal()] = true;
		}
	}

	protected void doFailed(Throwable e, Description description) {

	}

	protected void doFinished(Description description) {

	}

	protected void doStarting(Description description) {

	}

	protected void doSucceeded(Description description) {

	}

	/**
	 * Invoked when a test fails
	 */
	@Override
	protected void failed(Throwable e, Description description) {
		if (!_when[TestWatcherState.FAILED.ordinal()]) {
			return;
		}

		doFailed(e, description);
	}

	/**
	 * Invoked when a test method finishes (whether passing or failing)
	 */
	@Override
	protected void finished(Description description) {
		if (!_when[TestWatcherState.FINISHED.ordinal()]) {
			return;
		}

		doFinished(description);
	}

	/**
	 * Invoked when a test is about to start
	 */
	@Override
	protected void starting(Description description) {
		if (!_when[TestWatcherState.STARTING.ordinal()]) {
			return;
		}

		doStarting(description);
	}

	/**
	 * Invoked when a test succeeds
	 */
	@Override
	protected void succeeded(Description description) {
		if (!_when[TestWatcherState.SUCCEEDED.ordinal()]) {
			return;
		}

		doSucceeded(description);
	}

	private boolean[] _when = new boolean[TestWatcherState.values().length];

}