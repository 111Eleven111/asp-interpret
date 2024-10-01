package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspNoneLiteral extends AspAtom {
    AspNoneLiteral(int n) {
	    super(n);
    }

    public static AspNoneLiteral parse(Scanner s) {
	// Part 2:
    enterParser("none literal");
    
	AspNoneLiteral ae = new AspNoneLiteral(s.curLineNum());
    skip(s, TokenKind.noneToken);

    leaveParser("none literal");
	return ae;
    }

    @Override
    public void prettyPrint() {
	//-- Must be changed in part 2:
    prettyWrite("None");
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	// Part 3:
	return new RuntimeNoneValue();
    }
}