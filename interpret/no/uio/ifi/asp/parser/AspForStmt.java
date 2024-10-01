package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspForStmt extends AspSyntax {
    AspName name = null;
    AspExpr expr = null;
    AspSuite suite = null;

    AspForStmt(int n) {
        super(n);
    }

    public static AspForStmt parse(Scanner s) {
        enterParser("for stmt");

        AspForStmt afs = new AspForStmt(s.curLineNum());

        skip(s, TokenKind.forToken);
        afs.name = AspName.parse(s);
        skip(s, TokenKind.inToken);
        afs.expr = AspExpr.parse(s);
        skip(s, colonToken);
        afs.suite = AspSuite.parse(s);

        leaveParser("for stmt");
        return afs;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("for ");
        name.prettyPrint();
        prettyWrite(" in ");
        expr.prettyPrint();
        prettyWrite(": ");
        suite.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        RuntimeValue value = expr.eval(curScope);
        if (value instanceof RuntimeListValue) {
            trace("for stmt");
            RuntimeListValue listValue = (RuntimeListValue) value; // Må vi caste?
            for (RuntimeValue indeks : listValue.list) {
                curScope.assign(name.nameLiteralString, indeks);
                suite.eval(curScope);
            }
        }
        return null;
    }
}