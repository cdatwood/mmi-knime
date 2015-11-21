package com.pg.facebook.api.selectpost.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SelectPost" Node.
 * 
 *
 * @author P&G, eBusiness
 */
public class SelectPostNodeFactory 
        extends NodeFactory<SelectPostNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectPostNodeModel createNodeModel() {
        return new SelectPostNodeModel();
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
    public NodeView<SelectPostNodeModel> createNodeView(final int viewIndex,
            final SelectPostNodeModel nodeModel) {
        return new SelectPostNodeView(nodeModel);
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
        return new SelectPostNodeDialog();
    }

}

