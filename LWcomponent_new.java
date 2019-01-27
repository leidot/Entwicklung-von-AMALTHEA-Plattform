package org.eclipse.app4mc.amalthea.example.workflow.components;

import java.lang.management.MemoryType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.xml.resolver.apps.resolver;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.CallGraph;
import org.eclipse.app4mc.amalthea.model.CallSequence;
import org.eclipse.app4mc.amalthea.model.CallSequenceItem;
import org.eclipse.app4mc.amalthea.model.Core;
import org.eclipse.app4mc.amalthea.model.CoreType;
import org.eclipse.app4mc.amalthea.model.Counter;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.DataSizeUnit;
import org.eclipse.app4mc.amalthea.model.Deviation;
import org.eclipse.app4mc.amalthea.model.ECU;
import org.eclipse.app4mc.amalthea.model.Frequency;
import org.eclipse.app4mc.amalthea.model.FrequencyUnit;
import org.eclipse.app4mc.amalthea.model.GraphEntryBase;
import org.eclipse.app4mc.amalthea.model.HWModel;
import org.eclipse.app4mc.amalthea.model.HwSystem;
import org.eclipse.app4mc.amalthea.model.Instructions;
import org.eclipse.app4mc.amalthea.model.InstructionsDeviation;
import org.eclipse.app4mc.amalthea.model.InterProcessStimulus;
import org.eclipse.app4mc.amalthea.model.InterProcessTrigger;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.LabelAccess;
import org.eclipse.app4mc.amalthea.model.LabelAccessEnum;
import org.eclipse.app4mc.amalthea.model.LongObject;
import org.eclipse.app4mc.amalthea.model.Memory;
import org.eclipse.app4mc.amalthea.model.Microcontroller;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.Prescaler;
import org.eclipse.app4mc.amalthea.model.Quartz;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.RunnableCall;
import org.eclipse.app4mc.amalthea.model.RunnableInstructions;
import org.eclipse.app4mc.amalthea.model.RunnableItem;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.TaskRunnableCall;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.Value;
import org.eclipse.app4mc.amalthea.workflow.core.Context;
import org.eclipse.app4mc.amalthea.workflow.core.WorkflowComponent;
import org.eclipse.app4mc.amalthea.workflow.core.exception.WorkflowException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.change.ChangePackage.Literals;
import org.eclipse.swt.internal.mozilla.Execute;
import org.eclipse.swt.internal.win32.MCHITTESTINFO;
import org.eclipse.ui.keys.Key;


public class LWcomponent_new extends WorkflowComponent{
	
