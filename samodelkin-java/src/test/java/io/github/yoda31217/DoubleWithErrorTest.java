package io.github.yoda31217;

import static io.github.yoda31217.DoubleWithError.newDoubleWithError;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import org.junit.jupiter.api.Test;

class DoubleWithErrorTest {

  @Test
  void newDoubleWithError_onCorrectValue_correctValuesReturned() {
    DoubleWithError doubleWithError = newDoubleWithError(12.34, 0.56);

    assertThat(doubleWithError.getValue()).isCloseTo(12.34, offset(0.0000001));
    assertThat(doubleWithError.getError()).isCloseTo(0.56, offset(0.0000001));
  }
}