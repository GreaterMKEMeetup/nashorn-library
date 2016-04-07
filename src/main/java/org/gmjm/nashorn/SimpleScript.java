package org.gmjm.nashorn;


import org.gmjm.script.Script;

public class SimpleScript implements Script
{
	private final String name;
	private final String body;


	public SimpleScript(String name, String body)
	{
		this.name = name;
		this.body = body;
	}


	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getBody()
	{
		return body;
	}


}
