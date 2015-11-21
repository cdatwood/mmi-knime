package com.pg.facebook.api.listaccounts.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ListAccounts" Node.
 * 
 *
 * @author 
 */
public class ListAccountsNodeFactory 
        extends NodeFactory<ListAccountsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ListAccountsNodeModel createNodeModel() {
        return new ListAccountsNodeModel();
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
    public NodeView<ListAccountsNodeModel> createNodeView(final int viewIndex,
            final ListAccountsNodeModel nodeModel) {
        return new ListAccountsNodeView(nodeModel);
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
        return new ListAccountsNodeDialog();
    }

}

