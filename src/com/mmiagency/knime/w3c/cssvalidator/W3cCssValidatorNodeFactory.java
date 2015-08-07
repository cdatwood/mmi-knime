package com.mmiagency.knime.w3c.cssvalidator;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "W3cCssValidatorNode" Node.
 * 
 *
 * @author MMI Agency
 */
public class W3cCssValidatorNodeFactory 
        extends NodeFactory<W3cCssValidatorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public W3cCssValidatorNodeModel createNodeModel() {
        return new W3cCssValidatorNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<W3cCssValidatorNodeModel> createNodeView(final int viewIndex,
            final W3cCssValidatorNodeModel nodeModel) {
        return new W3cCssValidatorNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new W3cCssValidatorNodeDialog();
    }

}

