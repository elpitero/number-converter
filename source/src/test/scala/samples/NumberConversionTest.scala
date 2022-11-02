package samples

import org.junit._
import com.example1._

@Test
class NumberConversionTest {
    @Test
    def testConversionToRomanSuccess(): Unit = {
        assert(NumberConverter.toRoman("3333") == "MMMCCCXXXIII")
        assert(NumberConverter.toRoman("99") == "XCIX")
        assert(NumberConverter.toRoman("2000") == "MM")
        assert(NumberConverter.toRoman("1974") == "MCMLXXIV")
        assert(NumberConverter.toRoman("789") == "DCCLXXXIX")
    }

    @Test
    def testConversionToHexSuccess(): Unit = {
        assert(NumberConverter.toHex("789") == "315")
        assert(NumberConverter.toHex("2001314") == "1e89a2")
        assert(NumberConverter.toHex("0") == "0")
        assert(NumberConverter.toHex("16") == "10")
        assert(NumberConverter.toHex("99999999") == "5f5e0ff")
    }

    @Test
    def testConversionToRomanExceptions(): Unit = {
        val thrown: Array[Boolean] = Array(false, false, false)

        try {
            NumberConverter.toRoman("0")
        } catch {
            case _: NotValidRomanNumberException => thrown(0) = true
        }

        try {
            NumberConverter.toRoman("-5000")
        } catch {
            case _: NotValidRomanNumberException => thrown(1) = true
        }

        try {
            NumberConverter.toRoman("-5e.2")
        } catch {
            case _: NotValidRomanNumberException => thrown(2) = true
        }

        assert(thrown(0) && thrown(1) && thrown (2))
    }

    @Test
    def testConversionToHexExceptions(): Unit = {
        val thrown: Array[Boolean] = Array(false, false, false)

        try {
            NumberConverter.toHex("-5")
        } catch {
            case _: NotDecimalNumberException => thrown(0) = true
        }

        try {
            NumberConverter.toHex("9999999999")
        } catch {
            case _: NumberFormatException => thrown(1) = true
        }

        try {
            NumberConverter.toHex("-5e.2")
        } catch {
            case _: NotDecimalNumberException => thrown(2) = true
        }

        assert(thrown(0) && thrown(1) && thrown(2))
    }
}


