/*
 * Open Source Software published under the GNU Licence, Version 2.0.
 */

package io.github.liquec.analysis.calculation.ranges.eurocode.fines15;

import io.github.liquec.analysis.calculation.Range;

public class LessOrEqual implements Range {

    @Override
    public boolean contains(final Double sptCorrected) {
        final boolean contains = sptCorrected <= 5.13;
        LOG.debug(":::::: Range less or equals: " + contains);
        return contains;
    }

}
