package burlap.behavior.singleagent.learning.modellearning.rmax;

import burlap.behavior.policy.Policy;
import burlap.behavior.policy.support.ActionProb;
import burlap.behavior.singleagent.learning.modellearning.KWIKModel;
import burlap.debugtools.RandomFactory;
import burlap.mdp.core.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.action.ActionType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James MacGlashan.
 */
public class UnmodeledFavoredPolicy implements Policy{

	protected Policy sourcePolicy;
	protected KWIKModel model;
	protected List<ActionType> allActionTypes;


	public UnmodeledFavoredPolicy(Policy sourcePolicy, KWIKModel model, List <ActionType> actionTypes){
		this.sourcePolicy = sourcePolicy;
		this.model = model;
		this.allActionTypes = actionTypes;
	}

	@Override
	public Action action(State s) {

		List<Action> unmodeled = KWIKModel.Helper.unmodeledActions(model, allActionTypes, s);

		if(!unmodeled.isEmpty()){
			return unmodeled.get(RandomFactory.getMapped(0).nextInt(unmodeled.size()));
		}

		return this.sourcePolicy.action(s);
	}

	@Override
	public List<ActionProb> policyDistribution(State s) {

		List<Action> unmodeled = KWIKModel.Helper.unmodeledActions(model, allActionTypes, s);

		if(!unmodeled.isEmpty()){
			List<ActionProb> aps = new ArrayList<ActionProb>(unmodeled.size());
			double p = 1./(double)unmodeled.size();
			for(Action ga : unmodeled){
				aps.add(new ActionProb(ga, p));
			}
			return aps;
		}

		return this.sourcePolicy.policyDistribution(s);
	}


	@Override
	public boolean definedFor(State s) {
		return true;
	}
}