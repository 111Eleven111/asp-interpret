package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmt extends AspSyntax {
    AspAssignment assignment = null;
    AspExprStmt exprStmt = null;
    AspGlobalStmt globalStmt = null;
    AspPassStmt passStmt = null;
    AspReturnStmt returnStmt = null;

    AspSmallStmt(int n) {
	    super(n);
    }

    public static AspSmallStmt parse(Scanner s) {
        enterParser("small stmt");

        AspSmallStmt ss = new AspSmallStmt(s.curLineNum());
        if(s.curToken().kind == TokenKind.globalToken) {
            ss.globalStmt = AspGlobalStmt.parse(s);
        } else if (s.curToken().kind == TokenKind.passToken) {
            ss.passStmt = AspPassStmt.parse(s);
        } else if(s.curToken().kind == TokenKind.returnToken) {
            ss.returnStmt = AspReturnStmt.parse(s);
        } else if (s.anyEqualToken()) {
            ss.assignment = AspAssignment.parse(s);
        } else {
            ss.exprStmt = AspExprStmt.parse(s);
        }

        leaveParser("small stmt");
        return ss;
    }


    @Override
    void prettyPrint() {
        if(globalStmt != null) {
            globalStmt.prettyPrint();
        } else if (passStmt != null) {
            passStmt.prettyPrint();
        } else if(returnStmt != null) {
            returnStmt.prettyPrint();
        } else if (assignment != null) {
            assignment.prettyPrint();
        } else {
            exprStmt.prettyPrint();
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("small stmt");
        if (assignment != null)
            return assignment.eval(curScope);
        if (exprStmt != null)
            return exprStmt.eval(curScope);
        if (globalStmt != null)
            return globalStmt.eval(curScope);
        if (passStmt != null)
            return passStmt.eval(curScope);
        if (returnStmt != null)
            return returnStmt.eval(curScope);
        return null;
    }
}