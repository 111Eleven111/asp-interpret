package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspCompoundStmt extends AspStmt {
    AspForStmt forStmt = null;
    AspIfStmt ifStmt = null;
    AspWhileStmt whileStmt = null;
    AspFuncDef funcDef = null;

    AspCompoundStmt(int n) {
        super(n);
    }

    public static AspCompoundStmt parse(Scanner s) {
        enterParser("compound stmt");

        AspCompoundStmt acs = new AspCompoundStmt(s.curLineNum());

        if(s.curToken().kind == TokenKind.forToken) {
            acs.forStmt = AspForStmt.parse(s);
        } else if(s.curToken().kind == TokenKind.ifToken) {
            acs.ifStmt = AspIfStmt.parse(s);
        } else if(s.curToken().kind == TokenKind.whileToken) {
            acs.whileStmt = AspWhileStmt.parse(s);
        } else {
            acs.funcDef = AspFuncDef.parse(s);
        }

        leaveParser("compound stmt");
        return acs;
    }

    @Override
    public void prettyPrint() {
        if(forStmt != null) {
            forStmt.prettyPrint();
        } else if(ifStmt != null) {
            ifStmt.prettyPrint();
        } else if(whileStmt != null) {
            whileStmt.prettyPrint();
        } else {
            funcDef.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("compound stmt");
        if(forStmt != null) {
            return forStmt.eval(curScope);
        } else if(ifStmt != null) {
            return ifStmt.eval(curScope);
        } else if(whileStmt != null) {
            return whileStmt.eval(curScope);
        } else {
            return funcDef.eval(curScope);
        }
    }
}