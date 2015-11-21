package com.pg.facebook.api.insights.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Insights" Node.
 * 
 *
 * @author P&G, eBusiness
 */
public class InsightsNodeFactory 
        extends NodeFactory<InsightsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public InsightsNodeModel createNodeModel() {
        return new InsightsNodeModel();
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
    public NodeView<InsightsNodeModel> createNodeView(final int viewIndex,
            final InsightsNodeModel nodeModel) {
        return new InsightsNodeView(nodeModel);
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
        return new InsightsNodeDialog();
    }

}

