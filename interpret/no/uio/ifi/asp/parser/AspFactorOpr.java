package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorOpr extends AspSyntax {
    String factorOprString;

    AspFactorOpr(int n) {
        super(n);
    }

    public static AspFactorOpr parse(Scanner s) {
        enterParser("factor opr");

        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());

        if (s.curToken().kind == astToken) {
            afo.factorOprString = "*";
            skip(s, astToken);
        } else if (s.curToken().kind == slashToken) {
            afo.factorOprString = "/";
            skip(s, slashToken);
        } else if (s.curToken().kind == percentToken) {
            afo.factorOprString = "%";
            skip(s, percentToken);
        } else {
            afo.factorOprString = "//";
            skip(s, doubleSlashToken);
        }

        leaveParser("factor opr");
        return afo;
    }

    @Override
    public void prettyPrint() {
        // Part 2:
        prettyWrite(" " + factorOprString + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- Must be changed in part 4:
        trace("factor opr");
        return new RuntimeStringValue(factorOprString);
    }
}