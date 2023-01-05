package io.github.yoda31217.samodelkin.math.error;

import static io.github.yoda31217.samodelkin.math.error.DoubleWithError.newDoubleWithError;
import static io.github.yoda31217.samodelkin.math.error.DoubleWithError.newDoubleWithRelativeError;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

import org.junit.jupiter.api.Test;

class DoubleWithErrorTest {

  @Test
  void newDoubleWithError_onPositiveValueAndError_correctFieldsReturned() {
    DoubleWithError doubleWithError = newDoubleWithError(12.34, 0.56);

    assertThat(doubleWithError.getValue()).isCloseTo(12.34, offset(0.0000001));
    assertThat(doubleWithError.getError()).isCloseTo(0.56, offset(0.0000001));
  }

  @Test
  void newDoubleWithError_onNegativeError_throwEx() {
    assertThatThrownBy(() -> newDoubleWithError(12.34, -0.56))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("-0.56");
  }

  @Test
  void newDoubleWithError_onPositiveValueAndError_correctRelativeErrorReturned() {
    DoubleWithError doubleWithError = newDoubleWithError(10.0, 1);
    assertThat(doubleWithError.getRelativeError()).isCloseTo(0.1, offset(0.0000001));
  }

  @Test
  void newDoubleWithError_onNegativeValue_correctPositiveRelativeErrorReturned() {
    DoubleWithError doubleWithError = newDoubleWithError(-10.0, 1);
    assertThat(doubleWithError.getRelativeError()).isCloseTo(0.1, offset(0.0000001));
  }

  @Test
  void newDoubleWithRelativeError_onPositiveValueAndRelativeError_correctFieldsReturned() {
    DoubleWithError doubleWithError = newDoubleWithRelativeError(12.34, 0.1);

    assertThat(doubleWithError.getValue()).isCloseTo(12.34, offset(0.0000001));
    assertThat(doubleWithError.getError()).isCloseTo(1.234, offset(0.0000001));
  }

  @Test
  void newDoubleWithRelativeError_onNegativeValue_correctErrorReturned() {
    DoubleWithError doubleWithError = newDoubleWithRelativeError(-12.34, 0.1);
    assertThat(doubleWithError.getError()).isCloseTo(1.234, offset(0.0000001));
  }

  @Test
  void newDoubleWithRelativeError_onNegativeRelativeError_throwEx() {
    assertThatThrownBy(() -> newDoubleWithRelativeError(12.34, -0.1))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("-1.234");
  }

  @Test
  void format_withValueFormatter_returnCorrectStr() {
    DoubleWithError doubleWithError = newDoubleWithError(0.25, 0.05);
    assertThat(doubleWithError.format(value -> format(ENGLISH, "%.2f", value)))
      .isEqualTo("0.25±0.05(20.00%)");
  }

  @Test
  void newDoubleWithError_on0Error_return0RelativeError() {
    DoubleWithError doubleWithError = newDoubleWithError(1, 0);
    assertThat(doubleWithError.getRelativeError()).isCloseTo(0, offset(0.0000001));
  }

  @Test
  void newDoubleWithError_on0ValueAndError_return0RelativeError() {
    DoubleWithError doubleWithError = newDoubleWithError(0, 0);
    assertThat(doubleWithError.getRelativeError()).isCloseTo(0, offset(0.0000001));
  }

  @Test
  void newDoubleWithError_on0ValueAnd1Error_returnInfiniteRelativeError() {
    DoubleWithError doubleWithError = newDoubleWithError(0, 1);
    assertThat(doubleWithError.getRelativeError()).isInfinite();
  }
}