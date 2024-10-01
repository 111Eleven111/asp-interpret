package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspCompOpr extends AspSyntax {
    TokenKind opr = null;

    AspCompOpr(int n) {
	super(n);
    }

    public static AspCompOpr parse(Scanner s) {
        enterParser("comp opr");

        AspCompOpr aco = new AspCompOpr(s.curLineNum());
        aco.opr = s.curToken().kind;
        skip(s, s.curToken().kind);

        leaveParser("comp opr");
        return aco;
    }


    @Override
    public void prettyPrint() {
	// Part 2:
        if(opr == TokenKind.lessToken) {
            prettyWrite(" < ");
        } else if(opr == TokenKind.greaterToken) {
            prettyWrite(" > ");
        } else if(opr == TokenKind.doubleEqualToken) {
            prettyWrite(" == ");
        } else if(opr == TokenKind.greaterEqualToken) {
            prettyWrite(" >= ");
        } else if(opr == TokenKind.lessEqualToken) {
            prettyWrite(" <= ");
        } else {
            prettyWrite(" != ");
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	//-- Must be changed in part 4:
	return null;
    }
}
