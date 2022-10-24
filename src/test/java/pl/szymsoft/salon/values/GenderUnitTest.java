package pl.szymsoft.salon.values;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class GenderUnitTest {

    @ParameterizedTest(name = "{0}.toString() should return ''{1}''")
    @CsvSource(delimiter = '|', textBlock = """
            # GENDER    | STRING
            #-------------------
              MALE      | Male
              FEMALE    | Female
            """)
    void Gender_should_have_expected_string_representation(Gender givenGender, String expectedString) {

        // expect
        assertThat(givenGender.toString())
                .isEqualTo(expectedString);
    }
}