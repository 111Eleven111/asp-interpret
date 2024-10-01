package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspFuncDef;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;

public class RuntimeFuncValue extends RuntimeValue {
    AspFuncDef def;
    RuntimeScope defScope;
    public String name;

    public RuntimeFuncValue(String name) {
        this.name = name;
    }

    public RuntimeFuncValue(AspFuncDef def, String name) {
        this.def = def;
        this.name = name;
    }

    public RuntimeFuncValue(AspFuncDef def, String name, RuntimeScope outer) {
        this.def = def;
        this.name = name;
        this.defScope = outer;
    }

    @Override
    public String typeName() {
        return "func";
    }

    @Override
    public String toString() {
        return "" + name + ""; // ()???
    }
    
    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actPars,
    AspSyntax where) {
        RuntimeValue actParsRuntimeList = actPars.get(0); // Workaround? actPars er lista av liste, men indre liste er alle argumentene
        actPars = actParsRuntimeList.getArguments("argument list", where);
        // Del 3 sjekke formelle og aktuelle parametere
        if (def.argument_names.size() != actPars.size()) {
            // Error, actPars lenght does not match with amount of defined arguments!
            // Better error message?
            runtimeError("Unexpected amount of arguments: " + def.argument_names + " --!=" + actPars, where);
        }

        // Del 4
        RuntimeScope newScope = new RuntimeScope(defScope);

        // Del 5 Initiere parameterene
        for (int i = 0; i < actPars.size(); i ++) {
            String paramName = def.argument_names.get(i).nameLiteralString;
            newScope.assign(paramName, actPars.get(i));
        }

        
        
        // Slides / prekode:
        try {
            def.suite.eval(newScope);
        } catch (RuntimeReturnValue rrv) {
            return rrv.value;
        }
        return new RuntimeNoneValue();
    }
}