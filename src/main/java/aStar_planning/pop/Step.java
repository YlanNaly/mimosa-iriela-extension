package aStar_planning.pop;

import logic.Action;
import logic.ActionConsequence;
import logic.ActionPrecondition;
import logic.Atom;
import logic.CodenotationConstraints;
import logic.Context;
import logic.ContextualAtom;
import logic.LogicalInstance;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.function.Predicate;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Step implements PlanElement {
    private LogicalInstance actionInstance;

    /**
     * A shortcut to access the step's preconditions
     * @return the set of preconditions of the step
     */
    public ActionPrecondition getActionPreconditions(){
        Action action = (Action) this.actionInstance.getLogicalEntity();

        return action.getPreconditions();
    }

    /**
     * A shortcut to access the step's consequences.
     * @return the set of consequences of the step
     */
    public ActionConsequence getActionConsequences(){
        Action action = (Action) this.actionInstance.getLogicalEntity();

        return action.getConsequences();
    }

    /**
     * Checks if the current step makes the given proposition necessarily true in its preceding
     * situation.
     * @param proposition : the proposition to check if it is asserted by the current step or not
     * @param codenotationConstraints : codenotations constraints describing variable bindings
     * @return
     */
    public boolean asserts(ContextualAtom proposition,
                           CodenotationConstraints codenotationConstraints)
    {
        for (Atom consequence : this.getActionPreconditions().getAtoms()) {
            ContextualAtom consequenceInstance = new ContextualAtom(
                    this.getActionInstance().getContext(), consequence);

            if(assertsProposition(consequenceInstance, proposition, codenotationConstraints)){
                return true;
            }
        }

        return false;
    }

    private boolean assertsProposition(ContextualAtom proposition, ContextualAtom otherProposition,
                                       CodenotationConstraints codenotationConstraints){
        CodenotationConstraints temp = codenotationConstraints.copy();

        return proposition.getAtom().isNegation() == otherProposition.getAtom().isNegation()
                && otherProposition.getAtom().getPredicate().unify(
                        this.getActionInstance().getContext(),
                        proposition.getAtom().getPredicate(),
                        proposition.getContext(),
                        temp
                );
    }

    /**
     * Checks if the current step is threatening the precondition(s) of another step
     * TODO : make it work with codenotations constraints instead
     * @param step
     * @return
     */
    public boolean isThreatening(Step step) {
        for (Atom consequence : this.getActionConsequences().getAtoms()) {
            List<Atom> destroyedPreconditions = step.getActionPreconditions().getAtoms()
                    .stream()
                    .filter(precondition -> precondition.getPredicate()
                            .sameName(consequence.getPredicate()))
                    .filter(precondition -> precondition.isNegation() != consequence.isNegation()
                        && consequence.getPredicate().unify(
                            this.getActionInstance().getContext(),
                            precondition.getPredicate(),
                            step.getActionInstance().getContext()
                        )
                    )
                    .toList();

            if (destroyedPreconditions.size() > 0){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the current step destroys the precondition of another step, according to the
     * current bindings in the codenotation constraints
     * @param context
     * @param precondition
     * @param codenotationConstraints
     * @return
     */
    public boolean destroys(Context context, Atom precondition, CodenotationConstraints
                            codenotationConstraints)
    {
        ContextualAtom proposition = new ContextualAtom(context, precondition);

        for (Atom consequence : this.getActionConsequences().getAtoms()) {
            ContextualAtom consequenceInstance = new ContextualAtom(
                    this.getActionInstance().getContext(), consequence);

            if(destroys(consequenceInstance, proposition, codenotationConstraints)){
                return true;
            }
        }

        return false;
    }

    public boolean destroys(ContextualAtom consequence, ContextualAtom otherProposition,
                                       CodenotationConstraints codenotationConstraints){
        CodenotationConstraints temp = codenotationConstraints.copy();

        return consequence.getAtom().isNegation() && !otherProposition.getAtom().isNegation()
                && otherProposition.getAtom().getPredicate().unify(
                    this.getActionInstance().getContext(),
                    consequence.getAtom().getPredicate(),
                    consequence.getContext(),
                    temp
                );
    }

    @Override
    public String toString() {
        return this.getActionInstance()
                .getLogicalEntity()
                .build(this.getActionInstance().getContext())
                .toString();
    }

    public CodenotationConstraints getAssertingCodenotations(ContextualAtom toAssert) {
        return null;
    }
}
