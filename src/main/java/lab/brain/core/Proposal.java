package lab.brain.core;

import lab.brain.api.ActionIntent;

public record Proposal(ActionIntent intent, double score) {}
