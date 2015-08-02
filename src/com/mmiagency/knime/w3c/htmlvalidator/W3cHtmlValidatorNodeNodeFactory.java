package com.mmiagency.knime.w3c.htmlvalidator;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "W3cHtmlValidatorNode" Node.
 * 
 *
 * @author MMI Agency
 */
public class W3cHtmlValidatorNodeNodeFactory 
        extends NodeFactory<W3cHtmlValidatorNodeNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public W3cHtmlValidatorNodeNodeModel createNodeModel() {
        return new W3cHtmlValidatorNodeNodeModel();
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
    public NodeView<W3cHtmlValidatorNodeNodeModel> createNodeView(final int viewIndex,
            final W3cHtmlValidatorNodeNodeModel nodeModel) {
        return new W3cHtmlValidatorNodeNodeView(nodeModel);
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
        return new W3cHtmlValidatorNodeNodeDialog();
    }

}

