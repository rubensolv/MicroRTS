/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.Runner;

/**
 *
 * @author rubens
 */
public class SettingsAlphaDSL {
    private static boolean mode_debug = false;
    //DEBUG SETTINGS
    private static final int DEBUG_NUMBER_ALPHADSL_ITERATIONS = 10;
    private static final int DEBUG_NUMBER_ROUNDS_ACTOR = 1;
    private static final int DEBUG_NUMBER_OPPONENTS_EXPLOITER = 1;
    private static final int DEBUG_NUMBER_SA_STEPS = 2;
    private static final float DEBUG_CURRENT_TEMPERATURE = 1.0f;
    private static final float DEBUG_FINAL_TEMPERATURE = 0.1f;
    private static final float DEBUG_ALPHA = 0.5f;
    private static final String DEBUG_MAP = "maps/8x8/basesWorkers8x8A.xml";
    private static final int DEBUG_ITERATIONS_SA_BEST_RESPONDE = 2;
    
    //RUN SETTINGS
    private static final int NUMBER_ALPHADSL_ITERATIONS = 2000;
    private static final int NUMBER_ROUNDS_ACTOR = 1;
    private static final int NUMBER_OPPONENTS_EXPLOITER = 1;
    private static final int NUMBER_SA_STEPS = 10;
    private static final float CURRENT_TEMPERATURE = 1.0f; //1.0f
    private static final float FINAL_TEMPERATURE = 0.1f;
    private static final float ALPHA = 0.01f;
    private static final boolean RUN_FULL_LEAGUE = true;
    private static final int ITERATIONS_SA_BEST_RESPONDE = 2;
   //private static final String MAP = "maps/battleMaps/Others/RangedHeavyMixed.xml";
   private static final String MAP = "maps/8x8/basesWorkers8x8A.xml";
   //private static final String MAP = "maps/16x16/basesWorkers16x16A.xml";
   //private static final String MAP = "maps/24x24/basesWorkers24x24A.xml";
   //new maps
   //private static final String MAP = "maps/barricades24x24.xml";
   //private static final String MAP = "maps/chambers32x32.xml";
   //private static final String MAP = "maps/DoubleGame24x24.xml";
   //private static final String MAP = "maps/itsNotSafe.xml";
   //private static final String MAP = "maps/letMeOut.xml";
   //private static final String MAP = "maps/NoWhereToRun9x8.xml";   
   
   private static final int WINDOWS_SIZE = 4;
   
   //OPTIONS for both types
   private static final String NOVELTY_METRIC = "default";
   //private static final String NOVELTY_METRIC = "base_novelty";
   //private static final String NOVELTY_METRIC = "symmetry";
   
   private static final boolean RESET_NOVELTY_TABLES = true;
   
   private static final Integer NOVELTY_SIZE = 3;
   
   private static final boolean APPLY_RULES_REMOVE = true;

    
    public static void setMode_debug(boolean mode_debug) {
        SettingsAlphaDSL.mode_debug = mode_debug;
    }

    public static boolean isMode_debug() {
        return mode_debug;
    }
    
    
    public static String get_map(){
        if (mode_debug) {
            return DEBUG_MAP;
        }
        return MAP;
    }
    
    public static float get_alpha(){
        if (mode_debug) {
            return DEBUG_ALPHA;
        }
        return ALPHA;
    }
    
    public static float get_final_temperature(){
        if (mode_debug) {
            return DEBUG_FINAL_TEMPERATURE;
        }
        return FINAL_TEMPERATURE;
    }
    
    public static float get_current_temperature(){
        if (mode_debug) {
            return DEBUG_CURRENT_TEMPERATURE;
        }
        return CURRENT_TEMPERATURE;
    }
    
    public static int get_number_sa_steps(){
        if (mode_debug) {
            return DEBUG_NUMBER_SA_STEPS;
        }
        return NUMBER_SA_STEPS;
    }
    
    public static int get_number_opponents_exploiter(){
        if (mode_debug) {
            return DEBUG_NUMBER_OPPONENTS_EXPLOITER;
        }
        return NUMBER_OPPONENTS_EXPLOITER;
    }

    public static int get_number_alphaDSL_iterations(){
            if (mode_debug) {
                return DEBUG_NUMBER_ALPHADSL_ITERATIONS;
            }
            return NUMBER_ALPHADSL_ITERATIONS;
    }
    
    public static int get_number_rounds_actor(){
        if (mode_debug) {
                return DEBUG_NUMBER_ROUNDS_ACTOR;
            }
            return NUMBER_ROUNDS_ACTOR;
    }

    public static boolean get_run_full_league() {
        return RUN_FULL_LEAGUE;
    }
    
    public static String get_novelty_metric(){
        return NOVELTY_METRIC;
    }
    
    public static boolean get_reset_novelty_table(){
        return RESET_NOVELTY_TABLES;
    }
    
    public static int get_novelty_size(){
        return NOVELTY_SIZE;
    }
    
    public static boolean get_apply_rules_remove(){
        return APPLY_RULES_REMOVE;
    }
    
    public static int get_qtd_iterations_for_SA(){
        if(mode_debug){
            return DEBUG_ITERATIONS_SA_BEST_RESPONDE;
        }
        return ITERATIONS_SA_BEST_RESPONDE;
    }
    
    public static int get_windows_size(){
        return WINDOWS_SIZE;
    }
}
