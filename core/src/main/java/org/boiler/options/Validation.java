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
public class Validation {

    public enum OrderType { DEPTH_FIRST, BREADTH_FIRST }

    public static class ValidationAutomaticWorkflowElementOptions extends
            Common.BaseAutomaticWorkflowElementOptions
    {
        public OrderType validationOrder;
        public boolean UnknownNamespacesIsInvalid;
    }

}
