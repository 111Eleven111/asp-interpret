// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

abstract class AspStmt extends AspSyntax {
    static AspSyntax temp;
    static ArrayList<TokenKind> legalCompoundTokens = new ArrayList<TokenKind>();

    AspStmt(int n) {
        super(n);
    }

    static AspStmt parse(Scanner s) {
	    // Part 2:
        enterParser("stmt");
        legalCompoundTokens.add(forToken);
        legalCompoundTokens.add(ifToken);
        legalCompoundTokens.add(whileToken);
        legalCompoundTokens.add(defToken);
        AspStmt stmt = null;

        if (legalCompoundTokens.contains(s.curToken().kind)) {
            stmt = AspCompoundStmt.parse(s);
            leaveParser("stmt");
            return stmt;
        }

        stmt = AspSmallStmtList.parse(s);
        temp = stmt;
        leaveParser("stmt");
	    return stmt;
    }

    @Override
    void prettyPrint() {
        temp.prettyPrint();
    }
}
