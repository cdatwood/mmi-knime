package com.mmiagency.knime.twitter.trends;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "TwitterTrends" Node.
 * 
 *
 * @author Ed Ng
 */
public class TwitterTrendsNodeFactory 
        extends NodeFactory<TwitterTrendsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public TwitterTrendsNodeModel createNodeModel() {
        return new TwitterTrendsNodeModel();
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
    public NodeView<TwitterTrendsNodeModel> createNodeView(final int viewIndex,
            final TwitterTrendsNodeModel nodeModel) {
        return new TwitterTrendsNodeView(nodeModel);
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
        return new TwitterTrendsNodeDialog();
    }

}

