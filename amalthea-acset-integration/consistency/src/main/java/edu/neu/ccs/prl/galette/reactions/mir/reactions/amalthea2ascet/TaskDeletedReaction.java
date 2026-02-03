package edu.neu.ccs.prl.galette.reactions.mir.reactions.amalthea2ascet;

import java.util.function.Function;
import edu.neu.ccs.prl.galette.reactions.mir.routines.amalthea2ascet.Amalthea2ascetRoutinesFacade;
import org.eclipse.xtext.xbase.lib.Extension;
import tools.vitruv.change.atomic.EChange;
import tools.vitruv.change.atomic.eobject.DeleteEObject;
import tools.vitruv.dsls.reactions.runtime.reactions.AbstractReaction;
import tools.vitruv.dsls.reactions.runtime.routines.AbstractRoutine;
import tools.vitruv.dsls.reactions.runtime.routines.RoutinesFacade;
import tools.vitruv.dsls.reactions.runtime.state.ReactionExecutionState;
import ecore.tools.vitruv.methodologisttemplate.model.amalthea.Task;

@SuppressWarnings("all")
public class TaskDeletedReaction extends AbstractReaction {
  private DeleteEObject<Task> deleteChange;

  public TaskDeletedReaction(final Function<ReactionExecutionState, RoutinesFacade> routinesFacadeGenerator) {
    super(routinesFacadeGenerator);
  }

  private static class Call extends AbstractRoutine.Update {
    public Call(final ReactionExecutionState reactionExecutionState) {
      super(reactionExecutionState);
    }

    public void updateModels(final DeleteEObject deleteChange, final Task affectedEObject, @Extension final Amalthea2ascetRoutinesFacade _routinesFacade) {
      _routinesFacade.deleteTask(affectedEObject);
    }
  }

  public boolean isCurrentChangeMatchingTrigger(final EChange change) {
    if (!(change instanceof DeleteEObject<?>)) {
    	return false;
    }
    
    DeleteEObject<ecore.tools.vitruv.methodologisttemplate.model.amalthea.Task> _localTypedChange = (DeleteEObject<ecore.tools.vitruv.methodologisttemplate.model.amalthea.Task>) change;
    if (!(_localTypedChange.getAffectedElement() instanceof ecore.tools.vitruv.methodologisttemplate.model.amalthea.Task)) {
    	return false;
    }
    this.deleteChange = (DeleteEObject<ecore.tools.vitruv.methodologisttemplate.model.amalthea.Task>) change;
    return true;
  }

  public void executeReaction(final EChange change, final ReactionExecutionState executionState, final RoutinesFacade routinesFacadeUntyped) {
    Amalthea2ascetRoutinesFacade routinesFacade = (Amalthea2ascetRoutinesFacade)routinesFacadeUntyped;
    if (!isCurrentChangeMatchingTrigger(change)) {
    	return;
    }
    ecore.tools.vitruv.methodologisttemplate.model.amalthea.Task affectedEObject = (ecore.tools.vitruv.methodologisttemplate.model.amalthea.Task)deleteChange.getAffectedElement();
    if (getLogger().isTraceEnabled()) {
    	getLogger().trace("Passed complete precondition check of Reaction " + this.getClass().getName());
    }
    
    new edu.neu.ccs.prl.galette.reactions.mir.reactions.amalthea2ascet.TaskDeletedReaction.Call(executionState).updateModels(deleteChange, affectedEObject, routinesFacade);
  }
}
