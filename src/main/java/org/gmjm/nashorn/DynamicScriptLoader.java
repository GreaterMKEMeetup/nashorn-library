package org.gmjm.nashorn;

import java.lang.ref.WeakReference;
import java.util.function.Function;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.gmjm.script.Script;
import org.gmjm.script.ScriptLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;


public class DynamicScriptLoader implements ScriptLoader
{
	private static final Logger logger = LoggerFactory.getLogger(DynamicScriptLoader.class);

	NashornScriptEngineFactory scriptEngineFactory;
	ScriptEngine se;

	public DynamicScriptLoader() {
		this.scriptEngineFactory = new NashornScriptEngineFactory();
		reset();
	}

	@Override
	public void evalOnly(Script script) throws ScriptException
	{
		String toEval = script.getBody();

		logger.debug("Loading: \n" + toEval);

		System.out.println(("Loading: \n" + toEval));

		se.eval(toEval);
	}

	@Override
	public <R> Function<Object[],R> load(Script script) throws ScriptException
	{
		String toEval = script.getBody();

		logger.debug("Loading: \n" + toEval);

		System.out.println(("Loading: \n" + toEval));

		se.eval(toEval);

		//Weak reference
		WeakReference<Invocable> invocableWeakRef = new WeakReference<Invocable>((Invocable)se);

		return (args) -> {
			try
			{
				Invocable inv = invocableWeakRef.get();

				if(inv == null)
				{
					throw new RuntimeException("The underlying script engine for this function has been garbage collected.");
				}

				return (R)inv.invokeFunction(script.getName(),args);
			}
			catch (Exception e)
			{
				throw new RuntimeException("Failed to invoke script function.",e);
			}
		};
	}

	@Override
	public boolean functionLoaded(String name){
		Object o = se.get(name);

		return o != null;
	}

	public void reset(){
		se = this.scriptEngineFactory.getScriptEngine();
	}
}
