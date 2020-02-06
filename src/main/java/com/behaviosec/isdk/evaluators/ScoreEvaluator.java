package com.behaviosec.isdk.evaluators;

import com.behaviosec.isdk.config.Constants;
import com.behaviosec.isdk.entities.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initial evaluators, seems like will require adapter to deal with the policies rather than scores
 */
public class ScoreEvaluator {
    private static final String TAG = ScoreEvaluator.class.getName();
    private final Logger logger = LoggerFactory.getLogger(TAG);
    private final Config config;

    /**
     * Configuration for the ScoreEvaluator.
     */
    public static class Config {
        /**
         * Minimum score to to accept
         *
         * @return the amount.
         */
        private double minScore = Constants.MIN_SCORE;
        public void setMinScore(int minScore) {
            this.minScore = minScore;
        }
        public double minScore() {
            return this.minScore;
        }

        /**
         * Minimum score to to accept
         *
         * @return the amount.
         */
        private double minConfidence = Constants.MIN_CONFIDENCE;
        public void setMinConfidence(int minConfidence) {
            this.minConfidence = minConfidence;
        }
        public double minConfidence() {
            return this.minConfidence;
        }

        /**
         * Maximum acceptable risk
         *
         * @return the amount.
         */
        private double maxRisk = Constants.MAX_RISK;

        public void setMaxRisk(int maxRisk) {
            this.maxRisk = maxRisk;
        }

        public double maxRisk() {
            return this.maxRisk;
        }

        /**
         * Allow users in training
         *
         * @return the amount.
         */
        private boolean allowInTraining = true;
        public void setAllowInTraining(boolean allowInTraining) {
            this.allowInTraining = allowInTraining;
        }
        boolean allowInTraining() {
            return this.allowInTraining;
        }
    }

    public ScoreEvaluator(ScoreEvaluator.Config config) {
        this.config = config;
    }
    public ScoreEvaluator() {
        this.config = new ScoreEvaluator.Config();
    }

    /**
     * Takes BehavioSecReport and evaluates towards set thresholds
     *
     * @param bhsReport BehavioSecReport
     * @return
     */
    public boolean evaluate(Report bhsReport) {
        if (!bhsReport.isTrained()  && config.allowInTraining()) {
            return true;
        }

        if (bhsReport.getScore() >= config.minScore() &&
            bhsReport.getConfidence() >= config.minConfidence() &&
            bhsReport.getRisk() <= config.maxRisk() ) {
            return true;
        } else {
            return false;
        }
    }
}
