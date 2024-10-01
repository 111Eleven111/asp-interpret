package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspDictDisplay extends AspAtom {
    ArrayList<AspStringLiteral> stringLits = new ArrayList<>();
    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspDictDisplay(int n) {
	    super(n);
    }

    public static AspDictDisplay parse(Scanner s) {
        // Part 2:
        enterParser("dict display");
        AspDictDisplay add = new AspDictDisplay(s.curLineNum());

        skip(s, TokenKind.leftBraceToken);
    
        if(s.curToken().kind != TokenKind.rightBraceToken) {
            add.stringLits.add(AspStringLiteral.parse(s));
            skip(s, TokenKind.colonToken);
            add.exprs.add(AspExpr.parse(s));
            while(s.curToken().kind == TokenKind.commaToken) {
                skip(s, TokenKind.commaToken);
                add.stringLits.add(AspStringLiteral.parse(s));
                skip(s, TokenKind.colonToken);
                add.exprs.add(AspExpr.parse(s));
            }
        }
        skip(s, TokenKind.rightBraceToken);

        leaveParser("dict display");
        return add;
    }

    @Override
    public void prettyPrint() {
	    // Part 2:
        prettyWrite("{");
        if(stringLits.size() > 0) {
            for(int i = 0; i < stringLits.size(); i++) {
                stringLits.get(i).prettyPrint();
                prettyWrite(" : ");
                exprs.get(i).prettyPrint();
                if(i < stringLits.size() - 1) {
                    prettyWrite(", ");
                }
            }
        }
        prettyWrite("}");
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 3:
        trace("dict display");
        Dictionary<String, RuntimeValue> dict = new Hashtable<>();
        for (int i = 0; i < stringLits.size(); i ++) {
            dict.put(stringLits.get(i).stringLiteral, exprs.get(i).eval(curScope)); 
        }
        return new RuntimeDictValue(dict);
    }
}