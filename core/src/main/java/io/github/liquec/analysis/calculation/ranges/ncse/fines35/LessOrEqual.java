/*
 * Open Source Software published under the GNU Licence, Version 2.0.
 */

package io.github.liquec.analysis.calculation.ranges.ncse.fines35;

import io.github.liquec.analysis.calculation.Range;

public class LessOrEqual implements Range {

    @Override
    public boolean contains(final Double sptCorrected) {
        final boolean contains = sptCorrected <= 2.82;
        LOG.debug(":::::: Range less or equals: " + contains);
        return contains;
    }

}
