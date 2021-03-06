/*
 * Open Source Software published under the GNU Licence, Version 2.0.
 */

package io.github.liquec.analysis.calculation.ranges.crr;

import io.github.liquec.analysis.calculation.Fines;
import io.github.liquec.analysis.calculation.Range;

public class GreaterOrEqual implements Range {

    @Override
    public boolean contains(final Double finesContent) {
        final boolean contains = finesContent >= Fines.BOUND_35.getBound();
        LOG.debug(":::::: Range greater or equals [" + Fines.BOUND_35.getBound() + "]: " + contains);
        return contains;
    }

}
