package photobooks.gateways;

import java.util.Collection;

public interface IGateway<T>
{
	public Collection<T> getAll();
	public T getByID(int id);
	public boolean add(T newObj);
	public void update(T obj);
	public void delete(T obj);
}
