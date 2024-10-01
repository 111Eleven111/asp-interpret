package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeListValue extends RuntimeValue {
    public ArrayList<RuntimeValue> list = new ArrayList<>();

    public RuntimeListValue(ArrayList<RuntimeValue> list) {
        this.list = list;
    }

    @Override
    public String typeName() {
        return "List lol";
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (!list.isEmpty());
    }

    // Workaround
    @Override
    public ArrayList<RuntimeValue> getArguments(String what, AspSyntax where) {
        return list;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for list ==.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(list.isEmpty());
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            ArrayList<RuntimeValue> newList = new ArrayList<>();
            for (int i = 0; i < v.getIntValue("* operand", where); i++) {
                for (RuntimeValue value : list) {
                    newList.add(value);
                }
            }
            return new RuntimeListValue(newList);
        }
        runtimeError("Type error for *.", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(list.size());
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            Long index = v.getIntValue("subscription", where);
            Integer i = index == null ? null : Math.toIntExact(index);

            if (list.get(i) instanceof RuntimeIntValue) {
                return new RuntimeIntValue(list.get(i).getIntValue("int from evalSubscription", where));
            } else if (list.get(i) instanceof RuntimeFloatValue) {
                return new RuntimeFloatValue(list.get(i).getFloatValue("float from evalSubscription", where));
            } else if (list.get(i) instanceof RuntimeStringValue) {
                return new RuntimeStringValue(list.get(i).getStringValue("string from evalSubscription", where));
            } else if (list.get(i) instanceof RuntimeBoolValue) {
                return new RuntimeBoolValue(list.get(i).getBoolValue("bool from evalSubscription", where));
            }
        }

        runtimeError("Subscription '[...]' undefined for " + typeName() + "!", where);
        return null; // Required by the compiler!
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        if (inx instanceof RuntimeIntValue) {
            int index;
            try {
                index = (int) inx.getIntValue("list eval assign", where);
            } catch (Exception e) {
                runtimeError("Error while converting index value for list", where);
                index = 0;
            }
            list.set(index, val);
            //list.add((int) inx.getIntValue("add to list", where), val);
            return;
        }
	    runtimeError("Not a valid index: "+inx.typeName()+"!", where);
    }
}
