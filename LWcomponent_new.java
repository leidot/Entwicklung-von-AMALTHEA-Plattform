package org.eclipse.app4mc.amalthea.example.workflow.components;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.CallGraph;
import org.eclipse.app4mc.amalthea.model.CallSequence;
import org.eclipse.app4mc.amalthea.model.CallSequenceItem;
import org.eclipse.app4mc.amalthea.model.GraphEntryBase;
import org.eclipse.app4mc.amalthea.model.InterProcessStimulus;
import org.eclipse.app4mc.amalthea.model.InterProcessTrigger;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.LabelAccess;
import org.eclipse.app4mc.amalthea.model.LabelAccessEnum;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.RunnableCall;
import org.eclipse.app4mc.amalthea.model.RunnableItem;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.TaskRunnableCall;
import org.eclipse.app4mc.amalthea.model.Value;
import org.eclipse.app4mc.amalthea.workflow.core.Context;
import org.eclipse.app4mc.amalthea.workflow.core.WorkflowComponent;
import org.eclipse.app4mc.amalthea.workflow.core.exception.WorkflowException;
import org.eclipse.emf.common.util.EList;

public class LWcomponent_new extends WorkflowComponent{
	
	@Override
	protected void runInternal(Context ctx) throws WorkflowException {
		// TODO Auto-generated method stub
		// some checking if sw model is available
		if (null == getAmaltheaModel(ctx).getSwModel()) {
			
			throw new WorkflowException("No proper SWModel available!");
		}
		this.log.info("Number of tasks in model: " + getAmaltheaModel(ctx).getSwModel().getTasks().size());
		this.log.info("Number of tasks in model: " + getAmaltheaModel(ctx).toString());
        //get TaskList in SWModel
		EList<Task> taskList = getTasks(ctx);
		this.log.info("taskList in model: " + taskList);
		//CallSequenceItem
		EList<CallSequenceItem> callSeq = getCallList(ctx);
		this.log.info("taskList in model: " + callSeq);
		//RunnableItem
		EList<RunnableItem> runnableItem = getRunnableItem(ctx);
		this.log.info("runnableitem in runnable: " + runnableItem);
		//LabelAccessMode
		String labelAccessMode = getlabelAccessMode(ctx) ;
		this.log.info("labe access mode in runnable: " + labelAccessMode);
	
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
		
	
		// Search CallSequence from each Task and cast every possible into CallSequence
		private EList<CallSequenceItem> getCallList(Context ctx) {

			//EList<Task> taskList = getTasks(ctx);

			/*
			this.log.info("Task List without sort:" + taskList.toString());

			SortedMap<String, ArrayList<Task>> sortedTasksByPrio = new TreeMap<>();
			CallSequence cseq = null;
			// for each task in taskList take the following operation, and we get a list of tasks for each level of "Priority"
			for (Task task : taskList) {
			    //We must use Value of Priority in form of String in Amalthea Model
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
					
			//Here we set up a LabelList,which consists of only "Write" type. Key is name of Label, Value is invocation amount of tasks.
					
			//This is main Process for If-Branch
			for (String prio : sortedTasksByPrio.keySet()) {
				ArrayList<Task> arrayList = sortedTasksByPrio.get(prio);
				for (Task task : arrayList) {
					String taskName = task.getName();
					EList<Stimulus> taskStimuliList = task.getStimuli();
					for (Stimulus stimulus : taskStimuliList) {
						if (PeriodicStimulus.class.isInstance(stimulus)) {
							PeriodicStimulus periodStimulus = (PeriodicStimulus) stimulus;
							processTaskPeriodicStimulus(prio, taskName, task, periodStimulus);
						} else if (InterProcessStimulus.class.isInstance(stimulus)) {
							InterProcessStimulus interProcStimulus = (InterProcessStimulus) stimulus;
							processTaskInterProcessStimulus(prio, taskName, task, stimulus);
						}
					}
				}
			}

			this.log.info("Task List WITH sort:" + sortedTasksByPrio.toString());
*/
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
		
		
				
		private void processTaskInterProcessStimulus(String prio, String taskName, Task task, Stimulus stimulus) {
			// TODO Auto-generated method stub
					
		}

		private void processTaskPeriodicStimulus(String prio, String taskName, Task task, PeriodicStimulus periodStimulus) {
			// TODO Auto-generated method stub
					
		}

		//通过这个函数我们得到每个CallSequence当中的RunnableList(faulse)
		//通过这个函数我们得到一个Runnable的RunnableItem
		private EList<RunnableItem> getRunnableItem (Context ctx){
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
		//get LabelAccessMode, exactly write or read or undefined
		private String getlabelAccessMode(Context ctx) {
			EList<RunnableItem> runnaItemList = getRunnableItem(ctx); 
			LabelAccessEnum accessMode = null;
			for(RunnableItem runnableItem : runnaItemList)
			if (LabelAccess.class.isInstance(runnableItem) ) {
				LabelAccess labelInRunnable = LabelAccess.class.cast(runnableItem);
				
				accessMode = labelInRunnable.getAccess();
			}
			return accessMode.getLiteral();
		} 

}
