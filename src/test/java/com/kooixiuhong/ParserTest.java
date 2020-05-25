package com.kooixiuhong;

import com.kooixiuhong.rewritesystem.app.parser.ASTParser;
import com.kooixiuhong.rewritesystem.app.parser.ParseException;
import com.kooixiuhong.rewritesystem.app.parser.Signature;
import com.kooixiuhong.rewritesystem.app.syntaxtree.BinaryOperator;
import com.kooixiuhong.rewritesystem.app.syntaxtree.DataType;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Operator;
import com.kooixiuhong.rewritesystem.app.syntaxtree.UnaryOperator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * This is a JUnit test class for the ASTParser
 *
 * @author Kooi
 * @date 4th Febuary 2019
 */
public class ParserTest {


    static ASTParser parser;

    @BeforeClass
    public static void initialise() {
        Set<Operator> ops = new HashSet<>();
        ops.add(new BinaryOperator("+", 2, DataType.INT));
        ops.add(new BinaryOperator("-", 2, DataType.INT));
        ops.add(new BinaryOperator("*", 3, DataType.INT));
        ops.add(new BinaryOperator("/", 3, DataType.INT));
        ops.add(new UnaryOperator("!", 1, DataType.INT));
        ops.add(new UnaryOperator("succ", 2, DataType.INT));

        HashMap<String, DataType> variables = new HashMap<>();
        variables.put("x", DataType.INT);
        variables.put("y", DataType.INT);


        Signature c = new Signature(ops, variables);

        parser = new ASTParser(c);
    }


    //The suite of tests below test the split string method in the ASTParser class

    @Test
    public void testTokenizeString1() {
        String input = "3+4";

        String[] ans = {"3", "+", "4"};

        assertArrayEquals(ans, parser.tokenizeString(input).toArray());

    }

    @Test
    public void testTokenizeString2() {
        String input = "13+10/2";

        String[] ans = {"13", "+", "10", "/", "2"};

        assertArrayEquals(ans, parser.tokenizeString(input).toArray());

    }

    @Test
    public void testTokenizeString3() {
        String input = "13+4-2+(10-87)";
        String[] ans = {"13", "+", "4", "-", "2", "+", "(", "10", "-", "87", ")"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());

    }

