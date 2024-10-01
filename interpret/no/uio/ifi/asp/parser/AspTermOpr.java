package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;

public class AspTermOpr extends AspSyntax {
    Boolean plus;

    AspTermOpr(int n) {
        super(n);
    }
    
    public static AspTermOpr parse(Scanner s) {
        enterParser("term opr");
        AspTermOpr ato = new AspTermOpr(s.curLineNum());

        while (true) {
            if (s.curToken().kind != plusToken && s.curToken().kind != minusToken)
                break;
            if (s.curToken().kind == plusToken) {
                ato.plus = true;
                skip(s, plusToken);
            }
            else {
                ato.plus = false;
                skip(s, minusToken);
            }
        }

        leaveParser("term opr");
        return ato;
    }

    @Override
    public void prettyPrint() {
        if(this.plus) {
            prettyWrite("+");
        } else {
            prettyWrite("-");
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4
        trace("TermOpr");
        // TODO might be redundant
        return null;
    }
}
