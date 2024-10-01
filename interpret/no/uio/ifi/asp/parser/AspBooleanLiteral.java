package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspBooleanLiteral extends AspAtom {
    Boolean booleanValue;
    AspBooleanLiteral(int n) {
	    super(n);
    }

    public static AspBooleanLiteral parse(Scanner s) {
        // Part 2:   
        enterParser("boolean literal");     
        AspBooleanLiteral ae = new AspBooleanLiteral(s.curLineNum());
        if (s.curToken().kind == TokenKind.trueToken) {
            ae.booleanValue = true;
            skip(s, TokenKind.trueToken);
        }

        else if (s.curToken().kind == TokenKind.falseToken) {
            ae.booleanValue = false;
            skip(s, TokenKind.falseToken);
        }

        leaveParser("boolean literal");
        return ae;        
    }

    @Override
    public void prettyPrint() {
	// Part 2:
        if (booleanValue) {
            prettyWrite("True");
        } else {
            prettyWrite("False");
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 3:
        trace("boolean literal");
        return new RuntimeBoolValue(booleanValue);
    }
}