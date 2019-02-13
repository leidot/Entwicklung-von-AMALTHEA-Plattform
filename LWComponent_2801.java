package org.eclipse.app4mc.amalthea.example.workflow.components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.CallGraph;
import org.eclipse.app4mc.amalthea.model.CallSequence;
import org.eclipse.app4mc.amalthea.model.CallSequenceItem;
import org.eclipse.app4mc.amalthea.model.Core;
import org.eclipse.app4mc.amalthea.model.CoreType;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.Deviation;
import org.eclipse.app4mc.amalthea.model.ECU;
import org.eclipse.app4mc.amalthea.model.Frequency;
import org.eclipse.app4mc.amalthea.model.GraphEntryBase;
import org.eclipse.app4mc.amalthea.model.HWModel;
import org.eclipse.app4mc.amalthea.model.HwSystem;
import org.eclipse.app4mc.amalthea.model.Instructions;
import org.eclipse.app4mc.amalthea.model.InstructionsDeviation;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.LabelAccess;
import org.eclipse.app4mc.amalthea.model.LabelAccessEnum;
import org.eclipse.app4mc.amalthea.model.LongObject;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.Memory;
import org.eclipse.app4mc.amalthea.model.Microcontroller;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.Prescaler;
import org.eclipse.app4mc.amalthea.model.Quartz;
import org.eclipse.app4mc.amalthea.model.RunnableInstructions;
import org.eclipse.app4mc.amalthea.model.RunnableItem;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Scheduler;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.TaskAllocation;
import org.eclipse.app4mc.amalthea.model.TaskRunnableCall;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.Value;
import org.eclipse.app4mc.amalthea.workflow.core.Context;
import org.eclipse.app4mc.amalthea.workflow.core.WorkflowComponent;
import org.eclipse.app4mc.amalthea.workflow.core.exception.WorkflowException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.app4mc.amalthea.model.Runnable;