	@Override
	protected void runInternal(Context ctx) throws WorkflowException {
		// TODO Auto-generated method stub
		
		/*+++++++++++++++++++++++++
		 * Operation in SWModel
		 ++++++++++++++++++++++++*/
		// some checking if sw model is available
		if (null == getAmaltheaModel(ctx).getSwModel()) {
			
			throw new WorkflowException("No proper SWModel available!");
		}
		this.log.info("Number of tasks in model: " + getAmaltheaModel(ctx).getSwModel().getTasks().size());
		this.log.info("Number of tasks in model: " + getAmaltheaModel(ctx).toString());
 /*     
  *     //Here are Code, where i use to learn how to find every parameter from SwModel 
		
		//get TaskList in SWModel
		EList<Task> taskList = getTasks(ctx);
		this.log.info("taskList in model: " + taskList);
		//CallSequenceItem
		EList<CallSequenceItem> callSeq = getCallList(ctx);
		this.log.info("taskList in model: " + callSeq);
		//RunnableItem 沉默这一段，是为了避免主函数中出现双重定义的跳错
		EList<RunnableItem> runnableItem = getRunnableItem(ctx);
		this.log.info("runnableitem in runnable: " + runnableItem);
		//get LabelAccessMode, exactly read or write or undefined
		String labelAccessMode = getlabelAccessMode(ctx) ;
		this.log.info("labe access mode in runnable: " + labelAccessMode);
		//get Label name
		String label = getLabelName(ctx);
		this.log.info("name of labe: " + label);
		//get Label Value, exactly number of Unit(Bit, Byte, ...)
		BigInteger labelValue = getLabelValue(ctx);
		this.log.info("value of labe without Unit: " + labelValue);
		//get Label Unit, exactly Bit, Byte, Kbyte,...
		DataSizeUnit labelUnit = getLabelUnit(ctx);
		this.log.info("unit of label: " + labelUnit);
		//get Instructions
		Instructions instructionsUpperBound = getInstructions(ctx);
		this.log.info("this is instructions in a single Runnable: " + instructionsUpperBound);
		//get Deviation UpperBound
		LongObject deviationUpperBound = getDeviationUpperBound(ctx);
		this.log.info("Instruction Upper Bound of single Runnable: " + deviationUpperBound);
		//get Deviation LowerBound
		LongObject deviationLowerBound = getDeviationLowerBound(ctx);
		this.log.info("Instruction Upper Bound of single Runnable: " + deviationLowerBound);
		//get Period from Task
		Time period = getTaskPeriod(ctx);
		this.log.info("Task's Period: " + period);
		//get Counter from Task, if this task has a InterProcessTrigger for itself
		Counter counter = getTaskCounter(ctx);
		this.log.info("This Task will be acitivated by : " + counter +"-th Processing of precedent Task");
*/
		
		//Build a set of Task and its InterProcessTrigger, which will activate InterProcessStimulus for its subsequent Task
		//With this method we can find the precedent Task of current Task, which is activated through the InterProcessStimulus
		TreeMap<String, String> setTaskWithInterProcessTrigger = setTaskwithInterProcessTrigger(ctx);
		this.log.info("This Set is : " + setTaskWithInterProcessTrigger );

		
		
		/*+++++++++++++++++++++++++
		 * Operation in HWModel
		 ++++++++++++++++++++++++*/
  	// some checking if sw model is available
		if (null == getAmaltheaModel(ctx).getHwModel()) {
			
			throw new WorkflowException("No proper HWModel available!");
		}
		//get HwSystem from HwModel
		HwSystem hwSystem = getHwSystem(ctx);
		this.log.info("Hardware System in HWModel: " + hwSystem);
		//get ECUList from HwSystem
		EList<ECU> ecuList = getEcuList(ctx);
		this.log.info("All ECUs in HwSystem: " + ecuList);
		//get MicrocontrollerList from ECU
		EList<Microcontroller> microcontrollerList = getMicrocontrollerList(ctx);
		this.log.info("All Microcontroller in all ECUs: " + microcontrollerList);
		//get CoreList from Microcontroller
		EList<Core> coreList = getCoreList(ctx);
		this.log.info("All Cores in all Microcontrollers: " + coreList);
		//get IPC from Core (actually from CoreType)
		float instructionPerCycle = getIPC(ctx);
		this.log.info("IPC of the CoreType: " + instructionPerCycle);
		//get ClockRatio from Prescaler, which is from Core
		double clockRatio = getPrescalerClockRatio(ctx);
		this.log.info("ClockRatio of the Prescaler: " + clockRatio);
		//get Frequency of Quartz
		Frequency frequency = getPrescalerQuartzFrequency(ctx);
		this.log.info("Frequency of this Quartz: " + frequency);
		//get Value and Unit of Frequency of Quartz
		double coreFrequency = getCoreFrequency(ctx);
		FrequencyUnit frequencyUnit = getFrequencyUnit(ctx);
		this.log.info("FrequencyValue is: " + coreFrequency + " The Unit is :" + frequencyUnit);
		
		//get "Computing Power" Info from CoreType
		Value computingPower = getComputingPower(ctx);
		this.log.info("The Value of Computing Power is: " + computingPower);
		//get "Idle Power" Info from CoreType
		Value idlePower = getIdlePower(ctx);
		this.log.info("The Value of Idle Power is: " + idlePower);
		
		//get Data Rate from MemoryType, which is from Memory under HwSystem
		Value dataRateUnderHwSystem = getDataRateUnderHwSystem(ctx);
		this.log.info("The Value of Data Rate from Memory under HwSystem is: " + dataRateUnderHwSystem);
		//get Data Rate from MemoryType, which is from Memory under ECU
		Value dataRateUnderEcu = getDataRateUnderEcu(ctx);
		this.log.info("The Value of Data Rate from Memory under ECU is: " + dataRateUnderEcu);
		//get Data Rate from MemoryType, which is from Memory under Microcontroller
		Value dataRateUnderMicrocontroller = getDataRateUnderMicrocontroller(ctx);
		this.log.info("The Value of Data Rate from Memory under Microcontroller is: " + dataRateUnderMicrocontroller);
		//get Data Rate from MemoryType, which is from Memory under Core
		Value dataRateUnderCore = getDataRateUnderCore(ctx);
		this.log.info("The Value of Data Rate from Memory under Core is: " + dataRateUnderCore);

		
		/*+++++++++++++++++++++++++++++++++++++++++++++++++++
		 * ++++++++++++++++++++++++++++++++++++++++++++++++++
		 * Main Function starts here
		 ++++++++++++++++++++++++++++++++++++++++++++++++++++
		 ++++++++++++++++++++++++++++++++++++++++++++++++++++*/	
		
		//Sorting Tasks according to Priority level ("1", "2",..."9", from high to low)	
		//In Amalthea model we shall limit the priority of task between 1 and 9, because we use String as data type, which makes a difference in comparing data
		SortedMap<String, ArrayList<Task>> sortedTasksByPrio = getSortedMap(ctx);
		//Build a set of Task and its InterProcessTrigger, which will activate InterProcessStimulus for its subsequent Task
		//With this method we can find the precedent Task of current Task, which is activated through the InterProcessStimulus
		TreeMap<String, String> mapOfTaskWithInterProcessTrigger = setTaskwithInterProcessTrigger(ctx);
	
		for (String prio : sortedTasksByPrio.keySet()) {
			ArrayList<Task> arrayList = sortedTasksByPrio.get(prio);
			this.log.info("Task List of this priority levle" + prio + " is : " + arrayList);
			this.log.info("Remember to set MHz as default Unit for Frequency in CoreType");

			for (Task task : arrayList) {
				String taskName = task.getName();
				long taskWCET = 0;
				long taskBCET = 0;
				
				EList<Stimulus> taskStimuliList = task.getStimuli();
				//Despite we have a list of Stimulus for each task, but actually in Task model we have just one Stimulus (maximal one Periodic Stimulus and one InterProcessTrigger Stimulus)in the list
				for (Stimulus stimulus : taskStimuliList) {
					if (PeriodicStimulus.class.isInstance(stimulus)) {
						PeriodicStimulus periodStimulus = (PeriodicStimulus) stimulus;
						
						//get WCET and BCET of single Task
						taskWCET = getWCETinIC(ctx, task);
						taskBCET = getBCETinIC(ctx, task);
						this.log.info("WCET in InstructionCycles is :" + taskWCET + ". BCET in InstructionCycles is :" + taskBCET);
						//Time Unit Conversion from Instruction Cycles to mS
						double taskWCETinmS = runUnitConversion(ctx, taskWCET);
						double taskBCETinmS = runUnitConversion(ctx, taskBCET);
						this.log.info("WCET in Unit mS is :" + taskWCETinmS + " mS. BCET in Unit mS is :" + taskBCETinmS + " mS");

						//get Period of this Task
						Time period = periodStimulus.getRecurrence();
						this.log.info("Period of this Task is :" + period);
						TimeUnit periodUnit = period.getUnit();
						this.log.info("The Unit of this Task is :" + periodUnit);
						
						//get Write Access Time of Single Task
						//We assume that Write Access of Critical Section will be blocked by other Task with lower Priority, for Reading Access would not be blocked
						//Here we assume, that Write Access to all Critical Section during each task would lead to Blocking.
						//String writeLabelList = getWriteLabelList(task);
						ArrayList<Label> writeLabelList = getWriteLabelList(task);
						this.log.info("The List of Write Label in task '" + taskName +"' is :"+ writeLabelList);
						//Calculate the total Blocking Time of this Task
						//Through Method calculateBlockingTime we have set the Unit of blockingTime in milliSecond
						double blockingTime = calculateBlockingTime(writeLabelList, ctx);
						this.log.info("The total Blocking Time of Task '" + taskName +"' is :"+ blockingTime + " mS" + "\r");

						
					}
				
			
				else if (InterProcessStimulus.class.isInstance(stimulus)) {
						 InterProcessStimulus interProcStimulus = (InterProcessStimulus) stimulus;
						 
						//get WCET and BCET of single Task
						taskWCET = getWCETinIC(ctx, task);
						taskBCET = getBCETinIC(ctx, task); 
						this.log.info("WCET in InstructionCycles is :" + taskWCET + ". BCET in InstructionCycles is :" + taskBCET);
						//Time Unit Conversion from Instruction Cycles to mS
						double taskWCETinmS = runUnitConversion(ctx, taskWCET);
						double taskBCETinmS = runUnitConversion(ctx, taskBCET);
						this.log.info("WCET in Unit mS is :" + taskWCETinmS + " mS. BCET in Unit mS is :" + taskBCETinmS + " mS");
 
						//get Period of this Task
						//Here we assume, that in this Trigger list there is only one InterProcessTrigger. In other words, this task will only be activated by one another task
						EList<InterProcessTrigger> interProcessTriggerList = interProcStimulus.getExplicitTriggers();
						Counter counter = interProcStimulus.getCounter();
						for (InterProcessTrigger interProcessTrigger : interProcessTriggerList) {
							interProcessTrigger = InterProcessTrigger.class.cast(interProcessTrigger);
							
							String interProcessTriggerString = interProcessTrigger.toString();
							String taskNameFromMap = mapOfTaskWithInterProcessTrigger.get(interProcessTriggerString);
							
							//The Counter comes from InterProcessStimulus, this counter means how often is the current task activated by its precedent task
							long counterValue = counter.getPrescaler();
							this.log.info("counterValue is  :" + counterValue);
							//this.log.info("Name of this Task is :" + taskNameFromMap);
							this.log.info("Name of this Task is :" + taskName);
							//get the precedent Task through its name from map mapOfTaskWithInterProcessTrigger
							Task precedentTask = getTaskThroughTaskName(ctx, taskNameFromMap);
							this.log.info("The precedent Task of current Task is -------->" + precedentTask );
						
							Time periodOfPrecedentTask = getTaskPeriod(precedentTask);
							TimeUnit periodUnitOfPrecedentTask = periodOfPrecedentTask.getUnit();
							this.log.info("The Period of the precedent Task '" + "'" + precedentTask + " is -------->" + periodOfPrecedentTask );
							//This conversion from BigInteger to long may not work or false result
						
							long periodOfPrecedentTaskValue = periodOfPrecedentTask.getValue().longValue();
							long periodOfCurrentTask = periodOfPrecedentTaskValue * counterValue;
							this.log.info("The Period of this current Task is -------->" + periodOfCurrentTask + " " + periodUnitOfPrecedentTask );

							
							//get Write Access Time of Single Task
							//We assume that Write Access of Critical Section will be blocked by other Task with lower Priority, for Reading Access would not be blocked
							//Here we assume, that Write Access to all Critical Section during each task would lead to Blocking.
							//String writeLabelList = getWriteLabelList(task);
							ArrayList<Label> writeLabelList = getWriteLabelList(task);
							this.log.info("The List of Write Label in task '" + taskName +"' is :"+ writeLabelList);
							//Calculate the total Blocking Time of this Task
							//Through Method calculateBlockingTime we have set the Unit of blockingTime in milliSecond
							double blockingTime = calculateBlockingTime(writeLabelList, ctx);
							this.log.info("The total Blocking Time of Task '" + taskName +"' is :"+ blockingTime + " mS" + "\r");
							
						}
						
						
						
						}
					//processTaskInterProcessStimulus(prio, taskName, task, stimulus);
				}
			
			//Display WCET and BCET of each task 	
			//this.log.info("WCET of this Task '" + taskName +"' is : " + taskWCET );
			//this.log.info("BCET of this Task '" + taskName +"' is : " + taskBCET );
			//this.log.info("The Unit is 'Instrcution Cycles'");
			
			//Unit Conversion from Instruction Cycles to mS (millisecond)
			//long executionCycles = taskWCET;
			//double WCETinmS = runUnitConversion(ctx, executionCycles);
			//this.log.info("WCET of this Task '" + taskName +"' in MilliSecond is : " + WCETinmS + " mS");
			//this.log.info("The Unit is MilliSecond, mS");

			//executionCycles = taskBCET;
			//double BCETinmS = runUnitConversion(ctx, executionCycles);
			//this.log.info("BCET of this Task '" + taskName +"' in MilliSecond is : " + BCETinmS + " mS");
			//this.log.info("The Unit is MilliSecond, mS" + "\r");


					
			}	
		} 
		

	
	
		
		
	}

		
	// read all tasks in SWModel and return a list of tasks
		private EList<Task> getTasks(Context ctx) {
			final Amalthea amaltheaModel = getAmaltheaModel(ctx);

			assert null != amaltheaModel;

			this.log.info("Starting to read AMALTHEA model...");

			final SWModel swModel = amaltheaModel.getSwModel();

			EList<Task> taskList = swModel.getTasks();

			return taskList;
		}
		
	
/*		// Search CallSequence from each Task and cast every possible into CallSequence
		private EList<CallSequenceItem> getCallList(Context ctx) {

			EList<Task> taskList = getTasks(ctx);

			
			//this.log.info("Task List without sort:" + taskList.toString());

			//SortedMap<String, ArrayList<Task>> sortedTasksByPrio = new TreeMap<>();
			//CallSequence cseq = null;
			// for each task in taskList take the following operation, and we get a list of tasks for each level of "Priority"
			//for (Task task : taskList) {
			    //We must use Value of Priority in form of String in Amalthea Model
			//	Value prioValue = task.getCustomProperties().get("priority");
			//	String prioStr = prioValue.toString();
			     //add, just add a task in ArrayList<Task>, not in TreeMap
			//	if (sortedTasksByPrio.containsKey(prioStr)) {
			//		sortedTasksByPrio.get(prioStr).add(task);
			//	} else {
			//		ArrayList<Task> prioTasks = new ArrayList<Task>();
			//	    prioTasks.add(task);
			//		sortedTasksByPrio.put(prioStr, prioTasks);
			//	}

			//}
					
			
					
			//This is main Process for If-Branch
		//	for (String prio : sortedTasksByPrio.keySet()) {
		//		ArrayList<Task> arrayList = sortedTasksByPrio.get(prio);
		//		for (Task task : arrayList) {
		//			String taskName = task.getName();
		//			EList<Stimulus> taskStimuliList = task.getStimuli();
		//			for (Stimulus stimulus : taskStimuliList) {
		//				if (PeriodicStimulus.class.isInstance(stimulus)) {
		//					PeriodicStimulus periodStimulus = (PeriodicStimulus) stimulus;
		//					processTaskPeriodicStimulus(prio, taskName, task, periodStimulus);
		//				} else if (InterProcessStimulus.class.isInstance(stimulus)) {
		//					InterProcessStimulus interProcStimulus = (InterProcessStimulus) stimulus;
		//					processTaskInterProcessStimulus(prio, taskName, task, stimulus);
		//				}
		//			}
		//		}
		//	}

		//	this.log.info("Task List WITH sort:" + sortedTasksByPrio.toString());

			EList<Task> taskList = getTasks(ctx);
			CallSequence cseq = null;
			
			for (Task task : taskList) {	
			  //invoation of CallGraph
		      CallGraph cgraph = task.getCallGraph();
		      for (GraphEntryBase entry : cgraph.getGraphEntries()) {
				 // Cast each entry convert into CallSequence
				 cseq = CallSequence.class.cast(entry);
			 	}
			}
			 //return null;
			return cseq.getCalls();
			
		}
*/
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
		
/*	++++++++++++++++++++++++++		
		private void processTaskInterProcessStimulus(String prio, String taskName, Task task, Stimulus stimulus) {
			// TODO Auto-generated method stub
					
		}

		private void processTaskPeriodicStimulus(String prio, String taskName, Task task, PeriodicStimulus periodStimulus) {
			// TODO Auto-generated method stub
					
		}
+++++++++++++++++++++++++++++*/
		
