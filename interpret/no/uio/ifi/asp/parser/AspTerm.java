package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;

public class AspTerm extends AspSyntax {
    ArrayList<AspFactor> factors = new ArrayList<>();
    ArrayList<AspTermOpr> termOprs = new ArrayList<>();

    AspTerm(int n) {
        super(n);
    }

    public static AspTerm parse(Scanner s) {
        enterParser("term");

        AspTerm at = new AspTerm(s.curLineNum());
        at.factors.add(AspFactor.parse(s));
        while (s.isTermOpr()) {
            at.termOprs.add(AspTermOpr.parse(s));
            at.factors.add(AspFactor.parse(s));
        }

        leaveParser("term");
        return at;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < factors.size(); i++) {
            factors.get(i).prettyPrint();
            if (i < termOprs.size()) {
                termOprs.get(i).prettyPrint();
            }
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        trace("term");
        RuntimeValue v = factors.get(0).eval(curScope);
        for (int i = 1; i < factors.size(); i++) {
            if (!termOprs.get(i - 1).plus) {
                v = v.evalSubtract(factors.get(i).eval(curScope), this);
            } else if (termOprs.get(i - 1).plus) {
                v = v.evalAdd(factors.get(i).eval(curScope), this);
            } else {
                Main.panic("Illegal term operator!");
            }
        }
        return v;
    }
}
