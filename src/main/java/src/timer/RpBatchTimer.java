package src.timer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;

import src.backingbean.RS3ProductRecovererBatchJobStarterBB;

@Startup
@Singleton
public class RpBatchTimer {
	
	@Inject
	private RS3ProductRecovererBatchJobStarterBB batchStarter;
	
	
	
	@Resource 
	private TimerService timerService;
	
	
	@PostConstruct
	public void initTimer() {
		timerService.createTimer(0, 600000, null);
	}
	
	
	@Timeout
	public void onTime(Timer timer) {
		recuperaProducto();
	}

	private void recuperaProducto() {
		System.out.println("RpBatchTimer.recuperProducto() - ejecutado a las " + new java.util.Date());
		getBatchStarter().startTask();
	}
	


	public RS3ProductRecovererBatchJobStarterBB getBatchStarter() {
		return batchStarter;
	}


	public void setBatchStarter(RS3ProductRecovererBatchJobStarterBB batchStarter) {
		this.batchStarter = batchStarter;
	}


	public TimerService getTimerService() {
		return timerService;
	}


	public void setTimerService(TimerService timerService) {
		this.timerService = timerService;
	}
}
