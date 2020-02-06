package com.behaviosec.isdk.evaluators;

import com.behaviosec.isdk.config.Constants;
import com.behaviosec.isdk.entities.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooleanEvaluator {
    private static final String TAG = BooleanEvaluator.class.getName();
    private final Logger logger = LoggerFactory.getLogger(TAG);
    private final Config config;


    /**
     * Configuration for the BooleanEvaluator.
     */
    public static class Config {
        /**
         * Toggle Bot flagged profiles to evaluate to true
         * @return allow bot.
         */
        private boolean allowBot = Constants.ALLOW_BOT;
        public void setAllowBot(boolean allowBot) { this.allowBot = allowBot; }
        boolean allowBot() {
            return this.allowBot;
        }

        /**
         * Toggle Allow replay flagged profiles to evaluate to true
         * @return allow replay.
         */
        private boolean allowReplay = Constants.ALLOW_REPLAY;
        public void setAllowReplay(boolean allowReplay) { this.allowReplay = allowReplay; }
        boolean allowReplay() {
            return this.allowReplay;
        }

        /**
         * Toggle in Training flagged profiles to evaluate to true
         * @return allow in training.
         */
        private boolean allowInTraining = Constants.ALLOW_IN_TRAINING;
        public void setAllowInTraining(boolean allowInTraining) { this.allowInTraining = allowInTraining; }
        boolean allowInTraining() {
            return this.allowInTraining;
        }

        /**
         * Toggle Remote Access flagged profiles to evaluate to true
         *
         * @return allow remote access.
         */
        private boolean allowRemoteAccess = Constants.ALLOW_REMOTE_ACCESS;
        public void setAllowRemoteAccess(boolean allowRemoteAccess) { this.allowRemoteAccess = allowRemoteAccess;};
        boolean allowRemoteAccess() {
            return this.allowRemoteAccess;
        }

        /**
         * Toggle Tab anomaly flagged profiles to evaluate to true
         *
         * @return allow tab anomaly.
         */
        private boolean allowTabAnomaly = Constants.ALLOW_TAB_ANOMALY;
        public void setAllowTabAnomaly(boolean allowTabAnomaly) { this.allowTabAnomaly = allowTabAnomaly; }
        boolean allowTabAnomaly() {
            return this.allowTabAnomaly;
        }

        /**
         *  Toggle numpad anomaly flagged profiles to evaluate to true
         *
         * @return allow num pad anomaly.
         */
        private boolean allowNumpadAnomaly = Constants.ALLOW_NUMPAD_ANOMALY;
        public void setAllowNumpadAnomaly(boolean allowNumpadAnomaly) { this.allowNumpadAnomaly = allowNumpadAnomaly; }
        boolean allowNumpadAnomaly() {
            return this.allowNumpadAnomaly;
        }

        /**
         *  Toggle device changed flagged profiles to evaluate to true
         *
         * @return device changed.
         */
        private boolean allowDeviceChanged = Constants.ALLOW_DEVICE_CHANGE;
        public void setAllowDeviceChanged(boolean allowDeviceChanged) { this.allowDeviceChanged = allowDeviceChanged; }
        boolean allowDeviceChanged() {
            return this.allowDeviceChanged;
        }

    }

    public BooleanEvaluator(Config config){
        this.config = config;
    }
    public BooleanEvaluator(){
        this.config = new Config();
    }

    /**
     * Takes BehavioSecReport and evaluates towards set boolean values
     *
     * @param bhsReport BehavioSecReport
     * @return
     */
    public boolean evaluate(Report bhsReport){
        return !(bhsReport.isBot() && !config.allowBot() ||
                bhsReport.isRemoteAccess()  && !config.allowRemoteAccess() ||
                bhsReport.isReplay()  && !config.allowReplay() ||
                bhsReport.isTrained()  && !config.allowInTraining() ||
                bhsReport.isTabAnomaly()  && !config.allowTabAnomaly() ||
                bhsReport.isDeviceChanged()  && !config.allowDeviceChanged() ||
                bhsReport.isNumpadAnomaly()  && !config.allowNumpadAnomaly());
    }

}