public class LWComponent_2801 extends WorkflowComponent{
	@Override
	protected void runInternal(Context ctx) throws WorkflowException {
		// TODO Auto-generated method stub
	
		//Information from Log to File	
		File file = new File("D:\\1");
		if(!file.exists()){//如果文件夹不存在
			file.mkdir();
		}
		
		Logger logger = Logger.getLogger("MyLog");  
	    FileHandler fh = null;  
	    
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("D:\\1\\out1.txt");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
	        

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
		
		/*+++++++++++++++++++++++++
		 * Operation in SWModel
		 ++++++++++++++++++++++++*/
		// some checking if sw model is available
		if (null == getAmaltheaModel(ctx).getSwModel()) {
			
			throw new WorkflowException("No proper SWModel available!");
		}
		this.log.info("Number of tasks in model: " + getAmaltheaModel(ctx).getSwModel().getTasks().size());
		logger.info("Number of tasks in model: " + getAmaltheaModel(ctx).getSwModel().getTasks().size());
		//this.log.info("Number of tasks in model: " + getAmaltheaModel(ctx).toString());
		
		/*+++++++++++++++++++++++++
		 * Operation in HWModel
		 ++++++++++++++++++++++++*/
		// some checking if sw model is available
		if (null == getAmaltheaModel(ctx).getHwModel()) {
					
			throw new WorkflowException("No proper HWModel available!");
		}
			
		/*+++++++++++++++++++++++++
		 * Operation in MappingModel
		 ++++++++++++++++++++++++*/
		// some checking if Mapping model is available
		// Remember that we must change file location in "workflow.js", because we must use mapping, "/output/AMALTHEA_Democar_mapping.amxmi"
		if (null == getAmaltheaModel(ctx).getMappingModel()) {
			
			throw new WorkflowException("No proper MappingModel available!");
		}
		//this.log.info("Start to find tasks in same Core: ");
		
		//create file to store Info from Console
		//File file = new File("""output.txt"); C:\Users\lei\Desktop
/*		//创建文件夹
		File file = new File("D:\\1");
		if(!file.exists()){//如果文件夹不存在
			file.mkdir();
		}
		try{
			File f=new File("D:\\1\\out.txt");
	        f.createNewFile();
			
			//异常处理
			//如果Qiju_Li文件夹下没有Qiju_Li.txt就会创建该文件
				//BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\1\\1.txt"));
			//bw.write("Hello I/O!");//在创建好的文件中写入"Hello I/O"
			//bw.close();//一定要关闭文件
			FileOutputStream fileOutputStream = new FileOutputStream(f);
	        PrintStream printStream = new PrintStream(fileOutputStream);
	        System.setOut(printStream);
	        System.out.println("默认输出到控制台的这一句，输出到了文件 out.txt");

		}catch(IOException el){
			System.out.println("Error during reading/writing");
		}  
*/		 
		
				
	    
	    
		/*+++++++++++++++++++++++++++++++++++++++++++++++++++
		 * ++++++++++++++++++++++++++++++++++++++++++++++++++
		 * Main Function starts here
		 ++++++++++++++++++++++++++++++++++++++++++++++++++++
		 ++++++++++++++++++++++++++++++++++++++++++++++++++++*/	
		
		//Sorting Tasks according to Priority level ("1", "2",..."9", "natural ordering" from high to low)	
		//In Amalthea model we shall limit the priority of task between 1 and 9, because we use String as data type, which makes a difference in comparing data
		SortedMap<String, ArrayList<Task>> sortedTasksByPrio = getSortedMap(ctx);
		this.log.info("Remember to set MHz as default Unit for Frequency in CoreType" );
		this.log.info("Remember we use 'GB/s' as Unit for 'Data Rate' in MemoryType " );
		this.log.info("Remember we use mW as default Unit for Power" + "\r");
		
		logger.info("Remember to set MHz as default Unit for Frequency in CoreType" );
		logger.info("Remember we use 'GB/s' as Unit for 'Data Rate' in MemoryType ");
		logger.info("Remember we use mW as default Unit for Power" + "\r");
		//Sorting Tasks according to TaskAllocation, where each task is allocated to a single Scheduler
		HashMap<String, ArrayList<Task>> sortedTasksBySched = getHashMap(ctx);
		this.log.info("The Loop for calculating WCRT and BCRT of each Task starts here : \r");
		logger.info("The Loop for calculating WCRT and BCRT of each Task starts here : \r");
		
		for (String prio : sortedTasksByPrio.keySet()) {
			ArrayList<Task> arrayList = sortedTasksByPrio.get(prio);
			this.log.info("Task List of Priority Levle '" + prio + "' is : " + arrayList);
			logger.info("Task List of Priority Levle '" + prio + "' is : " + arrayList);
			//this.log.info("Remember to set MHz as default Unit for Frequency in CoreType" );

			for (Task task : arrayList) {
				String taskName = task.getName();
				long taskWCET = 0;
				long taskBCET = 0;
				
				EList<Stimulus> taskStimuliList = task.getStimuli();
				
				//Examine, whether there are Stimuli in this task
				if (taskStimuliList.isEmpty()) {
					this.log.info("Warning : There exists none Stimulus for Task '" + taskName + "'");
					logger.info("Warning : There exists none Stimulus for Task '" + taskName + "'");
				}
				//Examine and output Warning, if there are more than 1 Stimulus in this task
				if (taskStimuliList.size() > 1) {
					this.log.info("Warning : This Plug-in supports only one Stimulus for each Task");
					logger.info("Warning : This Plug-in supports only one Stimulus for each Task");
				}
				
				
				//Despite we have a list of Stimulus for each task, but actually in Task model we have just one Stimulus (maximal one Periodic Stimulus and one InterProcessTrigger Stimulus)in the list
				for (Stimulus stimulus : taskStimuliList) {
					if (PeriodicStimulus.class.isInstance(stimulus)) {
						PeriodicStimulus periodStimulus = (PeriodicStimulus) stimulus;
						
						//get WCET and BCET of single Task
						taskWCET = getWCETinIC(ctx, task);
						taskBCET = getBCETinIC(ctx, task);
						this.log.info("Working for TASK : [" + taskName + "].");
						this.log.info("It's WCET in InstructionCycles is :" + taskWCET + ". BCET in InstructionCycles is :" + taskBCET);
						logger.info("Working for TASK : [" + taskName + "].");
						logger.info("It's WCET in InstructionCycles is :" + taskWCET + ". BCET in InstructionCycles is :" + taskBCET);
						//Time Unit Conversion from Instruction Cycles to mS
						double taskWCETinmS = runUnitConversion(ctx, taskWCET);
						double taskBCETinmS = runUnitConversion(ctx, taskBCET);
						this.log.info("It's WCET in Unit 'mS' is :" + taskWCETinmS + " mS. It's BCET in Unit mS is :" + taskBCETinmS + " mS");
						logger.info("It's WCET in Unit 'mS' is :" + taskWCETinmS + " mS. It's BCET in Unit mS is :" + taskBCETinmS + " mS");

						
						//get Period of this Task and change from Time to double
						Time period = periodStimulus.getRecurrence();
						this.log.info("Period of this Task is :" + period);
						logger.info("Period of this Task is :" + period);
						double periodValue = period.getValue().longValue();
						
						//Output Warning, if WCET > Period
						if (taskWCETinmS > periodValue) {
							
							this.log.info("Warning : Task '" + taskName + "' has a period less than its WCET");
							logger.info("Warning : Task '" + taskName + "' has a period less than its WCET");
							}
						
						//Output Warning, if BCET > Period
						if (taskBCETinmS > periodValue) {
							
							this.log.info("Warning : Task '" + taskName + "' has a period less than its BCET");
							logger.info("Warning : Task '" + taskName + "' has a period less than its BCET");
							}

						
						TimeUnit periodUnit = period.getUnit();
						//this.log.info("The Unit of this Task is :" + periodUnit);
						
						//get Write Access Time of Single Task
						//We assume that Write Access of Critical Section will be blocked by other Task with lower Priority, for Reading Access would not be blocked
						//Here we assume, that Write Access to all Critical Section during each task would lead to Blocking.
						//String writeLabelList = getWriteLabelList(task);
						ArrayList<Label> writeLabelList = getWriteLabelList(task);
						//this.log.info("The List of Write Label in task '" + taskName +"' is :"+ writeLabelList);
						
						//Calculate the total Blocking Time of this Task
						//Through Method calculateBlockingTime we have set the Unit of blockingTime in milliSecond
						double blockingTime = calculateBlockingTime(writeLabelList, ctx);
						this.log.info("The total Blocking Time of Task '" + taskName +"' is :"+ blockingTime + " mS");
						logger.info("The total Blocking Time of Task '" + taskName +"' is :"+ blockingTime + " mS");
						//this.log.info("Remember we use 'GB/s' as Unit for 'Data Rate' in MemoryType " + "\r");
						
						double taskWCRT = getTaskWCRT(task, ctx, sortedTasksBySched);
						this.log.info("WCRT of task '" + taskName + "' is :" + taskWCRT + " mS");
						logger.info("WCRT of task '" + taskName + "' is :" + taskWCRT + " mS");
						
						
						//Calculating BCRT of the task
						double taskBCRT = getTaskBCRT(task, ctx, sortedTasksBySched);
						this.log.info("BCRT of task '" + taskName + "' is :" + taskBCRT + " mS");
						logger.info("BCRT of task '" + taskName + "' is :" + taskBCRT + " mS");
						
						//Test for method GCD
						//double gcdtest = GCD(20, 5);
						//this.log.info("GCD is :" + gcdtest);
						
						//Calculating Computing Energy Consumption
						//Remember the default Unit of Power is milliWatt
						Value computingPowerValue = getComputingPower(ctx);
						//this.log.info("The Computing Power is :" + computingPowerValue + "mW");
						String computingPowerString = computingPowerValue.toString().substring(9);
						this.log.info("The Computing Power is :" + computingPowerString + " mW");
						logger.info("The Computing Power is :" + computingPowerString + " mW");
						
						double computingPower = Double.parseDouble(computingPowerString);
						double computingEnergyConsump = computingPower * taskWCRT;
						this.log.info("The Computing Energy Consumption of task '" + taskName + "' is :" + computingEnergyConsump + " mS*mW");
						logger.info("The Computing Energy Consumption of task '" + taskName + "' is :" + computingEnergyConsump + " mS*mW");
						
						//Calculating Idle state Consumption
						//Remember the default Unit of Power is milliWatt
						Value idlePowerValue = getIdlePower(ctx);
						String idlePowerString = idlePowerValue.toString().substring(9);
						double idlePower = Double.parseDouble(idlePowerString);
						//Calculating Idle Time
						double idleTime = periodValue - taskWCRT;
						double idleStateEnergyConsump = idlePower * idleTime;
						this.log.info("The idle state Energy Consumption of task '" + taskName + "' is :" + idleStateEnergyConsump + " mS*mW");
						logger.info("The idle state Energy Consumption of task '" + taskName + "' is :" + idleStateEnergyConsump + " mS*mW");

						//Calculating Total Energy Consumption
						double totalEnergyConsump = idleStateEnergyConsump + computingEnergyConsump;
						this.log.info("The Total Energy Consumption of task '" + taskName + "' is :" + totalEnergyConsump + " mS*mW" + "\r");
						logger.info("The Total Energy Consumption of task '" + taskName + "' is :" + totalEnergyConsump + " mS*mW" + "\r");

					}			
				}
			}
	
		}
	

		
	}

	
	//++++++++++++++++++++++++++++++++++++++++++++++
	//M--e--t--h--o--d
	//++++++++++++++++++++++++++++++++++++++++++++++
	
