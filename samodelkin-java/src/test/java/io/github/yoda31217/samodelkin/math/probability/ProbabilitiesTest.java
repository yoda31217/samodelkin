package io.github.yoda31217.samodelkin.math.probability;

import static io.github.yoda31217.samodelkin.math.probability.Probabilities.calculateProbabilityWithError;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import io.github.yoda31217.samodelkin.math.error.DoubleWithError;
import org.junit.jupiter.api.Test;

class ProbabilitiesTest {

  // https://www.calculator.net/sample-size-calculator.html?type=2&cl2=95&ss2=100&pc2=10&ps2=&x=83&y=24#findci
  @Test
  void calculateProbabilityWithError_onCorrectValues_returnCorrectValues() {
    DoubleWithError probability = calculateProbabilityWithError(100, 10, 0.95);

    assertThat(probability.getValue()).isCloseTo(0.1, offset(0.0000001));
    assertThat(probability.getError()).isCloseTo(0.058798919, offset(0.0000001));
  }
}