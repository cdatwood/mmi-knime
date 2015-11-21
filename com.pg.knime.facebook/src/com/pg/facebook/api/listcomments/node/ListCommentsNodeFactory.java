package com.pg.facebook.api.listcomments.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ListComments" Node.
 * 
 *
 * @author P&G, eBusiness
 */
public class ListCommentsNodeFactory 
        extends NodeFactory<ListCommentsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ListCommentsNodeModel createNodeModel() {
        return new ListCommentsNodeModel();
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
    public NodeView<ListCommentsNodeModel> createNodeView(final int viewIndex,
            final ListCommentsNodeModel nodeModel) {
        return new ListCommentsNodeView(nodeModel);
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
        return new ListCommentsNodeDialog();
    }

}

