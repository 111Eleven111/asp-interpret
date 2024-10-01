package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorPrefix extends AspSyntax {
    boolean isPlus;

    AspFactorPrefix(int n) {
        super(n);
    }

    public static AspFactorPrefix parse(Scanner s) {
        enterParser("factor prefix");

        AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());

        if (s.curToken().kind == plusToken) {
            afp.isPlus = true;
            skip(s, plusToken);
        } else {
            afp.isPlus = false;
            skip(s, minusToken);
        }

        leaveParser("factor prefix");
        return afp;
    }

    @Override
    public void prettyPrint() {
        // -- Must be changed in part 2:
        if (isPlus) {
            prettyWrite("+");
        } else {
            prettyWrite("-");
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- Must be changed in part 4:
        trace("factor prefix");
        return null;
    }
}
