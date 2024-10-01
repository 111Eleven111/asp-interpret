package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFuncDef extends AspSyntax {
    AspName funcName = null;
    public ArrayList<AspName> argument_names = new ArrayList<>();
    public AspSuite suite = null;

    AspFuncDef(int n) {
        super(n);
    }

    public static AspFuncDef parse(Scanner s) {
        enterParser("func def");

        AspFuncDef afd = new AspFuncDef(s.curLineNum());

        skip(s, TokenKind.defToken);
        afd.funcName = AspName.parse(s);
        skip(s, TokenKind.leftParToken);

        if(s.curToken().kind != TokenKind.rightParToken) {
            afd.argument_names.add(AspName.parse(s));
            while(s.curToken().kind == TokenKind.commaToken) {
                skip(s, TokenKind.commaToken);
                afd.argument_names.add(AspName.parse(s));
            }
        }

        skip(s, TokenKind.rightParToken);
        skip(s, TokenKind.colonToken);

        afd.suite = AspSuite.parse(s);

        leaveParser("func def");
        return afd;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("def ");
        funcName.prettyPrint();
        prettyWrite("(");
        if(argument_names.size() > 0) {
            for(int i = 0; i < argument_names.size(); i++) {
                argument_names.get(i).prettyPrint();
                if(i < argument_names.size() - 1) {
                    prettyWrite(", ");
                }
            }
        }
        prettyWrite(")");
        prettyWrite(":");
        suite.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("Func def");
        RuntimeFuncValue v = new RuntimeFuncValue(this, funcName.nameLiteralString, curScope);
        curScope.assign(funcName.nameLiteralString, v);
        return v;
    }
}