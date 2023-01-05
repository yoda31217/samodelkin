package io.github.yoda31217.samodelkin.math.probability;

import static io.github.yoda31217.samodelkin.math.error.DoubleWithError.newDoubleWithError;
import static org.apache.commons.math3.stat.interval.IntervalUtils.getNormalApproximationInterval;

import io.github.yoda31217.samodelkin.math.error.DoubleWithError;
import org.apache.commons.math3.stat.interval.ConfidenceInterval;

public class Probabilities {

  public static DoubleWithError calculateProbabilityWithError(
    int trialsCount, int successesCount, double confidenceLevel
  ) {
    double probability = calculateProbability(trialsCount, successesCount);
    double error = calculateError(trialsCount, successesCount, confidenceLevel);
    return newDoubleWithError(probability, error);
  }

  private static double calculateError(int trialsCount, int successesCount, double confidenceLevel) {
    ConfidenceInterval confidenceInterval =
      getNormalApproximationInterval(trialsCount, successesCount, confidenceLevel);
    return (confidenceInterval.getUpperBound() - confidenceInterval.getLowerBound()) / 2;
  }

  private static double calculateProbability(int trialsCount, int successesCount) {
    return 1.0 * successesCount / trialsCount;
  }
}
