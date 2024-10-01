package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;

class AspFloatLiteral extends AspAtom {
    double doubleLiteral;

    AspFloatLiteral(int n) {
        super(n);
    }

    public static AspFloatLiteral parse(Scanner s) {
        // -- Must be changed in part 2:
        AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());

        afl.doubleLiteral = s.curToken().floatLit;
        skip(s, TokenKind.floatToken);

        return afl;
    }

    @Override
    public void prettyPrint() {
        // Part 2:
        prettyWrite(Double.toString(doubleLiteral));
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 3:
        trace("float literal");
        return new RuntimeFloatValue(doubleLiteral);
    }
}