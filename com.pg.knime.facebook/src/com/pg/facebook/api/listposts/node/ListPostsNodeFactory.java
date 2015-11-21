package com.pg.facebook.api.listposts.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ListPosts" Node.
 * 
 *
 * @author P&G, eBusiness
 */
public class ListPostsNodeFactory 
        extends NodeFactory<ListPostsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ListPostsNodeModel createNodeModel() {
        return new ListPostsNodeModel();
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
    public NodeView<ListPostsNodeModel> createNodeView(final int viewIndex,
            final ListPostsNodeModel nodeModel) {
        return new ListPostsNodeView(nodeModel);
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
        return new ListPostsNodeDialog();
    }

}

