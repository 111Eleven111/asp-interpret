package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspSuite extends AspSyntax {
    AspSmallStmtList sstmtl = null;
    ArrayList<AspStmt> stmts = new ArrayList<>();

    AspSuite(int n) {
        super(n);
    }

    public static AspSuite parse(Scanner s) {
        enterParser("suite");
        AspSuite su = new AspSuite(s.curLineNum());

        if (s.curToken().kind != newLineToken) {
            su.sstmtl = AspSmallStmtList.parse(s);
        
        } else {
            skip(s, newLineToken);
            skip(s, indentToken);
            
            su.stmts.add(AspStmt.parse(s));
            while (s.curToken().kind != dedentToken) {
                su.stmts.add(AspStmt.parse(s));
            }
            skip(s, dedentToken);
        }

        leaveParser("suite");
        return su;
    }

    @Override
    void prettyPrint() {
        if (sstmtl == null) {
            prettyWriteLn();
            prettyIndent();
            for (AspStmt stmt: stmts) {
                stmt.prettyPrint();
            }
            //prettyWriteLn();
            prettyDedent();

        } else {
            sstmtl.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("suite");
        
        RuntimeValue v = null;
        if (sstmtl != null) {
            v = sstmtl.eval(curScope);

        } else {
            for (AspStmt stmt : stmts) {
                v = stmt.eval(curScope);
            }
        }
        return v;
    }
}
