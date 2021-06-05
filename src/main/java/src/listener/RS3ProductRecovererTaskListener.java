package src.listener;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.listener.StepListener;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;


@Named
@RequestScoped
public class RS3ProductRecovererTaskListener implements StepListener {

	static Logger logger = Logger.getLogger(RS3ProductRecovererTaskListener.class.getName());
	
	
	
	@Override
	public void beforeStep() throws Exception {
		String msg = "Starting RS3ProductRecovererTask at " + new Date();
		logger.log(Level.ALL, msg);

		
	}

	@Override
	public void afterStep() throws Exception {
		String msg = "Finished RS3ProductRecovererTask at " + new Date();
		logger.log(Level.ALL, msg);
	}

}
