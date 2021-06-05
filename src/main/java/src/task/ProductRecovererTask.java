package src.task;

import java.util.Date;
import java.util.List;

import javax.batch.api.Batchlet;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import src.entity.CartItem;
import src.entity.Order;
import src.inter.IServiceLocator;
import src.manager.IStockManager;


@Named
@RequestScoped
public class ProductRecovererTask implements Batchlet {
	
	@Inject
	IServiceLocator serviceLocator;

	@Inject 
	IStockManager stockManager;
	
	
	
	
	@Override
	public String process() throws Exception {
		String status = "COMPLETED";
		
		// obtener todas las ordenes pre-confirmadas - allPreConfirmedOrders
		Date limitDate = new Date(new Date().getTime() - (1000 * 60 * 60 * 24));
		String sLimitDate = limitDate.toString();
		List<Order> listaOrdenes = getServiceLocator().getOrderServices().createNamedQueryListResult("allPreConfirmedOrdersUntil", "limit_date", sLimitDate);
						
		// por cada orden
		for(Order order : listaOrdenes) {			
			// por cada item del carrito
			for(CartItem item : order.getCart().getListaItems()) {
				// recupera el stock reservado
				getStockManager().recuperarStock(item.getOferta().getId(), item.getCounter());
			}
			// cancelar la orden por defecto en el pago - se ha superado el tiempo de espera
			cancelOrder(order);			
		}		
		getServiceLocator().getEntityManager().flush();			
		return status;
	}
	
	private void cancelOrder(Order order) {
		
		Date now = new Date();
		order.getPurchaseStatus().setRemark("CANCELED");
		order.getPurchaseStatus().setLastModification(now);
		order.setLastModificationDate(now);
		
		getServiceLocator().getOrderServices().update(order);		
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub

	}

	public IServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(IServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public IStockManager getStockManager() {
		return stockManager;
	}

	public void setStockManager(IStockManager stockManager) {
		this.stockManager = stockManager;
	}

}
