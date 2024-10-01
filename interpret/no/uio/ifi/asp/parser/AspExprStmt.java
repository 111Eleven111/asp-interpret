package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;

class AspExprStmt extends AspAtom {
    AspExpr expr;

    AspExprStmt(int n) {
	    super(n);
    }

    public static AspExprStmt parse(Scanner s) {
        //-- Must be changed in part 2:
        enterParser("expr stmt");
        AspExprStmt es = new AspExprStmt(s.curLineNum());
        es.expr = AspExpr.parse(s);
        leaveParser("expr stmt");
        return es;
    }

    @Override
    void prettyPrint() {
        expr.prettyPrint();
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    // Part 3:
        trace("expr stmt");
	    return expr.eval(curScope);
    }
}
