package forward_IT;

import aStar.AStarProblem;
import aStar.AStarResolver;
import aStar.Operator;
import aStar_planning.forward.ForwardPlanningProblem;
import logic.Action;
import logic.Goal;
import logic.Situation;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import mock_blocks.ActionFactory;
import mock_blocks.GoalFactory;
import mock_blocks.SituationFactory;

import java.util.List;

public class BlocksWorld_Forward_IT {
    @Test
    public void shouldStackThreeConcreteBlocks(){
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.threeBlocks_ABC_stacked();
        List<Action> possibleActions = ActionFactory.allActionsInBlocksWorld();
        AStarProblem problem = new ForwardPlanningProblem(initialSituation,possibleActions, goal);

        AStarResolver resolver = new AStarResolver(problem);
        List<Operator> solutionPlan = resolver.findSolution();

        assertEquals(solutionPlan.size(),4,"Plan should have 4 actions");
        assertEquals(solutionPlan.get(0).getName(),"take");
        assertEquals(solutionPlan.get(1).getName(),"stack");
        assertEquals(solutionPlan.get(2).getName(),"take");
        assertEquals(solutionPlan.get(3).getName(),"stack");
    }

    @Test
    public void shouldReturnEmptyPlan() {
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.threeBlocksOnTable();
        List<Action> possibleActions = ActionFactory.allActionsInBlocksWorld();
        AStarProblem problem = new ForwardPlanningProblem(initialSituation,possibleActions, goal);

        AStarResolver resolver =new AStarResolver(problem);
        List<Operator> solutionPlan = resolver.findSolution();

        assertEquals(solutionPlan.size(), 0,"There should be no operators needed");
    }

    @Test
    public void shouldStackThreeAbstractBlocks(){
        Situation initialSituation = SituationFactory.threeBlocksOnTable();
        Goal goal = GoalFactory.anyThreeBlocks_stacked();
        List<Action> possibleActions = ActionFactory.allActionsInBlocksWorld();
        AStarProblem problem = new ForwardPlanningProblem(initialSituation,possibleActions, goal);

        AStarResolver resolver = new AStarResolver(problem);
        List<Operator> solutionPlan = resolver.findSolution();

        assertEquals(solutionPlan.size(),4,"Plan should have 4 actions");
        assertEquals(solutionPlan.get(0).getName(),"take");
        assertEquals(solutionPlan.get(1).getName(),"stack");
        assertEquals(solutionPlan.get(2).getName(),"take");
        assertEquals(solutionPlan.get(3).getName(),"stack");
    }
}
