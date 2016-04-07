package org.gmjm.nashorn;

import java.util.Arrays;
import java.util.List;

import org.gmjm.script.Script;

public class ContextWrapper
{
	private List<String> contextArgs;
	private Script preSnippet;
	private Script postSnippet;

	private static final String CONTEXT_WRAP_TEMPLATE =
		"this.%s = function(%s){\n" +
			"%s\n" +
		"}";

	private static final String CONTEXT_WRAP_TEMPLATE_WITH_RETURN =
		"this.%s = function(%s){\n" +
			"%s\n"+
			"%s\n" +
			"%s\n"+
			"}";


	public ContextWrapper(String ... contextArgs)
	{
		this.contextArgs = Arrays.asList(contextArgs);
	}

	public ContextWrapper(Script preSnippet, Script postSnippet, String ... contextArgs)
	{
		this.contextArgs = Arrays.asList(contextArgs);
		this.preSnippet = preSnippet;
		this.postSnippet = postSnippet;
	}

	private String printArgs()
	{

		if(contextArgs.size() == 0)
			return "";

		if(contextArgs.size() == 1)
			return contextArgs.get(0);

		StringBuilder sb = new StringBuilder();

		contextArgs.stream()
			.limit(contextArgs.size() - 1)
			.forEach(arg -> sb.append(arg).append(", "));

		if(contextArgs.size() > 1)
			sb.append(contextArgs.get(contextArgs.size()-1));


		return sb.toString();
	}

	public Script wrap(String functionName, Script script) {

		return new SimpleScript(
				functionName,
				String.format(CONTEXT_WRAP_TEMPLATE_WITH_RETURN,
					functionName,
					printArgs(),
					preSnippet == null ? "" : preSnippet.getBody(),
					script.getBody(),
					postSnippet == null ? "" : postSnippet.getBody()));

	}

	public List<String> getContextArgs()
	{
		return contextArgs;
	}
}