		//通过这个函数我们得到每个CallSequence当中的RunnableList(faulse)
		//通过这个函数我们得到一个Runnable的RunnableItem
/*		private EList<RunnableItem> getRunnableItem (Context ctx){
			EList<CallSequenceItem> callSeq = getCallList(ctx);
			Runnable calledRunnable = null;
			//EList<RunnableItem> runnableItem = null;
			for (CallSequenceItem callSequenceItem : callSeq) {
				if (TaskRunnableCall.class.isInstance(callSequenceItem)) {
					TaskRunnableCall taskCall = TaskRunnableCall.class.cast(callSequenceItem);
					//下面的这句话可能引发错误
				   //org.eclipse.app4mc.amalthea.model.Runnable calledRunnable = taskCall.getRunnable();
					calledRunnable = taskCall.getRunnable();
					}	
				
				//if (InterProcessTrigger.class.isInstance(callSequenceItem)) {
					//InterProcessTrigger interProTrigg = InterProcessTrigger.class.cast(callSequenceItem);
					
					//Stimulus interTrigger = interProTrigg.getStimulus();
				//}
				
			}
			return calledRunnable.getRunnableItems();
			
		}
*/		
		//通过这个函数我们得到一个InterProcessTrigger的某些属性—??
		/*		private EList<RunnableItem> getRunnableItem (Context ctx){
					EList<CallSequenceItem> callSeq = getCallList(ctx);
					Runnable calledRunnable = null;
					EList<RunnableItem> runnableItem = null;
					for (CallSequenceItem callSequenceItem : callSeq) {
						if (TaskRunnableCall.class.isInstance(callSequenceItem)) {
							TaskRunnableCall taskCall = TaskRunnableCall.class.cast(callSequenceItem);
							//下面的这句话可能引发错误
						   //org.eclipse.app4mc.amalthea.model.Runnable calledRunnable = taskCall.getRunnable();
							calledRunnable = taskCall.getRunnable();
							}	
						
						//if (InterProcessTrigger.class.isInstance(callSequenceItem)) {
							//InterProcessTrigger interProTrigg = InterProcessTrigger.class.cast(callSequenceItem);
							
							//Stimulus interTrigger = interProTrigg.getStimulus();
						//}
						
					}
					return calledRunnable.getRunnableItems();
					
				}
		*/
			