	//get SortedMap from taskList
	private SortedMap<String, ArrayList<Task>> getSortedMap(Context ctx) {
		//Information from Log to File	
		Logger logger = Logger.getLogger("MyLog");  
	    FileHandler fh = null;  
	    
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("D:\\1\\out1.txt");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

				
		EList<Task> taskList = getTasks(ctx);
		this.log.info("Task List without sorting:" + taskList.toString());
		logger.info("Task List without sorting:" + taskList.toString());				
		SortedMap<String, ArrayList<Task>> sortedTasksByPrio = new TreeMap<>();
		//CallSequence cseq = null;
		for (Task task : taskList) {
			//We must use Value of Priority in form of String in Amalthea Model
			//In Amalthea Model we must limit the priority value to 9, exactly 1~9. Otherweise "10" is less than "6" because of String.
			//In Amalthea Model Priority :1 -> 9, exactly from highest to lowest
			Value prioValue = task.getCustomProperties().get("priority");
			String prioStr = prioValue.toString();
			//add, just add a task in ArrayList<Task>, not in TreeMap
			if (sortedTasksByPrio.containsKey(prioStr)) {
				sortedTasksByPrio.get(prioStr).add(task);
			} else {
				ArrayList<Task> prioTasks = new ArrayList<Task>();
			    prioTasks.add(task);
				sortedTasksByPrio.put(prioStr, prioTasks);
			}
		}
		this.log.info("Task List WITH sorting:" + sortedTasksByPrio.toString());
		logger.info("Task List WITH sorting:" + sortedTasksByPrio.toString());
		return sortedTasksByPrio;
	}
	
	// get TaskList according to Scheduler, if tasks are allocated to the same Scheduler, then they are set in a list
	private HashMap<String, ArrayList<Task>> getHashMap(Context ctx) {
		//Information from Log to File	
		Logger logger = Logger.getLogger("MyLog");  
	    FileHandler fh = null;  
	    
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("D:\\1\\out1.txt");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

		
		
		EList<TaskAllocation> taskAllocationList = getAmaltheaModel(ctx).getMappingModel().getTaskAllocation();
		
		HashMap<String, ArrayList<Task>> sortedTaskBySchduler = new HashMap<>();
		String schedulerString = null;
		for (TaskAllocation taskAllocation : taskAllocationList) {
				//Scheduler scheduler = taskAllocation.getScheduler();
			schedulerString = taskAllocation.getScheduler().toString();
			Task task = taskAllocation.getTask();
			if (sortedTaskBySchduler.containsKey(schedulerString)) {
				sortedTaskBySchduler.get(schedulerString).add(task);
			} else {
				ArrayList<Task> schedTasks = new ArrayList<Task>();
				schedTasks.add(task);
				sortedTaskBySchduler.put(schedulerString, schedTasks);
			}
			
			
		}
		this.log.info("Task List according to Scheduler with sorting :" + sortedTaskBySchduler.toString());
		logger.info("Task List according to Scheduler with sorting :" + sortedTaskBySchduler.toString());
		return sortedTaskBySchduler;
		
	}
	
	
	// read all tasks in SWModel and return a list of tasks
	private EList<Task> getTasks(Context ctx) {
		
		final Amalthea amaltheaModel = getAmaltheaModel(ctx);

		assert null != amaltheaModel;

		//this.log.info("Starting to read AMALTHEA model...");

		final SWModel swModel = amaltheaModel.getSwModel();

		EList<Task> taskList = swModel.getTasks();

		return taskList;
	}
	
	//Method for calculating WCETinIC, where IC means Instruction Cycles
	//private long getWCETinmS(Context ctx, Task task) {
	private long getWCETinIC(Context ctx, Task task) {
		//下面这一行中的函数getCallList有问题，不同的Task得到的结果居然是一样的
		//EList<CallSequenceItem> callSequence = getCallList(ctx);
		//下面的构造函数就正确了，因为我们采用了形参，而不是采用Context
		EList<CallSequenceItem> callSequence = getCallList_new(task);
		//this.log.info("Call Sequence of this Task" + taskName + " is : " + callSequence);
		long taskwcet = 0;
		Runnable runnable = null;
		EList<RunnableItem> runnableItemList = null;
		//Judge whether, this CallSequenceItem is CallRunnable
		for (CallSequenceItem callSequenceItem : callSequence) {
			if (TaskRunnableCall.class.isInstance(callSequenceItem)) {
				TaskRunnableCall calledRunnable = TaskRunnableCall.class.cast(callSequenceItem);
			
				runnable = calledRunnable.getRunnable();
				runnableItemList = runnable.getRunnableItems();
				for (RunnableItem runnableItem : runnableItemList) {
					if (RunnableInstructions.class.isInstance(runnableItem)) {
						RunnableInstructions runnableInstructions = RunnableInstructions.class.cast(runnableItem);					
						Instructions instructions = runnableInstructions.getDefault();
						Deviation<LongObject> deviation = null;
						//long deviationLowerBound = 0;
						long deviationUpperBound = 0;
						if (InstructionsDeviation.class.isInstance(instructions)) {
							InstructionsDeviation instructionsDeviation = InstructionsDeviation.class.cast(instructions);
									
							deviation = instructionsDeviation.getDeviation();
							//deviationLowerBound = deviation.getLowerBound().getValue();
							deviationUpperBound = deviation.getUpperBound().getValue();
									
							//taskBCET = taskBCET + deviationLowerBound;
							taskwcet = taskwcet + deviationUpperBound;
						}
					}
				}
			}
		}
		return taskwcet;
	}
	
