package src.backingbean;

import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.transaction.Transactional;

@Named
@RequestScoped
public class RS3ProductRecovererBatchJobStarterBB {
	
	
	@Transactional
	public void startTask() {
		
		Properties prop = new Properties();
		prop.setProperty("miprop", "si");
		
		JobOperator jobOperator = BatchRuntime.getJobOperator();
		jobOperator.start("RS3ProductRecovererTask", prop);
	}

}
