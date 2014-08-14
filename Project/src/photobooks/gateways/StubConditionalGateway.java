package photobooks.gateways;

import java.util.Collection;

public class StubConditionalGateway<T> extends StubGateway<T> implements IConditionalGateway<T> 
{
	public StubConditionalGateway()
	{
		super();
	}

	// will return data with specific id only
	public Collection<T> getAllWithId(int id) 
	{
		return data;
	}
}
