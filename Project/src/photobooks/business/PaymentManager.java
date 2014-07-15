package photobooks.business;

import photobooks.gateways.IConditionalGateway;
import photobooks.gateways.PaymentGateway;

import java.util.ArrayList;
import java.util.Collection;

import photobooks.objects.Payment;

public class PaymentManager
{
	private IConditionalGateway<Payment> _gateway;
	
	public PaymentManager( IConditionalGateway<Payment> gateway )
	{
		_gateway = gateway;
	}
	
	public void insertStubData()
	{
		Payment newPayment = new Payment();
		newPayment.setDescription("This is a test payment");
		newPayment.setInvoiceId(1);
		this.insert( newPayment );
		
		newPayment = new Payment();
		newPayment.setDescription("This is a test payment2");
		newPayment.setInvoiceId(2);
		this.insert( newPayment );
	}
	
	public Collection<Payment> getAll()
	{
		return _gateway.getAll();
	}
	
	public Collection<Payment> getByInvoiceId(int id)
	{
		Collection<Payment> result;
		
		if (_gateway instanceof PaymentGateway)
		{
			result = _gateway.getAllWithId(id);
		}
		else
		{
			result = new ArrayList<Payment>();
			
			for (Payment item : this.getAll() )
			{
				if (item.getInvoiceId() == id)
				{
					result.add( item );
				}
			}
		}
		
		return result;
	}
	
	public Payment getByID(int id)
	{
		return _gateway.getByID(id);
	}
	
	public boolean insert(Payment payment)
	{
		return _gateway.add(payment);
	}
	
	public void delete(Payment payment)
	{
		_gateway.delete(payment);
	}
	
	public void update(Payment payment)
	{
		_gateway.update(payment);
	}
}
