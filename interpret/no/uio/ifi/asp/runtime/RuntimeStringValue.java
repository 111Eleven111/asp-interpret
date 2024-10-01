package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Arrays;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue {
    String stringValue = "";

    public RuntimeStringValue(String v) {
        stringValue = v;
    }

    @Override
    public String showInfo() {
        if (stringValue.indexOf('\'') >= 0) {
            return '"' + stringValue + '"';
        } else {
            return "'" + stringValue + "'";
        }
    }

    @Override
    public String typeName() {
        return "string";
    }

    @Override
    public String toString() {
        return stringValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (!stringValue.equals(""));
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return stringValue;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        try {
            return Long.parseLong(stringValue);
        } catch (NumberFormatException e) {
            runtimeError("Could not convert to int:" + what + " --- ", where); // Stop runtime
            return 0;
        }
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        try {
            return Double.parseDouble(stringValue);
        } catch (NumberFormatException e) {
            runtimeError("Could not convert string to float, "+ what + ".", where); // Stop runtime
            return 0;
        }
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(stringValue.length());
    }

    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeStringValue(stringValue + v.getStringValue("evalAdd for RuntimeStringValue", where));
        }
        runtimeError("Type error for +.", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            String s = "";
            for (int i = 0; i < v.getIntValue("* operand", where); i++) {
                s += stringValue;
            }
            return new RuntimeStringValue(s);
        }
        runtimeError("Type error for *.", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(stringValue.equals(v.getStringValue("== operand", where)));
        } else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for string ==.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (stringValue.compareTo(v.getStringValue("< operand", where)) < 0)
                return new RuntimeBoolValue(true);
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for <.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (stringValue.compareTo(v.getStringValue("<= operand", where)) <= 0)
                return new RuntimeBoolValue(true);
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for <=.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (stringValue.compareTo(v.getStringValue("> operand", where)) > 0)
                return new RuntimeBoolValue(true);
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for >.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (stringValue.compareTo(v.getStringValue(">= operand", where)) >= 0)
                return new RuntimeBoolValue(true);
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for >=.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if (!stringValue.equals("")) {
            return new RuntimeBoolValue(true);
        } else {
            return new RuntimeBoolValue(false);
        }
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            Long index = v.getIntValue("subscription", where);
            Integer i = index == null ? null : Math.toIntExact(index);

            String s = "";
            if (i.intValue() > stringValue.length()) {
                runtimeError("Char index of " + i.intValue() + " is out of bounds for " + stringValue, where);
                return null;
            }
            s += stringValue.charAt(i);
            return new RuntimeStringValue(s);
        }
        runtimeError("Subscription '[...]' undefined for " + typeName() + "!", where);
        return null; // Required by the compiler!
    }
}
