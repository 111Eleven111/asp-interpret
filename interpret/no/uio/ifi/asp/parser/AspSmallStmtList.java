package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> sstmts = new ArrayList<>();

    AspSmallStmtList(int n) {
	    super(n);
    }

    public static AspSmallStmtList parse(Scanner s) {
        enterParser("small stmt list");
        AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());
        assl.sstmts.add(AspSmallStmt.parse(s));
        
        while (s.curToken().kind == semicolonToken) {
            skip(s, semicolonToken);
            if (s.curToken().kind != newLineToken) {
                assl.sstmts.add(AspSmallStmt.parse(s));
            }
        }
        skip(s, newLineToken);
        leaveParser("small stmt list");
        return assl;
    }


    @Override
    void prettyPrint() {
        int i = 0;

        for (AspSmallStmt sstmt : sstmts) {
            sstmt.prettyPrint();
            if(i < this.sstmts.size()-1)
                prettyWrite("; ");
            i++;
        }
        prettyWriteLn();
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    // Part 4:
        trace("small stmt list");
        RuntimeValue v = null;
        for (AspSmallStmt sStmt : sstmts) {
            v = sStmt.eval(curScope);
        }
	    return v;
    }
}
