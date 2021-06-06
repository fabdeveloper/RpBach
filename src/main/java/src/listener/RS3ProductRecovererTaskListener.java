package src.listener;

import java.io.IOException;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.batch.api.listener.StepListener;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Dependent
@Named("RS3ProductRecovererTaskListener")
public class RS3ProductRecovererTaskListener implements StepListener {

	static Logger logger = Logger.getLogger(RS3ProductRecovererTaskListener.class.getName());
	
	static{	
		Handler consoleHandler = new ConsoleHandler();
		Handler fileHandler = null;
		try {
			fileHandler = new FileHandler("RS3ProductRecovererTaskListener.log", true);
		} catch (SecurityException | IOException e) {
			logger.log(Level.SEVERE, "RS3ProductRecovererTaskListener - error creando logger filehandler");
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
		logger.log(Level.INFO, "RS3ProductRecovererTaskListener - logger inicializado");
	}
	
	
	
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
