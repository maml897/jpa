package junit.test;

// J2SE imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;

public class MyPersistence
{

	public static String PERSISTENCE_PROVIDER = "javax.persistence.spi.PeristenceProvider";

	protected static final Set<PersistenceProvider> providers = new HashSet<PersistenceProvider>();

	/**
	 * Create and return an EntityManagerFactory for the named persistence unit.
	 * @param persistenceUnitName
	 *            The name of the persistence unit
	 * @return The factory that creates EntityManagers configured according to
	 *         the specified persistence unit
	 */
	public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName)
	{
		return createEntityManagerFactory(persistenceUnitName, null);
	}

	/**
	 * Create and return an EntityManagerFactory for the named persistence unit
	 * using the given properties.
	 * @param persistenceUnitName
	 *            The name of the persistence unit
	 * @param properties
	 *            Additional properties to use when creating the factory. The
	 *            values of these properties override any values that may have
	 *            been configured elsewhere.
	 * @return The factory that creates EntityManagers configured according to
	 *         the specified persistence unit.
	 */
	public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties)
	{
		EntityManagerFactory emf = null;
		System.out.println("提供者数量："+providers.size());
		
		if (providers.size() == 0)
		{
			try
			{
				findAllProviders();
			}
			catch (IOException exc)
			{
			}
			;
		}
		
		System.out.println("搜索之后提供者数量："+providers.size());
		for (PersistenceProvider provider : providers)
		{
			emf = provider.createEntityManagerFactory(persistenceUnitName, properties);
			if (emf != null)
			{
				break;
			}
		}
		if (emf == null)
		{
			throw new PersistenceException("No Persistence provider for EntityManager named " + persistenceUnitName);
		}
		return emf;
	}

	// Helper methods

	private static void findAllProviders() throws IOException
	{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		System.out.println("提供者的名字："+PersistenceProvider.class.getName());
		Enumeration<URL> resources = loader.getResources("META-INF/services/" + PersistenceProvider.class.getName());
		Set<String> names = new HashSet<String>();
		while (resources.hasMoreElements())
		{
			URL url = resources.nextElement();
			InputStream is = url.openStream();
			try
			{
				names.addAll(providerNamesFromReader(new BufferedReader(new InputStreamReader(is))));
			}
			finally
			{
				is.close();
			}
		}
		for (String s : names)
		{
			try
			{
				providers.add((PersistenceProvider) loader.loadClass(s).newInstance());
			}
			catch (ClassNotFoundException exc)
			{
			}
			catch (InstantiationException exc)
			{
			}
			catch (IllegalAccessException exc)
			{
			}
		}
	}

	private static final Pattern nonCommentPattern = Pattern.compile("^([^#]+)");

	private static Set<String> providerNamesFromReader(BufferedReader reader) throws IOException
	{
		Set<String> names = new HashSet<String>();
		String line;
		while ((line = reader.readLine()) != null)
		{
			line = line.trim();
			Matcher m = nonCommentPattern.matcher(line);
			if (m.find())
			{
				names.add(m.group().trim());
			}
		}
		return names;
	}
}
