package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;

class AspStringLiteral extends AspAtom {
    String stringLiteral;
    AspStringLiteral(int n) {
	    super(n);
    }

    public static AspStringLiteral parse(Scanner s) {
        //-- Must be changed in part 2:)
        enterParser("string literal");
        AspStringLiteral sl = new AspStringLiteral(s.curLineNum());
        sl = new AspStringLiteral(s.curLineNum());
        if (s.curToken().kind == TokenKind.stringToken) {
            sl.stringLiteral = s.curToken().stringLit;
        }
        skip(s, TokenKind.stringToken);
        leaveParser("string literal");
        return sl;
    }

    @Override
    public void prettyPrint() {
	    prettyWrite(" '" + stringLiteral + "' ");
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    // Part 3:
        trace("string literal");
	    return new RuntimeStringValue(stringLiteral);
    }
}