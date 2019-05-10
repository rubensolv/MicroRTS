package ai.ScriptsGenerator.TableGenerator;

import java.util.List;


public class Parameter {
	
	String parameterName;
	Double superiorLimit;
	Double inferiorLimit;
	List<String> discreteSpecificValues;

	public Parameter(String parameterName, Object superiorLimit, Object inferiorLimit, Object discreteSpecificValues)
	{
		this.parameterName=parameterName;
                if(superiorLimit != null){
                    this.superiorLimit=(double)superiorLimit;
                }else{
                    this.superiorLimit = null;
                }
                if(inferiorLimit != null){
                    this.inferiorLimit=(double)inferiorLimit;
                }else{
                    this.inferiorLimit = null;
                }
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
