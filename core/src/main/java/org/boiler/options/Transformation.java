/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boiler.options;

/**
 *
 * @author Victor Porton
 */
public class Transformation {

    public enum NotInTargetNamespace { IGNORE, REMOVE, ERROR }

    public static class TransformationAutomaticWorkflowElementOptions extends
            Common.BaseAutomaticWorkflowElementOptions
    {
        public NotInTargetNamespace notInTargetNamespace;
    }

}
