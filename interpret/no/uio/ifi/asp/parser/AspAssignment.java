package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAssignment extends AspSyntax {
    ArrayList<AspSubscription> subscriptions = new ArrayList<>();
    AspExpr expr;
    AspName name;
    

    AspAssignment(int n) {
	    super(n);
    }


    public static AspAssignment parse(Scanner s) {
        enterParser("assignment");

        AspAssignment a = new AspAssignment(s.curLineNum());

        a.name = AspName.parse(s);

        while (s.curToken().kind == leftBracketToken) {
            a.subscriptions.add(AspSubscription.parse(s));
        }
        skip(s, equalToken);
        a.expr = AspExpr.parse(s);

        leaveParser("assignment");
        return a;
    }


    @Override
    public void prettyPrint() {
	// Part 2:
        name.prettyPrint();

        for(AspSubscription as : subscriptions){
            as.prettyPrint();
        }
        
        prettyWrite(" = ");
        expr.prettyPrint();
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Part 4:
        trace("assignment");
        // Subscriptions will be empty if we perform a new declaration on the form: val = value
        RuntimeValue v = expr.eval(curScope);
        if(subscriptions.size() == 0) {
            String n = name.nameLiteralString;
            curScope.assign(n, v);
            return null;
        }

        RuntimeValue a = name.eval(curScope);
        trace("assigment2");        

        if (a instanceof RuntimeListValue || a instanceof RuntimeDictValue) {
            
            int i = 0;
            if (subscriptions.size() > 1) {
                for (AspSubscription as : subscriptions) {
                    RuntimeValue index = as.eval(curScope);
                    a = a.evalSubscription(index, this);
                    if(i < subscriptions.size() - 1) break;
                    i++;
                }
            }
            RuntimeValue lastIndex = subscriptions.get(subscriptions.size() - 1).eval(curScope);
            a.evalAssignElem(lastIndex, v, this);
            return null;
        }

        return null;
    }
}