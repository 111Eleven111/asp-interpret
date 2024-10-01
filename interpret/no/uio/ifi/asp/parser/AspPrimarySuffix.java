package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspPrimarySuffix extends AspSyntax {
    AspArguments a = null;
    AspSubscription s = null;

    AspPrimarySuffix(int n) {
        super(n);
    }

    public static AspPrimarySuffix parse(Scanner s) {
        enterParser("primary suffix");

        AspPrimarySuffix aps = new AspPrimarySuffix(s.curLineNum());
        if (s.curToken().kind == TokenKind.leftBracketToken) {
            aps.s = AspSubscription.parse(s);
        } else {
            aps.a = AspArguments.parse(s);
        }

        leaveParser("primary suffix");
        return aps;
    }

    @Override
    void prettyPrint() {
        if (s != null) {
            s.prettyPrint();
        } else {
            a.prettyPrint();
        }

    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("primary suffix");
        RuntimeValue v;
        if (a != null) {
            v = a.eval(curScope);
        } else {
            v = s.eval(curScope);
        }
        return v;
    }
}
