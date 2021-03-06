/*
 * Open Source Software published under the GNU Licence, Version 2.0.
 */

package io.github.liquec.analysis.calculation;

public interface Step {

    String getDescription();

    Class<? extends Runnable> getStepClass();

}
