package samples

import org.junit._
import com.example1.messages.NumberConverter._
import com.example1.exceptions.Exceptions._

@Test
class NumberConversionTest {
    @Test
    def testConversionToRomanSuccess(): Unit = {
        assert(toRoman("3333") == "MMMCCCXXXIII")
        assert(toRoman("99") == "XCIX")
        assert(toRoman("2000") == "MM")
        assert(toRoman("1974") == "MCMLXXIV")
        assert(toRoman("789") == "DCCLXXXIX")
    }

    @Test
    def testConversionToHexSuccess(): Unit = {
        assert(toHex("789") == "315")
        assert(toHex("2001314") == "1e89a2")
        assert(toHex("0") == "0")
        assert(toHex("16") == "10")
        assert(toHex("99999999") == "5f5e0ff")
    }

    @Test
    def testConversionToRomanExceptions(): Unit = {
        val thrown: Array[Boolean] = Array(false, false, false)

        try {
            toRoman("0")
        } catch {
            case _: NotValidRomanNumberException => thrown(0) = true
        }

        try {
            toRoman("-5000")
        } catch {
            case _: NotValidRomanNumberException => thrown(1) = true
        }

        try {
            toRoman("-5e.2")
        } catch {
            case _: NotValidRomanNumberException => thrown(2) = true
        }

        assert(thrown(0) && thrown(1) && thrown (2))
    }

    @Test
    def testConversionToHexExceptions(): Unit = {
        val thrown: Array[Boolean] = Array(false, false, false)

        try {
            toHex("-5")
        } catch {
            case _: NotDecimalNumberException => thrown(0) = true
        }

        try {
            toHex("9999999999")
        } catch {
            case _: NumberFormatException => thrown(1) = true
        }

        try {
            toHex("-5e.2")
        } catch {
            case _: NotDecimalNumberException => thrown(2) = true
        }

        assert(thrown(0) && thrown(1) && thrown(2))
    }
}


