package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;

import no.uio.ifi.asp.runtime.*;

public class AspName extends AspAtom {
    public String nameLiteralString = "";

    AspName(int n) {
	    super(n);
    }

    public static AspName parse(Scanner s) {
        // Part 2:
        enterParser("name");

        AspName ae = new AspName(s.curLineNum());
        ae.nameLiteralString = s.curToken().name;
        skip(s, TokenKind.nameToken);
        
        leaveParser("name");
        return ae;
    }

    @Override
    void prettyPrint() {
        prettyWrite(nameLiteralString);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    // Part 3:
        trace("name");
        // return new RuntimeStringValue(nameLiteralString);
        RuntimeValue v = curScope.find(nameLiteralString, this);
        if(v != null) {
            return v;
        }
        return new RuntimeStringValue(nameLiteralString);
    }
}
