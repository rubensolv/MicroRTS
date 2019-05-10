package ai.ScriptsGenerator.TableGenerator;

import java.util.List;


public class Parameter {
	
	String parameterName;
	double superiorLimit;
	double inferiorLimit;
	List<String> discreteSpecificValues;

	public Parameter(String parameterName, Object superiorLimit, Object inferiorLimit, Object discreteSpecificValues)
	{
		this.parameterName=parameterName;
		this.superiorLimit=(double)superiorLimit;
		this.inferiorLimit=(double)inferiorLimit;
		this.discreteSpecificValues=(List<String>)discreteSpecificValues;
	}

	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * @return the superiorLimit
	 */
	public double getSuperiorLimit() {
		return superiorLimit;
	}

	/**
	 * @return the inferiorLimit
	 */
	public double getInferiorLimit() {
		return inferiorLimit;
	}

	/**
	 * @return the discreteSpecificValues
	 */
	public List<String> getDiscreteSpecificValues() {
		return discreteSpecificValues;
	}
}