	//Method for calculating BCETinIC, IC means Instruction Cycles
	//private long getBCETinmS(Context ctx, Task task) {
	private long getBCETinIC(Context ctx, Task task) {
		//下面这一行中的函数getCallList有问题，不同的Task得到的结果居然是一样的
		//EList<CallSequenceItem> callSequence = getCallList(ctx);
		//下面的构造函数就正确了，因为我们采用了形参，而不是采用Context
		EList<CallSequenceItem> callSequence = getCallList_new(task);
		//this.log.info("Call Sequence of this Task" + taskName + " is : " + callSequence);
		//long taskwcet = 0;
		long taskbcet = 0;
		Runnable runnable = null;
		EList<RunnableItem> runnableItemList = null;
		//Judge whether, this CallSequenceItem is CallRunnable
		for (CallSequenceItem callSequenceItem : callSequence) {
			if (TaskRunnableCall.class.isInstance(callSequenceItem)) {
				TaskRunnableCall calledRunnable = TaskRunnableCall.class.cast(callSequenceItem);
	
				runnable = calledRunnable.getRunnable();
				runnableItemList = runnable.getRunnableItems();
				for (RunnableItem runnableItem : runnableItemList) {
					if (RunnableInstructions.class.isInstance(runnableItem)) {
						RunnableInstructions runnableInstructions = RunnableInstructions.class.cast(runnableItem);
				
						Instructions instructions = runnableInstructions.getDefault();
						Deviation<LongObject> deviation = null;
						long deviationLowerBound = 0;
						//long deviationUpperBound = 0;
						if (InstructionsDeviation.class.isInstance(instructions)) {
							InstructionsDeviation instructionsDeviation = InstructionsDeviation.class.cast(instructions);
							
							deviation = instructionsDeviation.getDeviation();
							deviationLowerBound = deviation.getLowerBound().getValue();
							//deviationUpperBound = deviation.getUpperBound().getValue();
							
							taskbcet = taskbcet + deviationLowerBound;
							//taskwcet = taskwcet + deviationUpperBound;
						}
					}
				}
			}
		}
		return taskbcet;
	}
	
	//Unit Consersion from Instruction Cycles to mS
	//In this method we use some other method, where exists problem, if we set more than 1 type of CoreType. Because in those method we search for all types but just return the last one
	//But in our Model, we have just one kind of CoreType, therefore, we can use this method anyway
	private double runUnitConversion(Context ctx, long executionCycles) {
		float IPC = getIPC(ctx);
		//Frequency frequency = getPrescalerQuartzFrequency(ctx);
		//double frequencyValue = frequency.getValue();
		double coreFrequency = getCoreFrequency(ctx);
		double WCETinmS = 0;
		double denominator = 0;
		//long BCETinmS = 0;
		
		//WCETinmS = executionCycles/(IPC * frequency);
		//Here we define the Unit of Frequency is MHz
		denominator = IPC * coreFrequency;
		//We use constant 1000, because we need to gurantee the Unit of time is MilliSecond
		WCETinmS = (executionCycles/denominator)/1000;
		return WCETinmS;
	}
	
	//get Elist for all Write Access in a Task and return this List
	private ArrayList<Label> getWriteLabelList(Task task) {
	//private String getWriteLabelList(Task task) {
		ArrayList<Label> writeLabelList = new ArrayList<Label>();
		String literal = null;
		//EList<Label> writeLabelList = null;
		CallGraph callGraph = task.getCallGraph();
		EList<GraphEntryBase> entry = callGraph.getGraphEntries();
		for (GraphEntryBase graphEntryBase : entry) {
			CallSequence cseq = CallSequence.class.cast(graphEntryBase);
			
			EList<CallSequenceItem> callSequenceItemList = cseq.getCalls();
			for (CallSequenceItem callSequenceItem : callSequenceItemList) {
				if (TaskRunnableCall.class.isInstance(callSequenceItem)) {
					TaskRunnableCall calledRunnable = TaskRunnableCall.class.cast(callSequenceItem);
				
							
					Runnable runnable = calledRunnable.getRunnable();
					EList<RunnableItem> runnableItemList = runnable.getRunnableItems();
					for (RunnableItem runnableItem : runnableItemList) {
						if (LabelAccess.class.isInstance(runnableItem)) {
							LabelAccess labelAccess = LabelAccess.class.cast(runnableItem);
									
							LabelAccessEnum accessMode = labelAccess.getAccess();
							//String literal = accessMode.getLiteral();
							literal = accessMode.getLiteral();
							Label label = labelAccess.getData();
							//this.log.info("Literal is : " + literal);
							if (literal == "write") {
										
								writeLabelList.add(label);
								}
							}
					}
				}
			}
		}
		return writeLabelList;
		//return literal;
	} 
	
