package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspGlobalStmt extends AspSyntax {
    ArrayList<AspName> names = new ArrayList<>();

    AspGlobalStmt(int n) {
        super(n);
    }

    public static AspGlobalStmt parse(Scanner s) {
        enterParser("global stmt");
        skip(s, globalToken);
        AspGlobalStmt gs = new AspGlobalStmt(s.curLineNum());
        gs.names.add(AspName.parse(s));

        while (s.curToken().kind == commaToken) {
            gs.names.add(AspName.parse(s));
        }

        leaveParser("global stmt");
        return gs;
    }

    @Override
    void prettyPrint() {
        prettyWrite("global ");
        prettyWrite(names.get(0).nameLiteralString);
        
        if (names.size() > 1) {
            for (int i = 1; i < names.size(); i++) {
                prettyWrite(", " + names.get(i).nameLiteralString);
            }
        }
        prettyWrite("\n");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- Must be changed in part 4:
        trace("global stmt");
        for(AspName name : names) {
            curScope.registerGlobalName(name.nameLiteralString);
        }
        return null;
    }
}
