package org.gmjm.nashorn;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.gmjm.script.Script;
import org.gmjm.script.ScriptFactory;

public class ScriptFactoryImpl implements ScriptFactory
{

	private ContextWrapper contextWrapper;

	public ScriptFactoryImpl(ContextWrapper contextWrapper) {
		this.contextWrapper = contextWrapper;
	}

	public ScriptFactoryImpl() {
	}


	public Script load(String name, URI uri) throws IOException
	{
		if(contextWrapper == null) {
			return new SimpleScript(name,IOUtils.toString(uri));
		} else
		{
			return contextWrapper.wrap(name, new SimpleScript(name, IOUtils.toString(uri)));
		}
	}

	public Script load(String name, String body)
	{
		if(contextWrapper == null) {
			return new SimpleScript(name,body);
		} else
		{
			return contextWrapper.wrap(name, new SimpleScript(name, body));
		}
	}
}
