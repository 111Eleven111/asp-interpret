
package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSubscription extends AspSyntax {
    AspExpr expr;

    AspSubscription(int n) {
        super(n);
    }

    public static AspSubscription parse(Scanner s) {
        enterParser("subscription");
        
        AspSubscription su = new AspSubscription(s.curLineNum());
        
        skip(s, TokenKind.leftBracketToken);
        su.expr = AspExpr.parse(s);
        skip(s, TokenKind.rightBracketToken);

        leaveParser("subscription");
        return su;
    }

    @Override
    public void prettyPrint() {
        // Part 2:
        prettyWrite("[");
        expr.prettyPrint();
        prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("subscription");
        return expr.eval(curScope);
    }
}
