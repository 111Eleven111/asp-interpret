package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNotTest extends AspSyntax {
    AspComparison comparison;
    Boolean not = false;

    AspNotTest(int n) {
        super(n);
    }

    static AspNotTest parse(Scanner s) {
        enterParser("not test");

        AspNotTest ant = new AspNotTest(s.curLineNum());

        if (s.curToken().kind == TokenKind.notToken) {
            ant.not = true;
            skip(s, notToken);
        }
        ant.comparison = AspComparison.parse(s);

        leaveParser("not test");
        return ant;
    }

    @Override
    void prettyPrint() {
        if(this.not) prettyWrite(" not ");
        comparison.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 3:
        trace("not test");
        RuntimeValue v = comparison.eval(curScope);
        if (not) {
            v = v.evalNot(this);
        }
        return v;
    }
}
