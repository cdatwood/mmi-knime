package com.pg.facebook.api.selectaccount.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SelectAccount" Node.
 * 
 *
 * @author P&G, eBusiness
 */
public class SelectAccountNodeFactory 
        extends NodeFactory<SelectAccountNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectAccountNodeModel createNodeModel() {
        return new SelectAccountNodeModel();
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
    public NodeView<SelectAccountNodeModel> createNodeView(final int viewIndex,
            final SelectAccountNodeModel nodeModel) {
        return new SelectAccountNodeView(nodeModel);
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
        return new SelectAccountNodeDialog();
    }

}