	 /*
	  *+++++++++++++++++++++++++ 
	  *Operation for LabelAccess
	  *+++++++++++++++++++++++++
	  */
		//get LabelAccessMode, exactly write or read or undefined
/*		private String getlabelAccessMode(Context ctx) {
			EList<RunnableItem> runnaItemList = getRunnableItem(ctx); 
			
			LabelAccessEnum accessMode = null;
			for(RunnableItem runnableItem : runnaItemList)
			if (LabelAccess.class.isInstance(runnableItem) ) {
				LabelAccess labelAccess = LabelAccess.class.cast(runnableItem);
				
				accessMode = labelAccess.getAccess();
			}
			return accessMode.getLiteral();
		} 
*/		
/*		//get name of label, which is write type in each Runnable
		private String getLabelName(Context ctx) {
			EList<RunnableItem> runnaItemList = getRunnableItem(ctx); 
			Label label = null;
			String accessMode = null;
			for(RunnableItem runnableItem : runnaItemList) {
				if (LabelAccess.class.isInstance(runnableItem) ) {
					LabelAccess labelAccess = LabelAccess.class.cast(runnableItem);
					
					accessMode = getlabelAccessMode(ctx);
					if (accessMode=="write") {
						label = labelAccess.getData();
					}
					//label = labelAccess.getData();
				}
			} 
				
			return label.getName();
			
			}
*/		
		//get Value of this Label, for example this Label use 8 bit space in memory, then through this method we get the number 8.
/*		private BigInteger getLabelValue(Context ctx) {
			EList<RunnableItem> runnaItemList = getRunnableItem(ctx); 
			Label label = null;
			DataSize labelSize = null;
			String accessMode = null;
			for(RunnableItem runnableItem : runnaItemList) {
				if (LabelAccess.class.isInstance(runnableItem) ) {
					LabelAccess labelAccess = LabelAccess.class.cast(runnableItem);
					
					accessMode = getlabelAccessMode(ctx);
					if (accessMode=="write") {
						label = labelAccess.getData();
						labelSize = label.getSize();
					}
					
				}
			} 
				
			return labelSize.getValue();
			
			}
*/		
		//get Unit of this Label, for example this Label use 8 bit space in memory, then through this method we get the Unit bit.
/*		private DataSizeUnit getLabelUnit(Context ctx){
			EList<RunnableItem> runnaItemList = getRunnableItem(ctx); 
			Label label = null;
			DataSize labelSize = null;
			String accessMode = null;
			for(RunnableItem runnableItem : runnaItemList) {
				if (LabelAccess.class.isInstance(runnableItem) ) {
					LabelAccess labelAccess = LabelAccess.class.cast(runnableItem);
					
					accessMode = getlabelAccessMode(ctx);
					if (accessMode=="write") {
						label = labelAccess.getData();
						labelSize = label.getSize();
					}
					
				}
			} 
				
			return labelSize.getUnit();
			
		}
	*/		
		 /*
		  *++++++++++++++++++++++++++++++++++ 
		  *Operation for RunnableInstructions
		  *++++++++++++++++++++++++++++++++++
		  */
		//get Instructions from RunnableInstructions
/*		private Instructions getInstructions(Context ctx) {
			EList<RunnableItem> runnaItemList = getRunnableItem(ctx); 
			
			RunnableInstructions runnableInstructions = null;
			Instructions instructions = null;
			//InstructionsDeviation instructionDeviation = null;
			//Deviation<Instructions> instructionDeviation = null;
			
			for (RunnableItem runnableItem : runnaItemList) {
				if (RunnableInstructions.class.isInstance(runnableItem)) {
					runnableInstructions = RunnableInstructions.class.cast(runnableItem);
					
					instructions = runnableInstructions.getDefault();
					//instructionDeviation = runnableInstructions.getDefault();
					//runnableInstructions.
					}
			}
			//return instructionDeviation.getUpperBound();
			return instructions;
		} 
*/		
/*		//get UpperBound
		private LongObject getDeviationUpperBound(Context ctx) {
			Instructions instruction = getInstructions(ctx);
			
			InstructionsDeviation instructionDeviation = null;
			
			Deviation<LongObject> deviation = null;
			LongObject deviationUpperBound = null;
			if(InstructionsDeviation.class.isInstance(instruction)) {
				instructionDeviation = InstructionsDeviation.class.cast(instruction);
				
				deviation = instructionDeviation.getDeviation();
				deviationUpperBound = deviation.getUpperBound();
			}
			return deviationUpperBound;
		}
*/		
/*		//get LowerBound
		private LongObject getDeviationLowerBound(Context ctx) {
			Instructions instruction = getInstructions(ctx);
			
			InstructionsDeviation instructionDeviation = null;
			
			Deviation<LongObject> deviation = null;
			LongObject deviationLowerBound = null;
			if(InstructionsDeviation.class.isInstance(instruction)) {
				instructionDeviation = InstructionsDeviation.class.cast(instruction);
				
				deviation = instructionDeviation.getDeviation();
				deviationLowerBound = deviation.getLowerBound();
			}
			return deviationLowerBound;
		}
*/		
		/*
		  *++++++++++++++++++++++++++++++++++ 
		  *Searching for Period of Task
		  *++++++++++++++++++++++++++++++++++
		  */
		
