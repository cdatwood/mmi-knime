package com.mmiagency.knime.w3c.cssvalidator;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "W3cCssValidatorNode" Node.
 * 
 *
 * @author MMI Agency
 */
public class W3cCssValidatorNodeView extends NodeView<W3cCssValidatorNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link W3cCssValidatorNodeModel})
     */
    protected W3cCssValidatorNodeView(final W3cCssValidatorNodeModel nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        W3cCssValidatorNodeModel nodeModel = 
            (W3cCssValidatorNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

