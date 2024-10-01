package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspPrimary extends AspSyntax {
    ArrayList<AspPrimarySuffix> pSuffixses = new ArrayList<>();
    AspAtom aa = null;

    AspPrimary(int n) {
        super(n);
    }

    public static AspPrimary parse(Scanner s) {
        enterParser("primary");

        AspPrimary ap = new AspPrimary(s.curLineNum());
        ap.aa = AspAtom.parse(s);

        while (s.curToken().kind == TokenKind.leftParToken || s.curToken().kind == TokenKind.leftBracketToken) {
            ap.pSuffixses.add(AspPrimarySuffix.parse(s));
        }

        leaveParser("primary");
        return ap;
    }

    @Override
    void prettyPrint() {
        aa.prettyPrint();
        for (AspPrimarySuffix aps : pSuffixses) {
            aps.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("primary");
        RuntimeValue v = aa.eval(curScope);
        if (!pSuffixses.isEmpty()) {
            trace("primary argument");
            // Arguments:
            if (pSuffixses.get(0).a != null) {
                ArrayList<RuntimeValue> valueList = new ArrayList<>();
                for (AspPrimarySuffix pSuffix : pSuffixses) {
                    valueList.add(pSuffix.eval(curScope));
                }
                v = v.evalFuncCall(valueList, this);
                trace("func call " + v.toString() + "with params: " + valueList);

            // Subscriptions:
            } else {
                trace("primary subscription");
                for (int i = 0; i < pSuffixses.size(); i++) {
                    if (v instanceof RuntimeListValue) {
                        v = v.evalSubscription(pSuffixses.get(i).eval(curScope), this);
                    } else if (v instanceof RuntimeDictValue) {
                        v = v.evalSubscription(pSuffixses.get(i).eval(curScope), this);
                    } else if (v instanceof RuntimeStringValue) {
                        v = v.evalSubscription(pSuffixses.get(i).eval(curScope), this);
                    }
                }
            }
        }
        return v;
    }
}
