package io.github.yoda31217.samodelkin.math.error;

import static io.github.yoda31217.samodelkin.math.error.DoubleWithError.newDoubleWithError;
import static io.github.yoda31217.samodelkin.math.error.DoubleWithError.newDoubleWithRelativeError;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DoubleWithErrorTest {

  @ParameterizedTest
  @MethodSource("newDoubleWithError_onCorrectArguments_returnCorrectValueAndErrors_provider")
  void newDoubleWithError_onCorrectArguments_returnCorrectValueAndErrors(
    double value, double error, double relativeError
  ) {
    DoubleWithError doubleWithError = newDoubleWithError(value, error);

    assertThat(doubleWithError.getValue()).isCloseTo(value, offset(0.000001));
    assertThat(doubleWithError.getError()).isCloseTo(error, offset(0.0000001));
    assertThat(doubleWithError.getRelativeError()).isCloseTo(relativeError, offset(0.0000001));
  }

  static Stream<Arguments> newDoubleWithError_onCorrectArguments_returnCorrectValueAndErrors_provider() {
    return Stream.of(
      arguments(10.0, 1.0, 0.1),
      arguments(-10.0, 1.0, 0.1),
      arguments(1, 0, 0)
    );
  }

  @ParameterizedTest
  @MethodSource("newDoubleWithRelativeError_onCorrectArguments_returnCorrectValueAndErrors_provider")
  void newDoubleWithRelativeError_onCorrectArguments_returnCorrectValueAndErrors(
    double value, double error, double relativeError
  ) {
    DoubleWithError doubleWithError = newDoubleWithRelativeError(value, relativeError);

    assertThat(doubleWithError.getValue()).isCloseTo(value, offset(0.000001));
    assertThat(doubleWithError.getError()).isCloseTo(error, offset(0.0000001));
    assertThat(doubleWithError.getRelativeError()).isCloseTo(relativeError, offset(0.0000001));
  }

  static Stream<Arguments> newDoubleWithRelativeError_onCorrectArguments_returnCorrectValueAndErrors_provider() {
    return Stream.of(
      arguments(10.0, 1.0, 0.1),
      arguments(-10.0, 1.0, 0.1),
      arguments(1, 0, 0)
    );
  }

  @ParameterizedTest
  @MethodSource("newDoubleWithError_onIncorrectArgument_throwEx_provider")
  void newDoubleWithError_onIncorrectArgument_throwEx(double value, double error, String badNumberStr) {
    assertThatThrownBy(() -> newDoubleWithError(value, error))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining(badNumberStr);
  }

  static Stream<Arguments> newDoubleWithError_onIncorrectArgument_throwEx_provider() {
    return Stream.of(
      arguments(10.0, -1.0, "-1.0"),
      arguments(10.0, NEGATIVE_INFINITY, "-Infinity"),
      arguments(10.0, POSITIVE_INFINITY, "Infinity"),
      arguments(10.0, NaN, "NaN"),
      arguments(0.0, 1.0, "0.0"),
      arguments(NEGATIVE_INFINITY, 1.0, "-Infinity"),
      arguments(POSITIVE_INFINITY, 1.0, "Infinity"),
      arguments(NaN, 1.0, "NaN")
    );
  }

  @ParameterizedTest
  @MethodSource("newDoubleWithRelativeError_onIncorrectArgument_throwEx_provider")
  void newDoubleWithRelativeError_onIncorrectArgument_throwEx(double value, double relativeError, String badNumberStr) {
    assertThatThrownBy(() -> newDoubleWithRelativeError(value, relativeError))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining(badNumberStr);
  }

  static Stream<Arguments> newDoubleWithRelativeError_onIncorrectArgument_throwEx_provider() {
    return Stream.of(
      arguments(10.0, -0.1, "-1.0"),
      arguments(10.0, NEGATIVE_INFINITY, "-Infinity"),
      arguments(10.0, POSITIVE_INFINITY, "Infinity"),
      arguments(10.0, NaN, "NaN"),
      arguments(0.0, 0.1, "0.0"),
      arguments(NEGATIVE_INFINITY, 0.1, "-Infinity"),
      arguments(POSITIVE_INFINITY, 0.1, "Infinity"),
      arguments(NaN, 0.1, "NaN")
    );
  }

  @Test
  void format_withValueFormatter_returnCorrectStr() {
    DoubleWithError doubleWithError = newDoubleWithError(0.25, 0.05);
    assertThat(doubleWithError.format(value -> format(ENGLISH, "%.1f", value)))
      .isEqualTo("0.3±0.1(20.00%)");
  }

  @Test
  void add_onOther_returnCorrectResult() {
    DoubleWithError doubleWithError = newDoubleWithError(1, 0.1).add(newDoubleWithError(2, 0.2));

    assertThat(doubleWithError.getValue()).isCloseTo(3, offset(0.0000001));
    assertThat(doubleWithError.getError()).isCloseTo(0.3, offset(0.0000001));
  }

  @Test
  void sub_onOther_returnCorrectResult() {
    DoubleWithError doubleWithError = newDoubleWithError(1, 0.1).sub(newDoubleWithError(2, 0.2));

    assertThat(doubleWithError.getValue()).isCloseTo(-1, offset(0.0000001));
    assertThat(doubleWithError.getError()).isCloseTo(0.3, offset(0.0000001));
  }

  @Test
  void mul_onOther_returnCorrectResult() {
    DoubleWithError doubleWithError = newDoubleWithRelativeError(1, 0.1).mul(newDoubleWithRelativeError(2, 0.2));

    assertThat(doubleWithError.getValue()).isCloseTo(2, offset(0.0000001));
    assertThat(doubleWithError.getRelativeError()).isCloseTo(0.3, offset(0.0000001));
  }

  @Test
  void div_onOther_returnCorrectResult() {
    DoubleWithError doubleWithError = newDoubleWithRelativeError(1, 0.1).div(newDoubleWithRelativeError(2, 0.2));

    assertThat(doubleWithError.getValue()).isCloseTo(0.5, offset(0.0000001));
    assertThat(doubleWithError.getRelativeError()).isCloseTo(0.3, offset(0.0000001));
  }
}