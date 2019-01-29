package org.eclipse.app4mc.amalthea.example.workflow.components;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

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
		
		/*+++++++++++++++++++++++++
		 * Operation in SWModel
		 ++++++++++++++++++++++++*/
		// some checking if sw model is available
		if (null == getAmaltheaModel(ctx).getSwModel()) {
			
			throw new WorkflowException("No proper SWModel available!");
		}
		this.log.info("Number of tasks in model: " + getAmaltheaModel(ctx).getSwModel().getTasks().size());
		this.log.info("Number of tasks in model: " + getAmaltheaModel(ctx).toString());
		
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
		this.log.info("Start to find tasks in same Core: ");
		//here i need a private method, so i comment these sentences
	//	EList<TaskAllocation> taskAllocationList = getAmaltheaModel(ctx).getMappingModel().getTaskAllocation();
	//	for (TaskAllocation taskAllocation : taskAllocationList) {
	//		Scheduler scheduler = taskAllocation.getScheduler();
	//		this.log.info("The Scheduler is :" + scheduler);
	//	}
		
		/*+++++++++++++++++++++++++++++++++++++++++++++++++++
		 * ++++++++++++++++++++++++++++++++++++++++++++++++++
		 * Main Function starts here
		 ++++++++++++++++++++++++++++++++++++++++++++++++++++
		 ++++++++++++++++++++++++++++++++++++++++++++++++++++*/	
		
		//Sorting Tasks according to Priority level ("1", "2",..."9", from high to low)	
		//In Amalthea model we shall limit the priority of task between 1 and 9, because we use String as data type, which makes a difference in comparing data
		SortedMap<String, ArrayList<Task>> sortedTasksByPrio = getSortedMap(ctx);

		//Sorting Tasks according to TaskAllocation, where each task is allocated to a single Scheduler
		HashMap<String, ArrayList<Task>> sortedTasksBySched = getHashMap(ctx);

		
		for (String prio : sortedTasksByPrio.keySet()) {
			ArrayList<Task> arrayList = sortedTasksByPrio.get(prio);
			this.log.info("Task List of this priority levle" + prio + " is : " + arrayList);
			this.log.info("Remember to set MHz as default Unit for Frequency in CoreType" + "\r");

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
						this.log.info("For TASK :" + taskName + " Its WCET in InstructionCycles is :" + taskWCET + ". BCET in InstructionCycles is :" + taskBCET);
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
						this.log.info("The total Blocking Time of Task '" + taskName +"' is :"+ blockingTime + " mS");
						this.log.info("Remember we use 'GB/s' as Unit for 'Data Rate' in MemoryType " + "\r");

						//Calculating WCRT of the task						
						//Find, if this task has highest Priority in its Core
						Scheduler scheduler = getTaskScheduler(task, ctx);
						//this.log.info("The Scheduler of specified Task : '" + taskName + "' is :" + scheduler + "\r");
						//find the task list, where all task are scheduled by the same Scheduler as the specified task
						//ArrayList<Task> taskListBySched = getTaskWCRT(task, ctx, sortedTasksBySched, sortedTasksByPrio);
						//this.log.info(taskListBySched);
						String str1 = "5";
						String str2 = "7";
						int a = str1.compareTo(str2);
						this.log.info("The result is :" + a );
						double preemptionTime = getTaskWCRT(task, ctx, sortedTasksBySched);
						this.log.info("Preemption Time is :" + preemptionTime);
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
		EList<Task> taskList = getTasks(ctx);
		this.log.info("Task List without sorting:" + taskList.toString());
						
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
		return sortedTasksByPrio;
	}
	
	// get TaskList according to Scheduler, if tasks are allocated to the same Scheduler, then they are set in a list
	private HashMap<String, ArrayList<Task>> getHashMap(Context ctx) {
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
		return sortedTaskBySchduler;
		
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
		HwSystem hwSystem = getHwSystem(ctx);
		
		EList<Memory> memoryList = hwSystem.getMemories();
		if (memoryList == null) {
			this.log.info("Error: There is no Memory in HWModel!");
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

		this.log.info("Starting to read AMALTHEA model...");

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
		
		String schedulerString = getTaskScheduler(task, ctx).toString();
		ArrayList<Task> taskListBySched = sortedTasksByScheduler.get(schedulerString);
		String taskPriorityValue = task.getCustomProperties().get("priority").toString();
		double totalPreemptionTime = 0;
		double wcetHigherPrior = 0;
		
		for (Task eachtask : taskListBySched) {
			String prioValue = eachtask.getCustomProperties().get("priority").toString();
			
			//字典顺序，"1">""2">"3">"4".....
			//但是实际上，通过下面的比较器，"1"-"2" = -1,务必要注意
			//Str1.compareTo(Str2)返回Str1-Str2的值，且结果为int类型
			int diff = prioValue.compareTo(taskPriorityValue);
			if (diff < 0) {
				long wcetICHigherPrior = getWCETinIC(ctx, eachtask);
				wcetHigherPrior = runUnitConversion(ctx, wcetICHigherPrior);
				totalPreemptionTime += wcetHigherPrior; 

			}
		}
		return totalPreemptionTime;
		
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
	
	
}