	//Calculate total Write Access Time for each Task
	//We assume that the Unit of LabelSize is Bit
	//We assume that the Unit of DataRate is GB/S, here B means Byte
	private double calculateBlockingTime(ArrayList<Label> writeLabelList, Context ctx) {
		String dataRateString = null;
		float blockingTime = 0;
		float dataRateLong = 0;
		if (getDataRateUnderHwSystem(ctx) != null) {
			//dataRateString = getDataRateUnderHwSystem(ctx).toString();
			//Because the input String include (String), so we have to exclude the beginning 8 bit
			//dataRateString = getDataRateUnderHwSystem(ctx).toString();
			//The Output is "(double) 512" therefore we just hold the bits after 9-th bit
			dataRateString = getDataRateUnderHwSystem(ctx).toString().substring(9);
		}
		else if (getDataRateUnderEcu(ctx) != null) {
			//dataRateString = getDataRateUnderEcu(ctx).toString();
			dataRateString = getDataRateUnderEcu(ctx).toString().substring(9);
		}
		else if (getDataRateUnderMicrocontroller(ctx)!= null) {
			//dataRateString = getDataRateUnderMicrocontroller(ctx).toString();
			dataRateString = getDataRateUnderMicrocontroller(ctx).toString().substring(9);
		}
		else if (getDataRateUnderCore(ctx) != null) {
			//dataRateString = getDataRateUnderCore(ctx).toString();
			dataRateString = getDataRateUnderCore(ctx).toString().substring(9);

		}
		//change Data Type from String to float
		//dataRateLong = Long.valueOf(dataRateString);
		dataRateLong = Float.parseFloat(dataRateString);
				
		//Remember that the Unit of LabelSize is Bit
		long totalLabelValue = 0;
		for (Label label : writeLabelList) {
			DataSize labelSize = label.getSize();
			long labelValue = labelSize.getValue().longValue();
					 
			 totalLabelValue += labelValue; 
			 }
		//Here we must have a conversion Bit/(GB/S), exactly ms/8000000
		//The Unit of totalLabelValue is milliSecond
		totalLabelValue = totalLabelValue/8000000;
		//The Unit is NanoSecond with number 8
		//totalLabelValue = totalLabelValue/8;
		blockingTime = totalLabelValue/dataRateLong;
		return blockingTime;
	}
	
	//create new getCallList() method as before, which uses Task as Parameter
	private EList<CallSequenceItem> getCallList_new(Task task) {
		CallSequence cseq = null;
				
		CallGraph cgraph = task.getCallGraph();
		for (GraphEntryBase entry : cgraph.getGraphEntries()) {
			 // Cast each entry convert into CallSequence
			 cseq = CallSequence.class.cast(entry);
			 }
		return cseq.getCalls();
	}

	//get IPC from "Core Type"
	private float getIPC(Context ctx) {
		EList<Core> coreList = getCoreList(ctx);
				
		//CoreType coreType = null;
		float instructionPerCycle = 0;
		for (Core core : coreList) {
					
			CoreType coreType = core.getCoreType();
			instructionPerCycle = coreType.getInstructionsPerCycle();
		}
				
		return instructionPerCycle;
				
	}
	
	//get Value of Frequency
	//whats more, we need the product from Quartzfrequency and Clock Ratio of prescaler, therefore we add ClockRatio 
	private double getCoreFrequency(Context ctx) {
		Frequency frequency = getPrescalerQuartzFrequency(ctx);
		double clockRatio = getPrescalerClockRatio(ctx);

		double frequencyValue = frequency.getValue();
		double coreFrequency = clockRatio * frequencyValue;
		
		return coreFrequency;
		//return frequencyValue;
	}
	
	//get Data Rate from "Memory Type", the Data Rate is stored in CustomProperty, if Memory is under HwSystem
	private Value getDataRateUnderHwSystem(Context ctx) {
		//Information from Log to File	
		Logger logger = Logger.getLogger("MyLog");  
	    FileHandler fh = null;  
	    
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("D:\\1\\out1.txt");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

		
		HwSystem hwSystem = getHwSystem(ctx);
		
		EList<Memory> memoryList = hwSystem.getMemories();
		if (memoryList == null) {
			this.log.info("Error: There is no Memory in HWModel!");
			logger.info("Error: There is no Memory in HWModel!");
			
		}
		org.eclipse.app4mc.amalthea.model.MemoryType memoryType = null;
		EMap<String, Value> memoryTypeCustomProperty = null;
		Value dataRateValue = null;
		
		//String dataRateValue = null;
		//long dataRate = 0;
		
		for (Memory memory : memoryList) {
			
			memoryType = memory.getType();
			if (memoryType == null) {
				this.log.info("Error: There is no Definition of MemoryType in HWModel!");
				logger.info("Error: There is no Definition of MemoryType in HWModel!");
			}
			memoryTypeCustomProperty = memoryType.getCustomProperties();
			
			if (memoryTypeCustomProperty.containsKey("Data Rate")) {
				
				dataRateValue = memoryTypeCustomProperty.get("Data Rate");
				//dataRateValue = memoryTypeCustomProperty.get("Data Rate").toString();
			}
		}
		return dataRateValue;
	}

	//get Data Rate from "Memory Type", the Data Rate is stored in CustomProperty, if Memory is under ECU
	private Value getDataRateUnderEcu(Context ctx) {
		EList<ECU> ecuList = getEcuList(ctx);
		
		EList<Memory> memoryList = null;
		org.eclipse.app4mc.amalthea.model.MemoryType memoryType = null;
		EMap<String, Value> memoryTypeCustomProperty = null;
		Value dataRateValue = null;
		for (ECU ecu : ecuList) {
			memoryList = ecu.getMemories();
			for (Memory memory : memoryList) {
				
				memoryType = memory.getType();
				memoryTypeCustomProperty = memoryType.getCustomProperties();
				
				if (memoryTypeCustomProperty.containsKey("Data Rate")) {
					
					dataRateValue = memoryTypeCustomProperty.get("Data Rate");
				}
			}
		}
		return dataRateValue;
	}
	
	//get Data Rate from "Memory Type", the Data Rate is stored in CustomProperty, if Memory is under Microcontroller
	private Value getDataRateUnderMicrocontroller (Context ctx){
		EList<Microcontroller> microcontrollerList = getMicrocontrollerList(ctx);
			
		EList<Memory> memoryList = null;
		org.eclipse.app4mc.amalthea.model.MemoryType memoryType = null;
		EMap<String, Value> memoryTypeCustomProperty = null;
		Value dataRateValue = null;
		for (Microcontroller microcontroller : microcontrollerList) {
			memoryList = microcontroller.getMemories();
			for (Memory memory : memoryList) {
				memoryType = memory.getType();
				memoryTypeCustomProperty = memoryType.getCustomProperties();
				if (memoryTypeCustomProperty.containsKey("Data Rate")) {
				
					dataRateValue = memoryTypeCustomProperty.get("Data Rate");
				}
			}	
			
		}
		return dataRateValue;
		
	}
	
