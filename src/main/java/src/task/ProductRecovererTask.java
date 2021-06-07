package src.task;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.batch.api.Batchlet;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import src.entity.CartItem;
import src.entity.Order;
import src.inter.IServiceLocator;
import src.manager.IStockManager;

@Dependent
@Named("ProductRecovererTask")
public class ProductRecovererTask implements Batchlet {
	
	static Logger logger = Logger.getLogger(ProductRecovererTask.class.getName());
	
	static {
		Handler consoleHandler = new ConsoleHandler();
		Handler fileHandler = null;
		try {
			fileHandler = new FileHandler("ProductRecovererTask.log", true);
		} catch (SecurityException | IOException e) {
			logger.log(Level.SEVERE, "ProductRecovererTask - error creando logger filehandler");
			e.printStackTrace();
		}
		if(consoleHandler != null){
			consoleHandler.setLevel(Level.ALL);
			logger.addHandler(consoleHandler);
		}
		if(fileHandler != null){
			SimpleFormatter sf = new SimpleFormatter();
			fileHandler.setFormatter(sf);
			fileHandler.setLevel(Level.ALL);
			logger.addHandler(fileHandler);
		}
		logger.log(Level.INFO, "ProductRecovererTask - logger inicializado");
		
	}
	
	@Inject
	IServiceLocator serviceLocator;

	@Inject 
	IStockManager stockManager;
	
	
	
	@Transactional
	@Override
	public String process() throws Exception {
		String msgStart = "ProductRecovererTask.process() ...";
		System.out.println(msgStart);
		logger.log(Level.ALL, msgStart);

		String status = "COMPLETED";
		
		// obtener todas las ordenes pre-confirmadas - allPreConfirmedOrders
		Date limitDate = new Date(new Date().getTime() - (1000 * 60 * 60 * 2));
		logger.log(Level.ALL, "Recuperando orders con fecha límite = " + limitDate);
		System.out.println("Recuperando orders con fecha límite = " + limitDate);

		List<Order> listaOrdenes = null;
		try {			

			listaOrdenes = getServiceLocator().getOrderServices().createNamedQueryListResultDateParam("allPreConfirmedOrdersUntil", "limit_date", limitDate);
			String msg = "Numero de ordenes encontradas = " + listaOrdenes.size();		
			logger.log(Level.ALL, msg);
			System.out.println(msg);

		
		}catch(Throwable t) {
			String algunerror = "Ocurrido algún error buscando ordenes - mensaje = " + t.getMessage();
			logger.log(Level.ALL, algunerror);
			System.out.println(algunerror);

		}
		

		
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
		order.getPurchaseStatus().setRemark("CANCELLED");
		order.getPurchaseStatus().setLastModification(now);
		order.setLastModificationDate(now);
		
		getServiceLocator().getOrderServices().update(order);		
	}

	@Override
	public void stop() throws Exception {
		logger.log(Level.ALL, "ProductRecovererTaks.stop() - task stopped");

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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ProductRecovererTask.logger = logger;
	}

}
