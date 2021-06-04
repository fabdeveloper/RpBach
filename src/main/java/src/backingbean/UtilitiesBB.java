package src.backingbean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;


import src.inter.IServiceLocator;


@Named
@RequestScoped
public class UtilitiesBB {
	
	@Inject
	private IServiceLocator serviceLocator;
	
	private Integer numOrders = 0;
	private Boolean bLoaded = false;
	
	
	public void loadNumOrders() {
		bLoaded = true;
		numOrders = getServiceLocator().getOrderServices().readAll().size();
	}

	public IServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(IServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public Integer getNumOrders() {
		if(bLoaded == false) {
			loadNumOrders();			
		}
		return numOrders;
	}

	public void setNumOrders(Integer numOrders) {
		this.numOrders = numOrders;
	}

	public Boolean getbLoaded() {
		return bLoaded;
	}

	public void setbLoaded(Boolean bLoaded) {
		this.bLoaded = bLoaded;
	}

}