	//get Data Rate from "Memory Type", the Data Rate is stored in CustomProperty, if Memory is under Core
	private Value getDataRateUnderCore(Context ctx) {
		EList<Core> coreList = getCoreList(ctx);
		
		EList<Memory> memoryList = null;
		org.eclipse.app4mc.amalthea.model.MemoryType memoryType = null;
		EMap<String, Value> memoryTypeCustomProperty = null;
		Value dataRateValue = null;
		for (Core core : coreList) {
			memoryList = core.getMemories();
			for (Memory memory : memoryList) {
				memoryType = memory.getType();
				memoryTypeCustomProperty = memoryType.getCustomProperties();
				if (memoryTypeCustomProperty.containsKey("Data Rate")) {
				
					dataRateValue = memoryTypeCustomProperty.get("Data Rate");
				}
			}	
			
		}
		return dataRateValue;
	}	

	//get all Core from Microcontrollers
	private EList<Core> getCoreList(Context ctx) {
		EList<Microcontroller> microcontrollerList = getMicrocontrollerList(ctx);
		
		EList<Core> coreList = null;
		for (Microcontroller microcontroller : microcontrollerList) {
			
			coreList = microcontroller.getCores();
		}
			
		return coreList;
		
	}
	
	//get Frequency of Quartz from Prescaler, which is from Core
	private Frequency getPrescalerQuartzFrequency(Context ctx) {
		EList<Core> coreList = getCoreList(ctx);

		Prescaler prescaler = null;
		Quartz quartz = null;
		Frequency frequency = null;
		for (Core core : coreList) {
			
			prescaler = core.getPrescaler();
			quartz = prescaler.getQuartz();
			frequency = quartz.getFrequency();
			
		}
		return frequency;
	}
	
	//get "Clock Ratio" from Prescaler, which is from Core
	private double getPrescalerClockRatio(Context ctx) {
		EList<Core> coreList = getCoreList(ctx);
		
		Prescaler prescaler = null;
		double clockRatio = 0;
		for (Core core : coreList) {
			
			prescaler = core.getPrescaler();
			clockRatio = prescaler.getClockRatio();
		}
		return clockRatio;
		
	}
	
	// read the single HwSystem in HWModel 
	private HwSystem getHwSystem(Context ctx) {
		final Amalthea amaltheaModel = getAmaltheaModel(ctx);

		assert null != amaltheaModel;

		//this.log.info("Starting to read AMALTHEA model...");

		final HWModel hwModel = amaltheaModel.getHwModel();

		HwSystem hardwareSystem = hwModel.getSystem();

		return hardwareSystem;
	}
	
	//get all Microcontroller from ECUs
	private EList<Microcontroller> getMicrocontrollerList(Context ctx) {
		EList<ECU> ecuList = getEcuList(ctx);
		
		EList<Microcontroller> microcontrollerList = null;
		for (ECU ecu : ecuList) {
			
			microcontrollerList = ecu.getMicrocontrollers();
		}
		
		return microcontrollerList;
	}

	// get all ECU from HwSystem
	private EList<ECU> getEcuList(Context ctx) {
		HwSystem hwSystem = getHwSystem(ctx);
		
		EList<ECU> ecuList = hwSystem.getEcus();
		
		return ecuList;
	}
	
	
	// calculate WCRT of single Task
	// need method "getTaskScheduler"
	// calculate Preemption time according to task with higher priority 
	private double getTaskWCRT(Task task, Context ctx, HashMap<String, ArrayList<Task>> sortedTasksByScheduler) {
	//private ArrayList<Task> getTaskWCRT(Task task, Context ctx, HashMap<String, ArrayList<Task>> sortedTasksByScheduler, SortedMap<String, ArrayList<Task>> sortedTasksByPriority) {
		//Information from Log to File	
		Logger logger = Logger.getLogger("MyLog");  
	    FileHandler fh = null;  
	    
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("D:\\1\\out1.txt");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

		
		String schedulerString = getTaskScheduler(task, ctx).toString();
		ArrayList<Task> taskListBySched = sortedTasksByScheduler.get(schedulerString);
		String taskPriorityValue = task.getCustomProperties().get("priority").toString();
		//double totalPreemptionTime = 0;
		double wcetHigherPrior = 0;
		
		//get WCET of this current task, which is one parameter of this method
		long taskWCETinIC = getWCETinIC(ctx, task);
		double taskWCET = runUnitConversion(ctx, taskWCETinIC);
		
		//get Blocking Time of this current task,which is one parameter of this method
		ArrayList<Label> writeLabelList = getWriteLabelList(task);
		double blockingTime = calculateBlockingTime(writeLabelList, ctx);
		
		//get Period of this current task,which is one parameter of this method
		long taskPeriodValue = 0;
		EList<Stimulus> taskStimulusList = task.getStimuli();
		for (Stimulus stimulus : taskStimulusList) {
			PeriodicStimulus periodicStimulus = (PeriodicStimulus) stimulus; 
			
			Time period = periodicStimulus.getRecurrence();
			//Take care, the default Unit must be mS
			taskPeriodValue = period.getValue().longValue();
			}
		//this.log.info("The period of the specified Task '" + task + "' is :" + taskPeriodValue);
		
		//Initialization of WCRT of current task
		//This Value should be a constant, which represents ONLY WCET und Blocking Time of current TASK
		double initialWCRT = taskWCET + blockingTime;
		//This variable records the last taskWCRT, get ready to compare with the current taskWCRT
		double temporaryWCRT = 0;
		//this.log.info("The WCRT of task '"+ task + "' is :" + taskWCRT);
		double preempTime = 0;
		boolean equalJudge = false;
		
		//Initialing taskWCRT, which changes with Cycles Number of preemptive Task
		double taskWCRT = initialWCRT;
		//ITERATIVE CALCULATION ALGORITHME
		//while (taskWCRT < taskPeriodValue || ) {
		do 						{	
			//record the last taskWCRT, get ready to compare with the current taskWCRT
			temporaryWCRT = taskWCRT;	
			double totalPreemptionTime = 0;
		
		for (Task eachtask : taskListBySched) {
			String prioValue = eachtask.getCustomProperties().get("priority").toString();
			
			if(eachtask != task ){
				
			//字典顺序，"1">""2">"3">"4".....
			//但是实际上，通过下面的比较器，"1"-"2" = -1,务必要注意
			//Str1.compareTo(Str2)返回Str1-Str2的值，且结果为int类型
			//Here, we take in consideration of greater and equal to the specified Task
			int diff = prioValue.compareTo(taskPriorityValue);
			if (diff <= 0) {
				// get "Cj" --> wcetHihgerPrior
				long wcetICHigherPrior = getWCETinIC(ctx, eachtask);
				wcetHigherPrior = runUnitConversion(ctx, wcetICHigherPrior);
				//totalPreemptionTime += wcetHigherPrior; 
				
				//get task name
				String eachtaskName = eachtask.getName();
				
				//get period Pj
				long periodValue = 0;
				EList<Stimulus> stimulusList = eachtask.getStimuli();
				for (Stimulus stimulus : stimulusList) {
					PeriodicStimulus periodicStimulus = (PeriodicStimulus) stimulus; 
					
					Time period = periodicStimulus.getRecurrence();
					//Take care, the default Unit must be mS
					periodValue = period.getValue().longValue();
					}
				
				// "[△/Pj]" term, take the Upper Limit
				double cycNumber = taskWCRT/periodValue;
				double cyclesNumber = Math.ceil(cycNumber);
				this.log.info("The upper preemption times from task '" + eachtaskName + "'is " + cyclesNumber);
				logger.info("The upper preemption times from task '" + eachtaskName + "'is " + cyclesNumber);
				
				//"[△/Pj]*Cj" term
				preempTime = cyclesNumber * wcetHigherPrior;
				
				// "∑ [△/Pj]*Cj" term
				totalPreemptionTime += preempTime; 
				this.log.info("The totalpreemption time is :" + totalPreemptionTime);
				logger.info("The totalpreemption time is :" + totalPreemptionTime);
			}
			// "∑ [△/Pj]*Cj" term
			//totalPreemptionTime += preempTime; 
			//this.log.info("The totalpreemption time is :" + totalPreemptionTime);
			}
		}
		// "∑ [△/Pj]*Cj" term
		//totalPreemptionTime += preempTime; 
		taskWCRT = initialWCRT + totalPreemptionTime;
		//return totalPreemptionTime;
		
		//Judge if WCRT of task equals to last Iteration
	    equalJudge = equal(taskWCRT, temporaryWCRT);
		
	    //If WCRT < Period or WCRT do not change
		} while (taskWCRT < taskPeriodValue && !equalJudge);
	//	} while (false);
		
		//this.log.info("The final WCRT value is :" + temporaryWCRT);
		
		return temporaryWCRT;
		//return taskWCRT;
	}
	
