package com.behaviosec.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class ConfigurationList {
	@Min(1)
	@Max(120)
	int minimumLoginScore;
	@Min(1)
	@Max(120)
	int maximumLoginRisk;
	@Min(1)
	@Max(120)
	int minimumStepUpScore;
	@Min(1)
	@Max(120)
	int maximumStepUpRisk;
	@Min(1)
	@Max(120)
	int minimumFormScore;
	@Min(1)
	@Max(120)
	int maximumFormRisk;
	@Min(1)
	@Max(120)
	int minimumSSOScore;
	@Min(1)
	@Max(120)
	int maximumSSORisk;
}
