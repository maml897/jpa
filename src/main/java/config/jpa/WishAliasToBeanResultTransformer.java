package config.jpa;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.property.access.internal.PropertyAccessStrategyBasicImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyChainedImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyMapImpl;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

public class WishAliasToBeanResultTransformer extends AliasedTupleSubsetResultTransformer
{
	private static final long serialVersionUID = -3338220755045186624L;

	private final Class<?> resultClass;

	private boolean isInitialized;

	private String[] aliases;

	private Setter[] setters;

	public WishAliasToBeanResultTransformer(Class<?> resultClass)
	{
		if (resultClass == null)
		{
			throw new IllegalArgumentException("resultClass cannot be null");
		}
		isInitialized = false;
		this.resultClass = resultClass;
	}

	@Override
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength)
	{
		return false;
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases)
	{
		Object result;

		try
		{
			if (!isInitialized)
			{
				initialize(aliases);
			}
			else
			{
				check(aliases);
			}

			result = resultClass.newInstance();

			for (int i = 0; i < aliases.length; i++)
			{
				if (setters[i] != null)
				{
					if(tuple[i]!=null){
						if (tuple[i].getClass() == java.math.BigInteger.class)
						{
							
								tuple[i] = Long.valueOf(((BigInteger) tuple[i]).longValue());
							
						}
					}
					setters[i].set(result, tuple[i], null);
				}
			}
		}
		catch (InstantiationException e)
		{
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		}
		catch (IllegalAccessException e)
		{
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		}

		return result;
	}

	private void initialize(String[] aliases)
	{
		PropertyAccessStrategyChainedImpl propertyAccessStrategy = new PropertyAccessStrategyChainedImpl(PropertyAccessStrategyBasicImpl.INSTANCE, PropertyAccessStrategyFieldImpl.INSTANCE,
				PropertyAccessStrategyMapImpl.INSTANCE);

		Map<String, ClassFields> allFields = getAllFields();

		this.aliases = new String[aliases.length];
		setters = new Setter[aliases.length];
		for (int i = 0; i < aliases.length; i++)
		{
			String alias = aliases[i];
			if (alias != null)
			{
				this.aliases[i] = alias;
				ClassFields classFields = allFields.get(alias.toLowerCase());
				String tmpAlias = alias;
				if (classFields != null)
					tmpAlias = classFields.getField().getName();
				setters[i] = propertyAccessStrategy.buildPropertyAccess(resultClass, tmpAlias).getSetter();
			}
		}
		isInitialized = true;
	}

	private Map<String, ClassFields> getAllFields()
	{
		Map<String, ClassFields> annofields = new HashMap<>();
		Field[] fields = resultClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			String key = fields[i].getName().toLowerCase();

			if (fields[i].isAnnotationPresent(ColumnWish.class))
			{
				ColumnWish nqrc = fields[i].getAnnotation(ColumnWish.class);
				if (!nqrc.name().equals(""))
					key = nqrc.name().toLowerCase();
				ClassFields classFields = new ClassFields(fields[i], true);
				annofields.put(key, classFields);
			}
			else
			{
				ClassFields classFields = new ClassFields(fields[i], false);
				annofields.put(key, classFields);
			}
		}
		return annofields;
	}

	private void check(String[] aliases)
	{
		if (!Arrays.equals(aliases, this.aliases))
		{
			throw new IllegalStateException("aliases are different from what is cached; aliases=" + Arrays.asList(aliases) + " cached=" + Arrays.asList(this.aliases));
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		WishAliasToBeanResultTransformer that = (WishAliasToBeanResultTransformer) o;

		if (!resultClass.equals(that.resultClass))
		{
			return false;
		}
		if (!Arrays.equals(aliases, that.aliases))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = resultClass.hashCode();
		result = 31 * result + (aliases != null ? Arrays.hashCode(aliases) : 0);
		return result;
	}

	class ClassFields
	{
		Field field;

		boolean hasName;

		public ClassFields(Field field, boolean hasName)
		{
			this.field = field;
			this.hasName = hasName;
		}

		public Field getField()
		{
			return field;
		}

		public boolean isHasName()
		{
			return hasName;
		}
	}
}
