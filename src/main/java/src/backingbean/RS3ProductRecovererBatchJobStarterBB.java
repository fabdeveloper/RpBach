package src.backingbean;

import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class RS3ProductRecovererBatchJobStarterBB {
	
	
	
	public void startTask() {
		
		Properties prop = new Properties();
		prop.setProperty("miprop", "si");
		
		JobOperator jobOperator = BatchRuntime.getJobOperator();
		jobOperator.start("RS3ProductRecovererTask", prop);
	}

}
