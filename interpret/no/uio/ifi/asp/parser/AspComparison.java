package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspComparison extends AspSyntax {
    ArrayList<AspTerm> terms = new ArrayList<>();
    ArrayList<AspCompOpr> compOprs = new ArrayList<>();

    AspComparison(int n) {
        super(n);
    }

    public static AspComparison parse(Scanner s) {
        enterParser("comparison");

        AspComparison ac = new AspComparison(s.curLineNum());
        ac.terms.add(AspTerm.parse(s));

        while (s.isCompOpr()) {
            ac.compOprs.add(AspCompOpr.parse(s));
            ac.terms.add(AspTerm.parse(s));
        }

        leaveParser("comparison");
        return ac;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < terms.size(); i++) {
            terms.get(i).prettyPrint();
            if (i < compOprs.size()) {
                compOprs.get(i).prettyPrint();
            }
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("comparison");
        RuntimeValue v = terms.get(0).eval(curScope);

        for (int i = 1; i < terms.size(); i++) {
            v = terms.get(i - 1).eval(curScope);
            if (compOprs.get(i - 1).opr == TokenKind.lessToken) {
                v = v.evalLess(terms.get(i).eval(curScope), this);
                if (!v.getBoolValue("comparison operator", this)) {
                    return new RuntimeBoolValue(false);
                }
            } else if (compOprs.get(i - 1).opr == TokenKind.greaterToken) {
                v = v.evalGreater(terms.get(i).eval(curScope), this);
                if (!v.getBoolValue("comparison operator", this)) {
                    return new RuntimeBoolValue(false);
                }
            } else if (compOprs.get(i - 1).opr == TokenKind.doubleEqualToken) {
                v = v.evalEqual(terms.get(i).eval(curScope), this);
                if (!v.getBoolValue("comparison operator", this)) {
                    return new RuntimeBoolValue(false);
                }
            } else if (compOprs.get(i - 1).opr == TokenKind.greaterEqualToken) {
                v = v.evalGreaterEqual(terms.get(i).eval(curScope), this);
                if (!v.getBoolValue("comparison operator", this)) {
                    return new RuntimeBoolValue(false);
                }
            } else if (compOprs.get(i - 1).opr == TokenKind.lessEqualToken) {
                v = v.evalLessEqual(terms.get(i).eval(curScope), this);
                if (!v.getBoolValue("comparison operator", this)) {
                    return new RuntimeBoolValue(false);
                }
            } else { // if notEqualToken
                v = v.evalNotEqual(terms.get(i).eval(curScope), this);
                if (!v.getBoolValue("comparison operator", this)) {
                    return new RuntimeBoolValue(false);
                }
            }
        }
        return v;
    }
}