		//get Period from Task, if this task has PeriodStimulus
		//Here i have changed the original function. Now through this function i can get Period from single periodic Task
		private Time getTaskPeriod(Task task) {
			//EList<Task> taskList = getTasks(ctx);
			EList<Stimulus> stimulusList = null;
			PeriodicStimulus periodicStimulus = null;
			InterProcessStimulus interProcessStimulus = null;
			Time period = null;
			//Counter counter = null;
			//for (Task task : taskList) {
				stimulusList = task.getStimuli();
				for (Stimulus stimulus : stimulusList) {
					if (PeriodicStimulus.class.isInstance(stimulus)) {
						periodicStimulus = PeriodicStimulus.class.cast(stimulus);
						
						period = periodicStimulus.getRecurrence();
					}
					//if (InterProcessStimulus.class.isInstance(stimulus)) {
						//interProcessStimulus = InterProcessStimulus.class.cast(stimulus);
						
						//counter = interProcessStimulus.getCounter();
						//period = counter*
					//}
				}
			//}
			return period;
		}
		
		//get Counter from Task, if this task has InterProcessStimulus
		private Counter getTaskCounter(Context ctx) {
			EList<Task> taskList = getTasks(ctx);
			EList<Stimulus> stimulusList = null;
			//PeriodicStimulus periodicStimulus = null;
			InterProcessStimulus interProcessStimulus = null;
			Time period = null;
			Counter counter = null;
			for (Task task : taskList) {
				stimulusList = task.getStimuli();
				for (Stimulus stimulus : stimulusList) {
					if (InterProcessStimulus.class.isInstance(stimulus)) {
						interProcessStimulus = InterProcessStimulus.class.cast(stimulus);
						
						counter = interProcessStimulus.getCounter();
						//period = counter*
					}
				}
			}
			return counter;
		}
		
		
		

