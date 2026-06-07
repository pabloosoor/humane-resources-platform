package com.uda.hrplatform.dto;

import com.uda.hrplatform.model.BonusType;

public record CalculateBonusRequest(BonusType bonusType, String period) {}
