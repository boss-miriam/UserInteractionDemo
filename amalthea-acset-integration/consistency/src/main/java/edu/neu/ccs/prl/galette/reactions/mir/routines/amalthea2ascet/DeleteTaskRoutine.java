package edu.neu.ccs.prl.galette.reactions.mir.routines.amalthea2ascet;

import java.io.IOException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Extension;
import tools.vitruv.dsls.reactions.runtime.routines.AbstractRoutine;
import tools.vitruv.dsls.reactions.runtime.state.ReactionExecutionState;
import tools.vitruv.dsls.reactions.runtime.structure.CallHierarchyHaving;
import ecore.tools.vitruv.methodologisttemplate.model.amalthea.Task;
import ecore.tools.vitruv.methodologisttemplate.model.ascet.AscetTask;

@SuppressWarnings("all")
public class DeleteTaskRoutine extends AbstractRoutine {
  private InputValues inputValues;

  private Match.RetrievedValues retrievedValues;

  public class InputValues {
    public final Task task;

    public InputValues(final Task task) {
      this.task = task;
    }
  }

  private static class Match extends AbstractRoutine.Match {
    public class RetrievedValues {
      public final AscetTask ascettask;

      public RetrievedValues(final AscetTask ascettask) {
        this.ascettask = ascettask;
      }
    }

    public Match(final ReactionExecutionState reactionExecutionState) {
      super(reactionExecutionState);
    }

    public EObject getCorrepondenceSourceAscettask(final Task task) {
      return task;
    }

    public RetrievedValues match(final Task task) throws IOException {
      ecore.tools.vitruv.methodologisttemplate.model.ascet.AscetTask ascettask = getCorrespondingElement(
      	getCorrepondenceSourceAscettask(task), // correspondence source supplier
          ecore.tools.vitruv.methodologisttemplate.model.ascet.AscetTask.class,
      	null, // correspondence precondition checker
      	null, 
      	false // asserted
      );
      if (ascettask == null) {
      	return null;
      }
      return new edu.neu.ccs.prl.galette.reactions.mir.routines.amalthea2ascet.DeleteTaskRoutine.Match.RetrievedValues(ascettask);
    }
  }

  private static class Update extends AbstractRoutine.Update {
    public Update(final ReactionExecutionState reactionExecutionState) {
      super(reactionExecutionState);
    }

    public void updateModels(final Task task, final AscetTask ascettask, @Extension final Amalthea2ascetRoutinesFacade _routinesFacade) {
      if ((ascettask != null)) {
        this.removeObject(ascettask);
        this.removeCorrespondenceBetween(task, ascettask);
      }
    }
  }

  public DeleteTaskRoutine(final Amalthea2ascetRoutinesFacade routinesFacade, final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy, final Task task) {
    super(routinesFacade, reactionExecutionState, calledBy);
    this.inputValues = new InputValues(task);
  }

  protected boolean executeRoutine() throws IOException {
    if (getLogger().isTraceEnabled()) {
    	getLogger().trace("Called routine DeleteTaskRoutine with input:");
    	getLogger().trace("   inputValues.task: " + inputValues.task);
    }
    retrievedValues = new edu.neu.ccs.prl.galette.reactions.mir.routines.amalthea2ascet.DeleteTaskRoutine.Match(getExecutionState()).match(inputValues.task);
    if (retrievedValues == null) {
    	return false;
    }
    // This execution step is empty
    new edu.neu.ccs.prl.galette.reactions.mir.routines.amalthea2ascet.DeleteTaskRoutine.Update(getExecutionState()).updateModels(inputValues.task, retrievedValues.ascettask, getRoutinesFacade());
    return true;
  }
}
