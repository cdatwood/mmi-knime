package com.mmiagency.knime.w3c.htmlvalidator;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "W3cHtmlValidatorNode" Node.
 * 
 *
 * @author Ed Ng
 */
public class W3cHtmlValidatorNodeFactory 
        extends NodeFactory<W3cHtmlValidatorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public W3cHtmlValidatorNodeModel createNodeModel() {
        return new W3cHtmlValidatorNodeModel();
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
    public NodeView<W3cHtmlValidatorNodeModel> createNodeView(final int viewIndex,
            final W3cHtmlValidatorNodeModel nodeModel) {
        return new W3cHtmlValidatorNodeView(nodeModel);
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
        return new W3cHtmlValidatorNodeDialog();
    }

}

