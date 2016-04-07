package org.gmjm.script;

import java.io.IOException;
import java.net.URI;

public interface ScriptFactory
{
	Script load(String name, URI uri) throws IOException;
	Script load(String name, String body);
}
