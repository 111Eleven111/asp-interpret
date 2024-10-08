package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPassStmt extends AspSyntax {
    AspName name = null;
    AspExpr expr = null;
    AspSuite suite = null;

    AspPassStmt(int n) {
        super(n);
    }

    public static AspPassStmt parse(Scanner s) {
        enterParser("pass stmt");

        AspPassStmt aps = new AspPassStmt(s.curLineNum());

        skip(s, TokenKind.passToken);

        leaveParser("pass stmt");
        return aps;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("pass");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        trace("pass");
        return null;
    }
}