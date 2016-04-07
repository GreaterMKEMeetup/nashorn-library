import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.gmjm.nashorn.ContextWrapper;
import org.gmjm.nashorn.DynamicScriptLoader;
import org.gmjm.nashorn.ScriptFactoryImpl;
import org.gmjm.script.Script;
import org.gmjm.script.ScriptFactory;
import org.gmjm.script.ScriptLoader;
import org.junit.Before;
import org.junit.Test;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import static junit.framework.TestCase.*;



public class NashornTest
{

	NashornScriptEngineFactory nashornScriptEngineFactory = new NashornScriptEngineFactory();

	ScriptEngine scriptEngine;

	@Before
	public void setup() {

		scriptEngine = nashornScriptEngineFactory.getScriptEngine();

	}



	@Test
	public void simpleTest() throws ScriptException, NoSuchMethodException, IOException, URISyntaxException
	{

		Message message = new Message("Are you serious?",new Date(),"Jimmy","Frank");

		URI script1 = getScriptUri("hello-message-processor.js");

		ContextWrapper wrapper = new ContextWrapper("message");

		ScriptFactory scriptFactory = new ScriptFactoryImpl(wrapper);

		Script helloScript = scriptFactory.load("hello",script1);



		ScriptLoader loader = new DynamicScriptLoader();

		Function<Object[],String> func1 = loader.load(helloScript);

		String response = func1.apply(new Object[]{message});

		System.out.println(response);


		assertTrue(loader.functionLoaded("hello"));
	}

	@Test
	public void loadingIsFastTest() throws ScriptException, NoSuchMethodException, IOException, URISyntaxException
	{

		Message message = new Message("Are you serious?",new Date(),"Jimmy","Frank");

		URI script1 = getScriptUri("hello-format-date.js");

		ContextWrapper wrapper = new ContextWrapper("message","dateFormatter");

		ScriptFactory scriptFactory = new ScriptFactoryImpl(wrapper);

		Script helloScript = scriptFactory.load("hello",script1);



		ScriptLoader loader = new DynamicScriptLoader();



		SimpleDateFormat dateFormatter = new SimpleDateFormat(("MMM dd, yyyy"));

		String response = null;
		for(int i = 0; i < 10; i++)
		{
			Function<Object[],String> func1 = loader.load(helloScript);
			message.setMessage("iteration: " + i);
			message.setDate(new Date());
			response = func1.apply(new Object[]{message});
			System.out.println(response);
		}



		assertTrue(loader.functionLoaded("hello"));
		assertFalse(loader.functionLoaded("dateFormatter")); // inner scope variables aren't accessable
	}


	@Test
	public void multiVarivableTest() throws ScriptException, NoSuchMethodException, IOException, URISyntaxException
	{

		Message message = new Message("Finish this stuff!",new Date(),"Jimmy","Frank");

		URI script1 = getScriptUri("multi-args.js");

		ContextWrapper wrapper = new ContextWrapper("message","tasks","managerName");

		ScriptFactory scriptFactory = new ScriptFactoryImpl(wrapper);

		Script helloScript = scriptFactory.load("tasks",script1);



		ScriptLoader loader = new DynamicScriptLoader();


		Function<Object[],String> func1 = loader.load(helloScript);

		String[] tasks = new String[]{"Wash floor", "Clean toilet", "Do Laundry"};
		String managerName = "Bruce";

		String response = func1.apply(new Object[]{message,tasks,managerName});

		System.out.println(response);




		assertTrue(loader.functionLoaded("tasks"));
	}

	@Test
	public void prePostTest() throws ScriptException, NoSuchMethodException, IOException, URISyntaxException
	{

		Message message = new Message("Finish this stuff!",new Date(),"Jimmy","Frank");

		URI script1 = getScriptUri("multi-args-prepost.js");
		URI pre = getScriptUri("pre.js");
		URI post = getScriptUri("post.js");

		ScriptFactory basicScriptFactory = new ScriptFactoryImpl();
		Script preScript = basicScriptFactory.load("pre",pre);
		Script postScriptResource = basicScriptFactory.load("post",post);

		ContextWrapper wrapper = new ContextWrapper(preScript,postScriptResource,"message","tasks","managerName","messageFormat");

		ScriptFactory wrapperScriptFactory = new ScriptFactoryImpl(wrapper);

		Script helloScript = wrapperScriptFactory.load("tasks",script1);



		ScriptLoader loader = new DynamicScriptLoader();


		Function<Object[],String> func1 = loader.load(helloScript);

		String[] tasks = new String[]{"Wash floor", "Clean toilet", "Do Laundry"};
		String managerName = "Bruce";

		String response = func1.apply(new Object[]{message,tasks,managerName,"MMM dd, yyyy hh:mm:ss:SSS"});

		System.out.println(response);


		assertTrue(loader.functionLoaded("tasks"));
	}


	@Test
	public void useHandlebarsCompiled() throws ScriptException, NoSuchMethodException, IOException, URISyntaxException
	{

		Message message = new Message("Finish this stuff!",new Date(),"Jimmy","Frank");


		ScriptFactory basicScriptFactory = new ScriptFactoryImpl();
		Script handleBars = basicScriptFactory.load("handlebars",new URI("https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.5/handlebars.min.js"));


		ScriptFactory wrappedScriptFactory = new ScriptFactoryImpl(new ContextWrapper("template,message,tasks"));
		Script handleBarsHello = wrappedScriptFactory.load("hello",getScriptUri("handleBarsHelloCompiled.js"));
		Script compileTemplate = wrappedScriptFactory.load("compileTemplate",getScriptUri("handlebarCompile.js"));


		ScriptLoader loader = new DynamicScriptLoader();

		loader.evalOnly(handleBars);

		Function<Object[],Object> compile = loader.load(compileTemplate);
		Function<Object[],String> func1 = loader.load(handleBarsHello);

		String[] tasks = new String[]{"Wash floor", "Clean toilet", "Do Laundry"};

		String template = IOUtils.toString(getScriptUri("template.html"));

		Object compiledTemplate = compile.apply(new Object[]{template});

		String response = func1.apply(new Object[]{compiledTemplate,message,tasks});
		System.out.println(response);

	}

	URI getScriptUri(String scriptName) throws URISyntaxException
	{
		return getClass().getResource(scriptName).toURI();
	}

}
