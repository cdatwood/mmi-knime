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
public class W3cCssValidatorNodeNodeFactory 
        extends NodeFactory<W3cCssValidatorNodeNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public W3cCssValidatorNodeNodeModel createNodeModel() {
        return new W3cCssValidatorNodeNodeModel();
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
    public NodeView<W3cCssValidatorNodeNodeModel> createNodeView(final int viewIndex,
            final W3cCssValidatorNodeNodeModel nodeModel) {
        return new W3cCssValidatorNodeNodeView(nodeModel);
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
        return new W3cCssValidatorNodeNodeDialog();
    }

}

