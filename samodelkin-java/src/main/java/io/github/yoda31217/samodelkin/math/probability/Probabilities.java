package io.github.yoda31217.samodelkin.math.probability;

import static com.google.common.base.Preconditions.checkArgument;
import static io.github.yoda31217.samodelkin.math.error.DoubleWithError.newDoubleWithError;
import static java.lang.Double.isFinite;
import static java.lang.Double.isNaN;
import static org.apache.commons.math3.stat.interval.IntervalUtils.getNormalApproximationInterval;

import io.github.yoda31217.samodelkin.math.error.DoubleWithError;
import org.apache.commons.math3.stat.interval.ConfidenceInterval;

public class Probabilities {

  public static DoubleWithError calculateProbabilityWithError(
    int trialsCount, int successesCount, double confidenceLevel
  ) {
    double error = calculateError(trialsCount, successesCount, confidenceLevel);
    double probability = calculateProbability(trialsCount, successesCount);
    return newDoubleWithError(probability, error);
  }

  private static double calculateError(int trialsCount, int successesCount, double confidenceLevel) {
    checkArgument(trialsCount > 0,
      "Failed to calculate probability error. Trials count %s should be positive integer.",
      trialsCount);

    checkArgument(successesCount > 0 && successesCount < trialsCount,
      "Failed to calculate probability error. Successes count %s should be positive integer less than trials.",
      successesCount);

    checkArgument(isFinite(confidenceLevel) && !isNaN(confidenceLevel) && confidenceLevel > 0 && confidenceLevel < 1,
      "Failed to calculate probability error. Confidence level %s should be (0;1) double.",
      confidenceLevel);

    ConfidenceInterval confidenceInterval =
      getNormalApproximationInterval(trialsCount, successesCount, confidenceLevel);
    return (confidenceInterval.getUpperBound() - confidenceInterval.getLowerBound()) / 2;
  }

  private static double calculateProbability(int trialsCount, int successesCount) {
    return 1.0 * successesCount / trialsCount;
  }
}
