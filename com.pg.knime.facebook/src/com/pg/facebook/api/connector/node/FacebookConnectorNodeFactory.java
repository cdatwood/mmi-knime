package com.pg.facebook.api.connector.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "FacebookConnector" Node.
 * 
 *
 * @author P&G, eBusiness Analytics
 */
public class FacebookConnectorNodeFactory 
        extends NodeFactory<FacebookConnectorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FacebookConnectorNodeModel createNodeModel() {
        return new FacebookConnectorNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<FacebookConnectorNodeModel> createNodeView(final int viewIndex,
            final FacebookConnectorNodeModel nodeModel) {
        return new FacebookConnectorNodeView(nodeModel);
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
        return new FacebookConnectorNodeDialog();
    }

}

