/*
 * Open Source Software published under the GNU Licence, Version 2.0.
 */

package io.github.liquec.analysis.calculation.ranges.eurocode.fines15;

import io.github.liquec.analysis.calculation.Evaluation;
import io.github.liquec.analysis.calculation.Polynomial;
import io.github.liquec.analysis.calculation.Range;

public enum EurocodeFines15 implements Evaluation {
    LESS_OR_EQUAL(LessOrEqual.class, Polynomial.EUROCODE_F15_LESS_OR_EQUAL),
    BETWEEN_FIRST_SECTION(BetweenFirstSection.class, Polynomial.EUROCODE_F15_BETWEEN_FIST_SECTION),
    BETWEEN_SECOND_SECTION(BetweenSecondSection.class, Polynomial.EUROCODE_F15_BETWEEN_SECOND_SECTION),
    GREATER_OR_EQUAL(GreaterOrEqual.class, Polynomial.EUROCODE_F15_GREATER_OR_EQUAL);

    private Class<? extends Range> evaluation;
    private Polynomial polynomial;

    EurocodeFines15(final Class<? extends Range> evaluation, final Polynomial polynomial) {
        this.evaluation = evaluation;
        this.polynomial = polynomial;
    }

    public Class<? extends Range> getEvaluationClass() {
        return evaluation;
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }
}
