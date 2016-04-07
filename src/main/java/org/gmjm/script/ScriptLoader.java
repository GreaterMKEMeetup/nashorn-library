package org.gmjm.script;

import java.util.function.Function;

import javax.script.ScriptException;

public interface ScriptLoader
{
	<R> Function<Object[],R> load(Script script) throws ScriptException;
	void evalOnly(Script script) throws ScriptException;
	boolean functionLoaded(String name);

}
