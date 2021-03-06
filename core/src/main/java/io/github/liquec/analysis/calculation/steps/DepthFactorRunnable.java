package io.github.liquec.analysis.calculation.steps;

import io.github.liquec.analysis.calculation.Mode;
import io.github.liquec.analysis.calculation.Runnable;
import io.github.liquec.analysis.model.SptCalculationResult;
import io.github.liquec.analysis.session.SessionState;

public class DepthFactorRunnable extends Runnable {

    public DepthFactorRunnable(final Mode mode, final String description) {
        super(mode, description);
    }

    public void execute(final SessionState sessionState, final SptCalculationResult sptCalculationResult) {
        this.logStart();

        final Double depthFactor = 1.0 - (0.015 * sptCalculationResult.getDepth());

        LOG.debug(":::::: Depth factor (rd): " + depthFactor);

        sptCalculationResult.setDepthFactor(depthFactor);

        this.logEnd();
    }

}
