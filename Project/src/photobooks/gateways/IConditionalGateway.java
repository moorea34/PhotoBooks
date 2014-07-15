package photobooks.gateways;

import java.util.Collection;

public interface IConditionalGateway<T> extends IGateway<T>
{
	public Collection<T> getAllWithId(int id);
}