    @Test
    public void testTokenizeString4() {
        String input = "y+z-t";
        String[] ans = {"y", "+", "z", "-", "t"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }

    @Test
    public void testTokenizeString5() {
        String input = "True + False -(True + True)";
        String[] ans = {"True", "+", "False", "-", "(", "True", "+", "True", ")"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }

    @Test
    public void testTokenizeString6() {
        String input = "True AND False OR True AND True";
        String[] ans = {"TrueANDFalseORTrueANDTrue"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }

    @Test
    public void testTokenizeStringChainOperators1() {
        String input = "a+-b+c";
        String[] ans = {"a", "+", "-", "b", "+", "c"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }

    @Test
    public void testTokenizeStringChainOperators2() {
        String input = "a+-++++b+c";
        String[] ans = {"a", "+", "-", "+", "+", "+", "+", "b", "+", "c"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }

    @Test
    public void testTokenizeStringChainOperators3() {
        String input = "+-++++b";
        String[] ans = {"+", "-", "+", "+", "+", "+", "b"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }

    @Test
    public void testTokenizeString7() {
        String input = "True + False -True(True + True)";
        String[] ans = {"True", "+", "False", "-", "True", "(", "True", "+", "True", ")"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }

    @Test
    public void testTokenizeString8() {
        String input = "34+21-2(7)";
        String[] ans = {"34", "+", "21", "-", "2", "(", "7", ")"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }

    @Test
    public void testTokenizeStringFunction1() {
        String input = "succ(x)";
        String[] ans = {"succ", "(", "x", ")"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }

    @Test
    public void testTokenizeStringFunction2() {
        String input = "succ(x+y)";
        String[] ans = {"succ", "(", "x", "+", "y", ")"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }

    @Test
    public void testTokenizeStringFunction3() {
        String input = "succ(x+succ(y))";
        String[] ans = {"succ", "(", "x", "+", "succ", "(", "y", ")", ")"};
        assertArrayEquals(ans, parser.tokenizeString(input).toArray());
    }


    // The suite of tests to test parsing infix notation into RPN


    @Test
    public void testInfixToRPN1() throws ParseException {

        String infix = "3+4";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "3 4 +";
        assertEquals(expectedPostFix, postFix);

    }

    @Test
    public void testInfixToRPN2() throws ParseException {

        String infix = "3+4+2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "3 4 + 2 +";
        assertEquals(expectedPostFix, postFix);

    }

    @Test
    public void testInfixToRPN3() throws ParseException {

        String infix = "3+4*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "3 4 2 * +";
        assertEquals(expectedPostFix, postFix);

    }

    @Test
    public void testInfixToRPN4() throws ParseException {

        String infix = "13+4*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "13 4 2 * +";
        assertEquals(expectedPostFix, postFix);

    }

    @Test
    public void testInfixToRPN5() throws ParseException {
        String infix = "(13+4)*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "13 4 + 2 *";
        assertEquals(expectedPostFix, postFix);

    }

    @Test
    public void testInfixToRPN6() throws ParseException {
        String infix = "1+10*(2+2)+(2-1)";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "1 10 2 2 + * + 2 1 - +";
        assertEquals(expectedPostFix, postFix);

    }

    @Test
    public void testInfixToRPN7() throws ParseException {
        String infix = "2*((3+2)-1)";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "2 3 2 + 1 - *";
        assertEquals(expectedPostFix, postFix);

    }

    @Test
    public void testInfixToRPN8() throws ParseException {
        String infix = "True + False -(True + True)";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "True False + True True + -";
        assertEquals(expectedPostFix, postFix);
    }

    @Test
    public void testInfixToRPN9() throws ParseException {
        String infix = "True + False -(True + (False - False)) + True + True";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "True False + True False False - + - True + True +";
        assertEquals(expectedPostFix, postFix);
    }

    @Test
    public void testInfixToRPN10() throws ParseException {
        String infix = "!!True";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "True ! !";
        assertEquals(expectedPostFix, postFix);
    }

    @Test
    public void testInfixToRPN11() throws ParseException {
        String infix = "True + (!!True)";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "True True ! ! +";
        assertEquals(expectedPostFix, postFix);
    }

    @Test
    public void testInfixToRPN12() throws ParseException {
        String infix = "succ(x)";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "x succ";
        assertEquals(expectedPostFix, postFix);
    }

    @Test
    public void testInfixToRPN13() throws ParseException {
        String infix = "succ(x) + y";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "x succ y +";
        assertEquals(expectedPostFix, postFix);
    }

    @Test
    public void testInfixToRPN14() throws ParseException {
        String infix = "succ(x + y)";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "x y + succ";
        assertEquals(expectedPostFix, postFix);
    }

    @Test
    public void testInfixToRPN15() throws ParseException {
        String infix = "y+succ(x)";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "y x succ +";
        assertEquals(expectedPostFix, postFix);
    }

    @Test
    public void testInfixToRPN16() throws ParseException {
        String infix = "True + !!False";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "True False ! ! +";
        assertEquals(expectedPostFix, postFix);
    }

    @Test(expected = ParseException.class)
    public void testInfixToRPNException1() throws ParseException {

        String infix = "13+(4*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);

    }

    @Test(expected = ParseException.class)
    public void testInfixToRPNException2() throws ParseException {

        String infix = "(13+4*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);

    }

    @Test(expected = ParseException.class)
    public void testInfixToRPNException3() throws ParseException {

        String infix = "13)+4*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);

    }

    @Test(expected = ParseException.class)
    public void testInfixToRPNException4() throws ParseException {

        String infix = "(13+4))*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);

    }

    @Test
    public void testInfixToRPNFunction() throws ParseException {

        String infix = "f(34)+4*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "f 34 4 2 * +";
        assertEquals(expectedPostFix, postFix);
    }

    @Test(expected = ParseException.class)
    public void testInfixToRPNException5() throws ParseException {

        String infix = "14+4*2+(";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);
    }


    @Test(expected = ParseException.class)
    public void testInfixToRPNException6() throws ParseException {

        String infix = "2+-4*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);
    }

    @Test(expected = ParseException.class)
    public void testInfixToRPNException7() throws ParseException {

        String infix = "2+-+4*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);
    }

    @Test(expected = ParseException.class)
    public void testInfixToRPNException8() throws ParseException {

        String infix = "succ(2)+-+4*2";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);
    }

    @Test(expected = ParseException.class)
    public void testInfixToRPNException9() throws ParseException {

        String infix = "2+(-2+3)";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);
    }

    @Test(expected = ParseException.class)
    public void testInfixToRPNException10() throws ParseException {

        String infix = "2+2(-2+3)";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);
    }

    @Test(expected = ParseException.class)
    public void testInfixToRPNException11() throws ParseException {

        String infix = "2+2-(2+3)+";
        String postFix = parser.parseRPN(infix);
        String expectedPostFix = "";
        assertEquals(expectedPostFix, postFix);
    }

    // The suite of tests to test parsing infix notation into AST

    @Test
    public void testInfixToAST1() throws ParseException {
        String infix = "3+4";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "3 4 +";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));
    }

    @Test
    public void testInfixToAST2() throws ParseException {
        String infix = "3+4+2";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "3 4 + 2 +";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));
    }

    @Test
    public void testInfixToAST3() throws ParseException {
        String infix = "3+4*2";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "3 4 2 * +";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));
    }

    @Test
    public void testInfixToAST4() throws ParseException {
        String infix = "(13+4)*2";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "13 4 + 2 *";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));
    }

    @Test
    public void testInfixToAST5() throws ParseException {
        String infix = "3+4+2";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "3 4 + 2 +";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));
    }

    @Test
    public void testInfixToAST6() throws ParseException {
        String infix = "1+10*(2+2)+(2-1)";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "1 10 2 2 + * + 2 1 - +";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));
    }

    @Test
    public void testInfixToAST7() throws ParseException {
        String infix = "2*((3+2)-1)";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "2 3 2 + 1 - *";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));

    }

    @Test
    public void testInfixToAST8() throws ParseException {
        String infix = "True + False -(True + True)";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "True False + True True + -";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));
    }

    @Test
    public void testInfixToAST9() throws ParseException {
        String infix = "True + False -(True + (False - False)) + True + True";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "True False + True False False - + - True + True +";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));
    }

    @Test
    public void testInfixToAST10() throws ParseException {
        String infix = "!!True";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "True ! !";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));
    }

    @Test
    public void testInfixToAST11() throws ParseException {
        String infix = "True + (!!True)";
        Node astRoot = parser.parseAST(infix);
        String expectedPostFix = "True True ! ! +";
        assertEquals(expectedPostFix, parser.postOrderTreverse(astRoot));
    }


}