	// method to return a scheduler from the specified task
	private Scheduler getTaskScheduler(Task task, Context ctx) {
		EList<TaskAllocation> taskAllocationList = getAmaltheaModel(ctx).getMappingModel().getTaskAllocation();
		
		Scheduler scheduler = null;
		for (TaskAllocation taskAllocation : taskAllocationList) {
			
			Task allocatedTask = taskAllocation.getTask();
			if (allocatedTask == task) {
				
				 scheduler = taskAllocation.getScheduler(); 
			}
		}
		return scheduler;
	}
	
	// method to judge, whether the two variable are equal
	private boolean equal(double currentWCRT, double lastWCRT) {
		
		double diff = currentWCRT - lastWCRT;
		
		if (Math.abs(diff) <= 0.01) {
			return true;
		} else {
			return false;
		}
	}

	
	//Method to calculate BCRT of a task
	private double getTaskBCRT(Task task, Context ctx, HashMap<String, ArrayList<Task>> sortedTasksByScheduler) {
		//Information from Log to File	
		Logger logger = Logger.getLogger("MyLog");  
	    FileHandler fh = null;  
	    
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("D:\\1\\out1.txt");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

		
		String taskName = task.getName();
		// get TaskList according to the same allocated Scheduler
		String schedulerString = getTaskScheduler(task, ctx).toString();
		ArrayList<Task> taskListBySched = sortedTasksByScheduler.get(schedulerString);
		String taskPriorityValue = task.getCustomProperties().get("priority").toString();
		
		//Definition BCET of task with higher Priority as task, which is the parameter in this method
		double bcetHigherPrior = 0;
		
		//get BCET of this specified task, which is the parameter of this method
		long taskBCETinIC = getBCETinIC(ctx, task);
		double taskBCET = runUnitConversion(ctx, taskBCETinIC);
		
		//We dont need Blocking Time for calculating BCRT
		//ArrayList<Label> writeLabelList = getWriteLabelList(task);
		//double blockingTime = calculateBlockingTime(writeLabelList, ctx);
		
		//get Period of the specified task,which is one parameter in this method
		long taskPeriodValue = 0;
		TimeUnit periodUnit = null;
		EList<Stimulus> taskStimulusList = task.getStimuli();
		for (Stimulus stimulus : taskStimulusList) {
			PeriodicStimulus periodicStimulus = (PeriodicStimulus) stimulus; 
			
			Time period = periodicStimulus.getRecurrence();
			//Take care, the default Unit must be mS
			taskPeriodValue = period.getValue().longValue();
			periodUnit = period.getUnit();
			}
		
		//this.log.info("The period of the Specified Task '" + taskName + "' is :" + taskPeriodValue + " " + periodUnit);
		
		//Initialization BCRT of the specified task
		//This Value should be a constant, which represents ONLY BCET of the specified TASK
		double initialBCRT = taskBCET;
		//This variable records the last taskBCRT, get ready to compare with the current taskBCRT
		double temporaryBCRT = 0;
		//this.log.info("The WCRT of task '"+ task + "' is :" + taskWCRT);
		//double preempTime = 0;
		
		// this Value is used to break do..while{} Loop, when BCRT do not change anymore
		// Value Initialization
		boolean equalJudge = false;
		
		//Initializing taskWCRT, which changes with Cycles Number of preemptive Task
		double taskBCRT = initialBCRT;
		
		//ITERATIVE CALCULATION ALGORITHME
		do 						{	
			//record the last taskBCRT, get ready to compare with the current taskWCRT
			temporaryBCRT = taskBCRT;
			//Initializing Total preemptive Time to Zero before each iteration
			double totalPreemptionTime = 0;
		
		for (Task eachtask : taskListBySched) {
			String prioValue = eachtask.getCustomProperties().get("priority").toString();
			
			if(eachtask != task){
			String eachtaskName = eachtask.getName();
			//字典顺序，"1">""2">"3">"4".....
			//但是实际上，通过下面的比较器，"1"-"2" = -1,务必要注意
			//Str1.compareTo(Str2)返回Str1-Str2的值，且结果为int类型
			int diff = prioValue.compareTo(taskPriorityValue);
			if (diff <= 0) {
				// get "Cj" --> bcetHihgerPrior
				long bcetICHigherPrior = getBCETinIC(ctx, eachtask);
				bcetHigherPrior = runUnitConversion(ctx, bcetICHigherPrior);
				//totalPreemptionTime += wcetHigherPrior; 
				
				//get period Pj
				long periodValue = 0;
				EList<Stimulus> stimulusList = eachtask.getStimuli();
				for (Stimulus stimulus : stimulusList) {
					PeriodicStimulus periodicStimulus = (PeriodicStimulus) stimulus; 
					
					Time period = periodicStimulus.getRecurrence();
					//Take care, the default Unit must be mS
					periodValue = period.getValue().longValue();
					}
				
				// get gcd(Pi,Pj) between the specified Task and the current task in For-Loop
				double gcdinmS = GCD(taskPeriodValue, periodValue);
				//this.log.info("GCD between Specified Task and Current Task is :" + gcdinmS);
				//Recursion for △j, get BCRT of the task, which has higher priority as the specified task, that is the parameter of this method
				double bcrtHigherPriority = getTaskBCRT(eachtask, ctx, sortedTasksByScheduler);
				
				// [  △j/gcd(Pi,Pj) ]  term, round down 
				double temporaryValue = bcrtHigherPriority/gcdinmS;
				long floorValue = (long) Math.floor(temporaryValue);
				//this.log.info("The floor Value is :" + floorValue);
				
				// max[1,[△/gcd(Pi,Pj)]]
				long maxValue = Math.max(1, floorValue);
				//this.log.info("The Max Value is :" + maxValue);
		
				//max[ , ]*gcd() term
				double maxGCD = maxValue * gcdinmS;  
				//this.log.info("The Multiplied Value is :" + maxGCD);

				// xj, the next activation time
				double nextActivTime = periodValue - maxGCD;
				
				// ( △ i - xj ) term
				double differValue = taskBCRT - nextActivTime;
				
				// max[0 , △i-xj] term
				double maxDiffer = Math.max(0, differValue);
				double cyclesNumber = maxDiffer/periodValue;
				//  [  max / Pj ] term , taking the upper Limit
				double maxCycNum = Math.ceil(cyclesNumber);
				this.log.info("The Upper Preemption times from Task '" + eachtaskName + "' is :" + maxCycNum);
				logger.info("The Upper Preemption times from Task '" + eachtaskName + "' is :" + maxCycNum);
				// [ max / Pj ]  * Cj term, preemption time of current Task
				double preempTime = maxCycNum * bcetHigherPrior;
				
				//"∑ [max / Pj] * Cj" term
				totalPreemptionTime += preempTime; 
				this.log.info("The total preemption time for BCRT of Task '" + taskName + "' is :" + totalPreemptionTime);
				logger.info("The total preemption time for BCRT of Task '" + taskName + "' is :" + totalPreemptionTime);
				
				// "[△/Pj]" term, take the Upper Limit
				//double cycNumber = taskWCRT/periodValue;
				//double cyclesNumber = Math.ceil(cycNumber);
				//this.log.info("Cycles Number is" + cyclesNumber);
				
				//"[△/Pj]*Cj" term
				//preempTime = cyclesNumber * wcetHigherPrior;
				
				// "∑ [△/Pj]*Cj" term
				//totalPreemptionTime += preempTime; 
				//this.log.info("The totalpreemption time is :" + totalPreemptionTime);
			}
			}
		}
		
		taskBCRT = initialBCRT + totalPreemptionTime;
		//return totalPreemptionTime;
		
		//Judge if WCRT of task equals to last Iteration
	    equalJudge = equal(taskBCRT, temporaryBCRT);
		
	    //If BCRT < Period or BCRT do not change
		} while (taskBCRT < taskPeriodValue && !equalJudge);
	//	} while (false);
		
		//this.log.info("The final BCRT value is :" + temporaryBCRT);
		
		return temporaryBCRT;
		//return taskWCRT;

	}
	
