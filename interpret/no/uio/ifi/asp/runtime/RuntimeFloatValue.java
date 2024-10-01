package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue {
    double floatValue;

    public RuntimeFloatValue(double v) {
        floatValue = v;
    }

    @Override
    public String typeName() {
        return "float";
    }

    @Override
    public String toString() {
        return "" + floatValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return floatValue;
    }

    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeFloatValue(floatValue);
    }

    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(-floatValue);
    }

    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue + v.getIntValue("+ operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue + v.getFloatValue("+ operand", where));
        }
        runtimeError("Type error for +.", where);
        return null; // Required by the compiler!
    }

    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue - v.getIntValue("- operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue - v.getFloatValue("- operand", where));
        }
        runtimeError("Type error for -.", where);
        return null; // Required by the compiler!
    }

    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue * v.getIntValue("* operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue * v.getFloatValue("* operand", where));
        }
        runtimeError("Type error for *.", where);
        return null; // Required by the compiler!
    }

    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue / v.getIntValue("/ operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue / v.getFloatValue("/ operand", where));
        }
        runtimeError("Type error for /.", where);
        return null; // Required by the compiler!
    }

    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(Math.floor(floatValue / v.getIntValue("// operand", where)));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue("// operand", where)));
        }
        runtimeError("Type error for //.", where);
        return null; // Required by the compiler!
    }

    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue - v.getIntValue("% operand", where)
                    * Math.floor(floatValue / v.getIntValue("// operand", where)));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue - v.getFloatValue("% operand", where)
                    * Math.floor(floatValue / v.getFloatValue("// operand", where)));
        }
        runtimeError("Type error for //.", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        } else if (v instanceof RuntimeFloatValue) {
            if (floatValue == v.getFloatValue("== operator", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (floatValue == v.getIntValue("== operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for float ==.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            if (floatValue != v.getIntValue("!= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeFloatValue) {
            if (floatValue != v.getFloatValue("!= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for !=.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            if (floatValue < v.getIntValue("< operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeFloatValue) {
            if (floatValue < v.getFloatValue("< operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for <.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            if (floatValue <= v.getIntValue("<= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeFloatValue) {
            if (floatValue <= v.getFloatValue("<= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for <=.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            if (floatValue > v.getIntValue("> operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeFloatValue) {
            if (floatValue > v.getFloatValue("> operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for >.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            if (floatValue >= v.getIntValue(">= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeFloatValue) {
            if (floatValue >= v.getFloatValue(">= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for >=.", where);
        return null; // Required by the compiler
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (floatValue != 0.0);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if (floatValue == 0.0) {
            return new RuntimeBoolValue(true);
        } else {
            return new RuntimeBoolValue(false);
        }
    }
}
