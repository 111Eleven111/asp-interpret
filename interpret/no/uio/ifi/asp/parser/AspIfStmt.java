package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIfStmt extends AspSyntax {
    ArrayList<AspExpr> exprList = new ArrayList<>();
    ArrayList<AspSuite> suiteList = new ArrayList<>();
    AspSuite elseSuite = null;

    AspIfStmt(int n) {
        super(n);
    }

    public static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");

        AspIfStmt ais = new AspIfStmt(s.curLineNum());

        skip(s, TokenKind.ifToken);
        ais.exprList.add(AspExpr.parse(s));
        skip(s, TokenKind.colonToken);
        ais.suiteList.add(AspSuite.parse(s));

        while(s.curToken().kind == TokenKind.elifToken) {
            ais.exprList.add(AspExpr.parse(s));
            skip(s, TokenKind.colonToken);
            ais.suiteList.add(AspSuite.parse(s));
        }

        if(s.curToken().kind == TokenKind.elseToken) {
            skip(s, TokenKind.elseToken);
            skip(s, TokenKind.colonToken);
            ais.elseSuite = AspSuite.parse(s);
        }

        leaveParser("if stmt");
        return ais;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("if ");

        for(int i = 0; i < exprList.size(); i++) {
            exprList.get(i).prettyPrint();
            prettyWrite(": ");
            suiteList.get(0).prettyPrint();
            if(i < exprList.size() - 1) {
                prettyWrite("elif ");
            }
        }

        if(elseSuite != null) {
            prettyWrite("else");
            prettyWrite(": ");
            elseSuite.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("if stmt");
        int i = 0;
        for(AspExpr expr : exprList) {
            if(expr.eval(curScope).getBoolValue("if expr", this)) {
                suiteList.get(i).eval(curScope);
                return null;
            } 
            i++;
        }
        if(elseSuite != null) {
            elseSuite.eval(curScope);
        }
        return null;
    }
}