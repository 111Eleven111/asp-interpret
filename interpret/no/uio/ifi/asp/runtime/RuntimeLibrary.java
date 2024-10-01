// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
	    // Part 4:
        // len
        assign("len", new RuntimeFuncValue("len") {
            @Override
            public RuntimeValue evalFuncCall(
            ArrayList<RuntimeValue> actPars,
            AspSyntax where) {

                RuntimeValue actParsRuntimeList = actPars.get(0); // Workaround? actPars er lista av liste, men indre liste er alle argumentene
                actPars = actParsRuntimeList.getArguments("argument list", where);

                checkNumParams(actPars, 1, "len", where);
                return actPars.get(0).evalLen(where);
            }
        });
            
        // print
        assign("print", new RuntimeFuncValue("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actPars,
            AspSyntax where) {
                
                RuntimeValue actParsRuntimeList = actPars.get(0); // Workaround? actPars er lista av liste, men indre liste er alle argumentene
                actPars = actParsRuntimeList.getArguments("argument list", where);

                for (int i = 0; i < actPars.size(); ++i) {
                    if (i > 0) System.out.print(" ");
                    System.out.print(actPars.get(i).toString());
                }
                System.out.println();
                return new RuntimeNoneValue();
            }
        });

        // str
        assign("str", new RuntimeFuncValue("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actPars,
            AspSyntax where) {

                RuntimeValue actParsRuntimeList = actPars.get(0); // Workaround? actPars er lista av liste, men indre liste er alle argumentene
                actPars = actParsRuntimeList.getArguments("argument list", where);

                checkNumParams(actPars, 1, "str", where);
                return new RuntimeStringValue(actPars.get(0).toString());
            }
        });

        // input
        assign("input", new RuntimeFuncValue("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actPars,
            AspSyntax where) {

                RuntimeValue actParsRuntimeList = actPars.get(0); // Workaround? actPars er lista av liste, men indre liste er alle argumentene
                actPars = actParsRuntimeList.getArguments("argument list", where);

                checkNumParams(actPars, 1, "input", where);

                if(actPars.get(0) instanceof RuntimeStringValue)
                    System.out.println(actPars.get(0).getStringValue("input", where));
                else 
                    RuntimeValue.runtimeError("Argument to input-function must be of type string!",where);

                String val = keyboard.nextLine();
                RuntimeStringValue v = new RuntimeStringValue(val);
                return v;
            }
        });

        // float
        assign("float", new RuntimeFuncValue("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actPars,
            AspSyntax where) {

                RuntimeValue actParsRuntimeList = actPars.get(0); // Workaround? actPars er lista av liste, men indre liste er alle argumentene
                actPars = actParsRuntimeList.getArguments("argument list", where);

                checkNumParams(actPars, 1, "float", where);
                double v = actPars.get(0).getFloatValue("float conv", where);
                return new RuntimeFloatValue(v);
            }
        });
        
        // int
        assign("int", new RuntimeFuncValue("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actPars,
            AspSyntax where) {


                
                RuntimeValue actParsRuntimeList = actPars.get(0); // Workaround? actPars er lista av liste, men indre liste er alle argumentene
                actPars = actParsRuntimeList.getArguments("argument list", where);

                checkNumParams(actPars, 1, "int", where);
                long v = actPars.get(0).getIntValue("int conv", where);
                return new RuntimeIntValue(v);
            }
        });
        
        // range
        assign("range", new RuntimeFuncValue("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actPars,
            AspSyntax where) {

                RuntimeValue actParsRuntimeList = actPars.get(0); // Workaround? actPars er lista av liste, men indre liste er alle argumentene
                actPars = actParsRuntimeList.getArguments("argument list", where);

                checkNumParams(actPars, 2, "range", where);

                ArrayList<RuntimeValue> numbers = new ArrayList<>();
                for (long i = actPars.get(0).getIntValue("range int start", where); i < actPars.get(1).getIntValue("range int end", where); i ++) {
                    numbers.add(new RuntimeIntValue(i));
                }
                
                return new RuntimeListValue(numbers);
            }
        });
    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs, 
				int nCorrect, String id, AspSyntax where) {

                    // RuntimeValue actArgsRuntimeList = actArgs.get(0); // Workaround? actPars er lista av liste, men indre liste er alle argumentene
                    // actArgs = actArgsRuntimeList.getArguments("argument list", where);
                    if (actArgs.size() != nCorrect)
        RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}
