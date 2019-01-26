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
import org.eclipse.swt.internal.mozilla.Execute;
import org.eclipse.swt.internal.win32.MCHITTESTINFO;
import org.eclipse.ui.keys.Key;
//import org.eclipse.ui.keys.Key;

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
		//get TaskList in SWModel
		EList<Task> taskList = getTasks(ctx);
		this.log.info("taskList in model: " + taskList);
		//CallSequenceItem
		EList<CallSequenceItem> callSeq = getCallList(ctx);
		this.log.info("taskList in model: " + callSeq);
		//RunnableItem ��Ĭ��һ�Σ���Ϊ�˱����������г���˫�ض��������
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
		//get the set of Task and its InterProcessTrigger
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

		/*+++++++++++++++++++++++++
		 * Main Function starts here
		 +++++++++++++++++++++++++*/	
		
		//Sorting Tasks according to Priority level ("1", "2",..."9", from high to low)	
		//In Amalthea model we shall limit the priority of task between 1 and 9, because we use String as data type, which makes a difference in comparing datas.
		SortedMap<String, ArrayList<Task>> sortedTasksByPrio = getSortedMap(ctx);
		TreeMap<String, String> mapOfTaskWithInterProcessTrigger = setTaskWithInterProcessTrigger;
	
		for (String prio : sortedTasksByPrio.keySet()) {
			ArrayList<Task> arrayList = sortedTasksByPrio.get(prio);
			this.log.info("Task List of this priority levle" + prio + " is : " + arrayList);
			this.log.info("Remember to set MHz as Unit for Frequency in CoreType");

			for (Task task : arrayList) {
				String taskName = task.getName();
				long taskWCET = 0;
				long taskBCET = 0;
				
				EList<Stimulus> taskStimuliList = task.getStimuli();
				//Despite we have a list of Stimulus for each task, but actually in Amalthea model we have just one Stimulus in the list
				for (Stimulus stimulus : taskStimuliList) {
					if (PeriodicStimulus.class.isInstance(stimulus)) {
						PeriodicStimulus periodStimulus = (PeriodicStimulus) stimulus;
						
						//get WCET and BCET of single Task
						taskWCET = getWCETinmS(ctx, task);
						taskBCET = getBCETinmS(ctx, task);
						
						//get Period of this Task
						Time period = periodStimulus.getRecurrence();
						this.log.info("Period of this Task is :" + period);
						TimeUnit periodUnti = period.getUnit();
						this.log.info("The Unit of this Task is :" + periodUnti);

					}
				
			
				else if (InterProcessStimulus.class.isInstance(stimulus)) {
						 InterProcessStimulus interProcStimulus = (InterProcessStimulus) stimulus;
						 
						//get WCET and BCET of single Task
						taskWCET = getWCETinmS(ctx, task);
						taskBCET = getBCETinmS(ctx, task); 
						 
						//get Period of this Task
						//Here we assume, that in this Trigger list there is only one InterProcessTrigger. In other words, this task will only be activated by one another task
						EList<InterProcessTrigger> interProcessTriggerList = interProcStimulus.getExplicitTriggers();
						Counter counter = interProcStimulus.getCounter();
						for (InterProcessTrigger interProcessTrigger : interProcessTriggerList) {
							interProcessTrigger = InterProcessTrigger.class.cast(interProcessTrigger);
							
							String interProcessTriggerString = interProcessTrigger.toString();
							String taskNameFromMap = mapOfTaskWithInterProcessTrigger.get(interProcessTriggerString);
							
							long counterValue = counter.getPrescaler();
							this.log.info("counterValue is  :" + counterValue);
							this.log.info("Name of this Task is :" + taskNameFromMap);
							Task precedentTask = getTaskThroughTaskName(ctx, taskNameFromMap);
							this.log.info("The precedent Task of current Task is -------->" + precedentTask );
						
							Time periodOfPrecedentTask = getTaskPeriod(precedentTask);
							TimeUnit periodUnitOfPrecedentTask = periodOfPrecedentTask.getUnit();
							this.log.info("The Period of the precedent Task '" + "'" + precedentTask + " is -------->" + periodOfPrecedentTask );
							//This conversion from BigInteger to long may not work or false result
						
							long periodOfPrecedentTaskValue = periodOfPrecedentTask.getValue().longValue();
							long periodOfCurrentTask = periodOfPrecedentTaskValue * counterValue;
							this.log.info("The Period of this current Task is -------->" + periodOfCurrentTask + " " + periodUnitOfPrecedentTask );

							
						}
						
						
						
						}
					//processTaskInterProcessStimulus(prio, taskName, task, stimulus);
				}
			
			//Display WCET and BCET of each task 	
			this.log.info("WCET of this Task '" + taskName +"' is : " + taskWCET );
			this.log.info("BCET of this Task '" + taskName +"' is : " + taskBCET );
			this.log.info("The Unit is 'Instrcution Cycles'");
			//Unit Conversion from Instruction Cycles to mS (millisecond)
			long executionCycles = taskWCET;
			double WCETinmS = runUnitConversion(ctx, executionCycles);
			this.log.info("WCET of this Task '" + taskName +"' in MilliSecond is : " + WCETinmS + " mS");
			this.log.info("The Unit is MilliSecond, mS");

			executionCycles = taskBCET;
			double BCETinmS = runUnitConversion(ctx, executionCycles);
			this.log.info("BCET of this Task '" + taskName +"' in MilliSecond is : " + BCETinmS + " mS");
			this.log.info("The Unit is MilliSecond, mS" + "\r");


					
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
		
		//ͨ������������ǵõ�ÿ��CallSequence���е�RunnableList(faulse)
		//ͨ������������ǵõ�һ��Runnable��RunnableItem
/*		private EList<RunnableItem> getRunnableItem (Context ctx){
			EList<CallSequenceItem> callSeq = getCallList(ctx);
			Runnable calledRunnable = null;
			//EList<RunnableItem> runnableItem = null;
			for (CallSequenceItem callSequenceItem : callSeq) {
				if (TaskRunnableCall.class.isInstance(callSequenceItem)) {
					TaskRunnableCall taskCall = TaskRunnableCall.class.cast(callSequenceItem);
					//�������仰������������
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
		//ͨ������������ǵõ�һ��InterProcessTrigger��ĳЩ���ԡ�??
		/*		private EList<RunnableItem> getRunnableItem (Context ctx){
					EList<CallSequenceItem> callSeq = getCallList(ctx);
					Runnable calledRunnable = null;
					EList<RunnableItem> runnableItem = null;
					for (CallSequenceItem callSequenceItem : callSeq) {
						if (TaskRunnableCall.class.isInstance(callSequenceItem)) {
							TaskRunnableCall taskCall = TaskRunnableCall.class.cast(callSequenceItem);
							//�������仰������������
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
				//containsKeyֻ���жϸ�map���Ƿ���ָ����Key
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
			for (Memory memory : memoryList) {
				
				memoryType = memory.getType();
				memoryTypeCustomProperty = memoryType.getCustomProperties();
				
				if (memoryTypeCustomProperty.containsKey("Data Rate")) {
					
					dataRateValue = memoryTypeCustomProperty.get("Data Rate");
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
		
		//Method for calculating WCETinmS
		private long getWCETinmS(Context ctx, Task task) {
			//������һ���еĺ���getCallList�����⣬��ͬ��Task�õ��Ľ����Ȼ��һ����
			//EList<CallSequenceItem> callSequence = getCallList(ctx);
			//����Ĺ��캯������ȷ�ˣ���Ϊ���ǲ������βΣ������ǲ���Context
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
		
		//Method for calculating BCETinmS
		private long getBCETinmS(Context ctx, Task task) {
			//������һ���еĺ���getCallList�����⣬��ͬ��Task�õ��Ľ����Ȼ��һ����
			//EList<CallSequenceItem> callSequence = getCallList(ctx);
			//����Ĺ��캯������ȷ�ˣ���Ϊ���ǲ������βΣ������ǲ���Context
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
		
		
}
