package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeDictValue extends RuntimeValue {
    Dictionary<String, RuntimeValue> dict = new Hashtable<>();

    public RuntimeDictValue(Dictionary<String, RuntimeValue> dict) {
        this.dict = dict;
    }

    @Override
    public String typeName() {
        return "Dictionary";
    }

    @Override
    public String toString() {
        String s = "";
        s += "{ ";

        int i = 0;
        Enumeration<String> keys = dict.keys();
        while (keys.hasMoreElements()) {
            String k = keys.nextElement();
            s += "'" + k + "'";
            s += " : ";
            s += dict.get(k);
            if (i < dict.size() - 1) {
                s += " , ";
            }
            i++;
        }
        s += " }";
        return s;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (!dict.isEmpty());
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for dict ==.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(dict.isEmpty());
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            String key = v.getStringValue("subscription", where);

            if (dict.get(key) instanceof RuntimeIntValue) {
                return new RuntimeIntValue(dict.get(key).getIntValue("int from evalSubscription", where));
            } else if (dict.get(key) instanceof RuntimeFloatValue) {
                return new RuntimeFloatValue(dict.get(key).getFloatValue("float from evalSubscription", where));
            } else if (dict.get(key) instanceof RuntimeStringValue) {
                return new RuntimeStringValue(dict.get(key).getStringValue("string from evalSubscription", where));
            } else if (dict.get(key) instanceof RuntimeBoolValue) {
                return new RuntimeBoolValue(dict.get(key).getBoolValue("bool from evalSubscription", where));
            }
        }
        runtimeError("Subscription '[...]' undefined for " + typeName() + "!", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(dict.size());
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        System.out.println("TET/SYGSDHUIASJio");
        if (inx instanceof RuntimeStringValue) {
            dict.put(inx.getStringValue("add to dict", where), val);
            return;
        }
	    runtimeError("Not a valid index: "+inx.typeName()+"!", where);
    }
}
