package io.github.yoda31217.samodelkin.math.probability;

import static io.github.yoda31217.samodelkin.math.probability.Probabilities.calculateProbabilityWithError;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import io.github.yoda31217.samodelkin.math.error.DoubleWithError;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProbabilitiesTest {

  // https://www.calculator.net/sample-size-calculator.html?type=2&cl2=95&ss2=100&pc2=10&ps2=&x=83&y=24#findci
  @Test
  void calculateProbabilityWithError_onCorrectValues_returnCorrectValues() {
    DoubleWithError probability = calculateProbabilityWithError(100, 10, 0.95);

    assertThat(probability.getValue()).isCloseTo(0.1, offset(0.0000001));
    assertThat(probability.getError()).isCloseTo(0.058798919, offset(0.0000001));
  }

  @ParameterizedTest
  @MethodSource("calculateProbabilityWithError_onIncorrectValues_throwEx_provider")
  void calculateProbabilityWithError_onIncorrectValues_throwEx(
    int trialsCount, int successesCount, double confidenceLevel, String badNumberStr
  ) {
    assertThatThrownBy(() -> calculateProbabilityWithError(trialsCount, successesCount, confidenceLevel))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("Failed to calculate probability error")
      .hasMessageContaining(badNumberStr);
  }

  static Stream<Arguments> calculateProbabilityWithError_onIncorrectValues_throwEx_provider() {
    return Stream.of(
      arguments(100, 0, 0.95, "0"),
      arguments(100, -1, 0.95, "-1"),
      arguments(0, 1, 0.95, "0"),
      arguments(-100, 1, 0.95, "-100"),
      arguments(100, 100, 0.95, "100"),
      arguments(100, 1, -0.95, "-0.95"),
      arguments(100, 1, 0.0, "0.0"),
      arguments(100, 1, 1.0, "1.0"),
      arguments(100, 1, NaN, "NaN"),
      arguments(100, 1, POSITIVE_INFINITY, "Infinity"),
      arguments(100, 1, NEGATIVE_INFINITY, "-Infinity")
    );
  }
}