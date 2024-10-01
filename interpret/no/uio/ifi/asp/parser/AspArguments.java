package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspArguments extends AspSyntax {
    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }

    public static AspArguments parse(Scanner s) {
        enterParser("arguments");

        skip(s, TokenKind.leftParToken);
        AspArguments a = new AspArguments(s.curLineNum());

        if (s.curToken().kind != rightParToken) {
            a.exprs.add(AspExpr.parse(s));
            while (s.curToken().kind == commaToken) {
                skip(s, commaToken);
                a.exprs.add(AspExpr.parse(s));
            }
        }

        skip(s, TokenKind.rightParToken);

        leaveParser("arguments");
        return a;
    }

    @Override
    public void prettyPrint() {
        // Part 2:
        prettyWrite("(");

        if (exprs.size() > 0) {
            for (int i = 0; i < exprs.size(); i++) {
                exprs.get(i).prettyPrint();
                if (i < exprs.size() - 1) {
                    prettyWrite(", ");
                }
            }
        }
        prettyWrite(")");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("arguments");
        ArrayList<RuntimeValue> listValues = new ArrayList<>();
        for (AspExpr expr : exprs) {
            listValues.add(expr.eval(curScope));
        }

        // RuntimeListValue listValue = new RuntimeListValue(listValues);
        return new RuntimeListValue(listValues);
    }
}
