import React, { Component, Fragment } from 'react';
import TreeBase from 'rc-tree';
import { pick, isEqual, map } from 'lodash';
//components
import { BaseNode } from '../TreeNodes';
import Filter from './Filter';
import ExpandBtn from './ExpandBtn';
//fns
import { createTreeFn, takeKeysWhenSearching } from '../until';
import { propTypes, defaultProps, TREE_NODE_PROPS, TREE_PROPS } from './treeProps';
import 'rc-tree/assets/index.css';

class Tree extends Component {
  constructor(props) {
    super(props);

    this.state = {
      expandedKeys: [],
      datasource: [],
      autoExpandParent: true,
      searchValue: ''
    };

    this.createTree = createTreeFn(BaseNode);
    this.onFilter = this.onFilter.bind(this);
    this.onExpand = this.onExpand.bind(this);
    this.onHideAllTreeItem = this.onHideAllTreeItem.bind(this);
    this.onShowAllTreeItem = this.onShowAllTreeItem.bind(this);
  }

  static getDerivedStateFromProps(nextProps, prevState) {
    if (!isEqual(nextProps.datasource, prevState.datasource)) {
      return {
        datasource: nextProps.datasource
      };
    }
  }

  onFilter(value) {
    const propsFromSearch = pick(this.props, ['labelFieldId', 'filter', 'valueFieldId']);
    const { datasource } = this.state;
    const expandedKeys = takeKeysWhenSearching({
      datasource,
      value,
      ...propsFromSearch
    });

    this.setState({
      expandedKeys,
      autoExpandParent: true
    });
  }

  onExpand(expandedKeys) {
    this.setState({
      expandedKeys,
      autoExpandParent: false
    });
  }

  onShowAllTreeItem() {
    const { datasource } = this.state;
    const { valueFieldId } = this.props;
    this.setState({
      expandedKeys: map(datasource, valueFieldId)
    });
  }

  onHideAllTreeItem() {
    this.setState({
      expandedKeys: []
    });
  }

  render() {
    const nodeProps = pick(this.props, TREE_NODE_PROPS);
    const treeOtherProps = pick(this.props, TREE_PROPS);

    const { expandedKeys, datasource, autoExpandParent } = this.state;
    const { filter, expandBtn } = this.props;

    return (
      <Fragment>
        {filter && <Filter onFilter={this.onFilter} />}
        {expandBtn && (
          <ExpandBtn onShowAll={this.onShowAllTreeItem} onHideAll={this.onHideAllTreeItem} />
        )}
        <TreeBase
          treeData={this.createTree({ datasource, ...nodeProps })}
          expandedKeys={expandedKeys}
          onExpand={this.onExpand}
          autoExpandParent={autoExpandParent}
          {...treeOtherProps}
        />
      </Fragment>
    );
  }
}

Tree.propTypes = propTypes;
Tree.defaultProps = defaultProps;

export default Tree;