	//get gcd(Pi,Pj)
	//最大公约数必然是整数
	public long GCD(long a, long b) {
		   if (b==0) return a;
		   return GCD(b,a%b);
		}
	
	//get "Computing Power" Info from CoreType
	private Value getComputingPower(Context ctx) {
		EList<Core> coreList = getCoreList(ctx);
				
		CoreType coreType = null;
		EMap<String, Value> mapCoreTypeCustomProperty = null;
		Value computingPower = null;
		for (Core core : coreList) {
					
			coreType = core.getCoreType();
			mapCoreTypeCustomProperty = coreType.getCustomProperties();
			if (mapCoreTypeCustomProperty.containsKey("Computing Power")) {
						
				computingPower = mapCoreTypeCustomProperty.get("Computing Power");
				}
		}
				
		return computingPower;
				
	}

	//get "Idle Power" Info from CoreType
	private Value getIdlePower(Context ctx) {
		EList<Core> coreList = getCoreList(ctx);
		
		CoreType coreType = null;
		EMap<String, Value> mapCoreTypeCustomProperty = null;
		Value idlePower = null;
		for (Core core : coreList) {
			
			coreType = core.getCoreType();
			mapCoreTypeCustomProperty = coreType.getCustomProperties();
			//containsKey只是判断该map中是否含有指定的Key
			if (mapCoreTypeCustomProperty.containsKey("Idle Power")) {
				//String powerMode = "Idel Power";
				//idlePower = mapCoreTypeCustomProperty.get("Idle Power");
				//idlePower = mapCoreTypeCustomProperty.get(powerMode);
				idlePower = mapCoreTypeCustomProperty.get("Idle Power");
				}
		}
		
		return idlePower;
		}
	
	
	
	
}
