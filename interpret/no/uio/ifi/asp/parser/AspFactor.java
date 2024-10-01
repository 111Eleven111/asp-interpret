package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactor extends AspSyntax {
    ArrayList<AspFactorPrefix> fPrefixes = new ArrayList<>();
    ArrayList<AspPrimary> primaries = new ArrayList<>();
    ArrayList<AspFactorOpr> fOprs = new ArrayList<>();

    AspFactor(int n) {
        super(n);
    }

    public static AspFactor parse(Scanner s) {
        enterParser("factor");

        AspFactor af = new AspFactor(s.curLineNum());

        if (s.isFactorPrefix()) {
            af.fPrefixes.add(AspFactorPrefix.parse(s));
        } else {
            af.fPrefixes.add(null);
        }
        af.primaries.add(AspPrimary.parse(s));

        while (s.isFactorOpr()) {
            af.fOprs.add(AspFactorOpr.parse(s));
            if (s.isFactorPrefix()) {
                af.fPrefixes.add(AspFactorPrefix.parse(s));
            } else {
                af.fPrefixes.add(null);
            }
            af.primaries.add(AspPrimary.parse(s));
        }

        leaveParser("factor");
        return af;
    }

    @Override
    public void prettyPrint() {

        for (int i = 0; i < primaries.size(); i++) {
            if (fPrefixes.get(i) != null) {
                fPrefixes.get(i).prettyPrint();
            }
            primaries.get(i).prettyPrint();
            if (i < fOprs.size()) {
                fOprs.get(i).prettyPrint();
            }
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- Must be changed in part 4:
        // Temp:
        // Hva vil det si når det er flere primaries?
        trace("factor");
        RuntimeValue v1 = primaries.get(0).eval(curScope);
        if (fPrefixes.get(0) != null) {
            if (!fPrefixes.get(0).isPlus)
                v1 = v1.evalNegate(this);
        }

        for (int i = 1; i < primaries.size(); i++) {
            RuntimeValue v2 = primaries.get(i).eval(curScope);
            if (fPrefixes.get(i) != null && !(fPrefixes.get(i).isPlus)) {
                v2 = v2.evalNegate(this);
            }
            if (fOprs.get(i - 1) != null && fOprs.get(i - 1).factorOprString.equals("*")) {
                v1 = v1.evalMultiply(v2, this);
            } else if (fOprs.get(i - 1) != null && fOprs.get(i - 1).factorOprString.equals("/")) {
                v1 = v1.evalDivide(v2, this);
            } else if (fOprs.get(i - 1) != null && fOprs.get(i - 1).factorOprString.equals("%")) {
                v1 = v1.evalModulo(v2, this);
            } else {
                v1 = v1.evalIntDivide(v2, this);
            }
        }
        return v1;
    }
}
