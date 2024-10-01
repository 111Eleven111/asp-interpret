package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspInnerExpr extends AspAtom {
    AspExpr expr = null;

    AspInnerExpr(int n) {
	    super(n);
    }

    public static AspInnerExpr parse(Scanner s) {
        // Part 2:
        enterParser("inner expr");

        AspInnerExpr aie = new AspInnerExpr(s.curLineNum());
        skip(s, TokenKind.leftParToken);
        aie.expr = AspExpr.parse(s);
        skip(s, TokenKind.rightParToken);

        leaveParser("inner expr");
        return aie;
    }

    @Override
    public void prettyPrint() {
        // Part 2:
        prettyWrite("(");
        expr.prettyPrint();
        prettyWrite(")");
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    // Part 3:
        trace("inner expr");
	    return expr.eval(curScope);
    }
}