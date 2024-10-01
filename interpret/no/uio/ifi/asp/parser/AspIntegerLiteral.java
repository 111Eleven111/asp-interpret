package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspIntegerLiteral extends AspAtom {
    Long value;
    AspIntegerLiteral(int n) {
	    super(n);
    }

    public static AspIntegerLiteral parse(Scanner s) {
        //-- Must be changed in part 2:
        enterParser("integer literal");
        AspIntegerLiteral ae = new AspIntegerLiteral(s.curLineNum());
        ae.value = s.curToken().integerLit;
        skip(s, TokenKind.integerToken);
        leaveParser("integer literal");
        return ae;
    }

    @Override
    public void prettyPrint() {
	    // Part 2:
        prettyWrite(Long.toString(value));
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    // Part 3:
        trace("integer literal");
	    return new RuntimeIntValue(value);
    }
}