package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;

class AspListDisplay extends AspAtom {
    ArrayList<AspExpr> exprs = new ArrayList<>();
    AspListDisplay(int n) {
	    super(n);
    }

    public static AspListDisplay parse(Scanner s) {
	// Part 2:
    enterParser("list display");
    skip(s, TokenKind.leftBracketToken);
    AspListDisplay ld = new AspListDisplay(s.curLineNum());
    
    if (s.curToken().kind != TokenKind.rightBracketToken) {
        ld.exprs.add(AspExpr.parse(s));
        while (s.curToken().kind != TokenKind.rightBracketToken) {
            skip(s, TokenKind.commaToken);
            ld.exprs.add(AspExpr.parse(s));
        }
    }

    skip(s, TokenKind.rightBracketToken);
    
	leaveParser("list display");
	return ld;
    }

    @Override
    public void prettyPrint() {
	//-- Must be changed in part 2:
        prettyWrite(" [ ");
        
        if(exprs.size() > 0) {
            exprs.get(0).prettyPrint();
        }
        if (exprs.size() > 1) {
            for (int i = 1; i < exprs.size(); i++) {
                prettyWrite(", "); 
                exprs.get(i).prettyPrint();
            }
        }
        prettyWrite(" ] ");
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    // Part 3:
        trace("list display");
        ArrayList<RuntimeValue> v = new ArrayList<>();
        for (AspExpr expr : exprs) v.add(expr.eval(curScope));
        return new RuntimeListValue(v);

    }
}