		/*
		 * ++++++++++++++++++++++++++++++++++ 
		  *++++++++++++++++++++++++++++++++++ 
		  *Operation in HWModel
		  *Operation in HWModel
		  *++++++++++++++++++++++++++++++++++
		  *++++++++++++++++++++++++++++++++++
		  */
		
		// read the single HwSystem in HWModel 
		private HwSystem getHwSystem(Context ctx) {
			final Amalthea amaltheaModel = getAmaltheaModel(ctx);

			assert null != amaltheaModel;

			this.log.info("Starting to read AMALTHEA model...");

			final HWModel hwModel = amaltheaModel.getHwModel();

			HwSystem hardwareSystem = hwModel.getSystem();

			return hardwareSystem;
		}
	
		// get all ECU from HwSystem
		private EList<ECU> getEcuList(Context ctx) {
			HwSystem hwSystem = getHwSystem(ctx);
			
			EList<ECU> ecuList = hwSystem.getEcus();
			
			return ecuList;
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
		
		//get all Core from Microcontrollers
		private EList<Core> getCoreList(Context ctx) {
			EList<Microcontroller> microcontrollerList = getMicrocontrollerList(ctx);
			
			EList<Core> coreList = null;
			for (Microcontroller microcontroller : microcontrollerList) {
				
				coreList = microcontroller.getCores();
			}
				
			return coreList;
			
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
		//get Unit of Frequency
		private FrequencyUnit getFrequencyUnit(Context ctx) {
			Frequency frequency = getPrescalerQuartzFrequency(ctx);
			
			FrequencyUnit frequencyUnit = frequency.getUnit();
			return frequencyUnit;
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
		
		/*++++++++++++++++++++++++++++++
		 * Operation in searching Memory
		 +++++++++++++++++++++++++++++*/
		
		//get Data Rate from "Memory Type", the Data Rate is stored in CustomProperty, if Memory is under HwSystem
		private Value getDataRateUnderHwSystem(Context ctx) {
			HwSystem hwSystem = getHwSystem(ctx);
			
			EList<Memory> memoryList = hwSystem.getMemories();
			org.eclipse.app4mc.amalthea.model.MemoryType memoryType = null;
			EMap<String, Value> memoryTypeCustomProperty = null;
			Value dataRateValue = null;
			
			//String dataRateValue = null;
			//long dataRate = 0;
			
			for (Memory memory : memoryList) {
				
				memoryType = memory.getType();
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
			
		//get SortedMap from taskList
		private SortedMap<String, ArrayList<Task>> getSortedMap(Context ctx) {
			EList<Task> taskList = getTasks(ctx);
			this.log.info("Task List without sorting:" + taskList.toString());
					
			SortedMap<String, ArrayList<Task>> sortedTasksByPrio = new TreeMap<>();
			CallSequence cseq = null;
			for (Task task : taskList) {
				//We must use Value of Priority in form of String in Amalthea Model
				//In Amalthea Model we must limit the priority value to 9, exactly 1~9. Otherweise "10" is less than "6" because of String.
				//In Amalthea Model Priority :9 -> 1, exactly from highest to lowest
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
			return sortedTasksByPrio;
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
		
		//set a Map for task and its InterProcessTrigger, if this task has a InterProcessTrigger
		//private TreeMap<String, InterProcessTrigger> setTaskwithInterProcessTrigger(Context ctx) {
		private TreeMap<String, String> setTaskwithInterProcessTrigger(Context ctx) {
			EList<Task> taskList = getTasks(ctx);
			TreeMap <String, String> taskwithinterprocesstrigger = new TreeMap<>();
			CallSequence cseq = null;
			for (Task task : taskList) {
				String taskName = task.getName();
				
				InterProcessTrigger interProcessTrigger = null; 
				CallGraph callGraph = task.getCallGraph();
				EList<GraphEntryBase> entry = callGraph.getGraphEntries();
				for (GraphEntryBase graphEntryBase : entry) {
					if(CallSequence.class.isInstance(graphEntryBase))
						cseq  = CallSequence.class.cast(graphEntryBase);
						
					EList<CallSequenceItem> calledSequences = cseq.getCalls();
						for (CallSequenceItem callSequenceItem : calledSequences) {
							if (InterProcessTrigger.class.isInstance(callSequenceItem)) {
								interProcessTrigger = InterProcessTrigger.class.cast(callSequenceItem);
								
								String interProcessTriggerString = interProcessTrigger.toString();
								//taskwithinterprocesstrigger.put(taskName, interProcessTriggerString);
								taskwithinterprocesstrigger.put(interProcessTriggerString, taskName);
							}
						}
				}
			}	
				
			this.log.info("A set of Task and its InterProcessTrigger :" + taskwithinterprocesstrigger.toString());
			return taskwithinterprocesstrigger;
		}
		
		//search task, which has a InterProcessTrigger, according to existed taskName
		private Task getTaskThroughTaskName(Context ctx, String taskNameString) {
			EList<Task> taskList = getTasks(ctx);
			Task k = null;
			for (Task task : taskList) {
				String tName = task.getName();
				if (tName == taskNameString) {
					
				 k = task;
				}
			}
			return k;
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
}
