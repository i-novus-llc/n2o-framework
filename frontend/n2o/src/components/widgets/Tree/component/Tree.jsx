import React, { Component, Fragment } from 'react';
import { findDOMNode } from 'react-dom';
import TreeBase from 'rc-tree';
import { pick, isEqual, map, eq } from 'lodash';
import { HotKeys } from 'react-hotkeys';
//components
import { BaseNode } from '../TreeNodes';
import Filter from './Filter';
import ExpandBtn from './ExpandBtn';
//fns
import { createTreeFn, takeKeysWhenSearching } from '../until';
import { propTypes, defaultProps, TREE_NODE_PROPS, TREE_PROPS } from './treeProps';
import Icon from '../../../snippets/Icon/Icon';
import CheckboxN2O from '../../../controls/Checkbox/CheckboxN2O';

import { KEY_CODES } from './constants';

class Tree extends Component {
  constructor(props) {
    super(props);

    this.treeRef = React.createRef();

    this.state = {
      expandedKeys: [],
      datasource: [],
      autoExpandParent: true,
      searchValue: '',
      selectedKeys: []
    };

    this.createTree = createTreeFn(BaseNode);
    this.onFilter = this.onFilter.bind(this);
    this.onExpand = this.onExpand.bind(this);
    this.onHideAllTreeItem = this.onHideAllTreeItem.bind(this);
    this.onShowAllTreeItem = this.onShowAllTreeItem.bind(this);
    this.renderSwitcherIcon = this.renderSwitcherIcon.bind(this);
    this.onKeyDown = this.onKeyDown.bind(this);
    this.onSelect = this.onSelect.bind(this);
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

  renderSwitcherIcon() {
    const { showLine } = this.props;
    if (!showLine) {
      return <Icon name="fa fa-chevron-right" />;
    }
    return <Icon name="fa fa-chevron-right" />;
  }

  onSelect(selectedKeys) {
    this.setState({ selectedKeys });
  }

  onKeyDown(_, key) {
    findDOMNode(this.treeRef.current);
    if (eq(key, KEY_CODES.KEY_DOWN)) {
    }
  }

  render() {
    const nodeProps = pick(this.props, TREE_NODE_PROPS);
    const treeOtherProps = pick(this.props, TREE_PROPS);

    const { expandedKeys, datasource, autoExpandParent, selectedKeys } = this.state;
    const { filter, expandBtn } = this.props;

    return (
      <Fragment>
        {filter && <Filter onFilter={this.onFilter} />}
        {expandBtn && (
          <ExpandBtn onShowAll={this.onShowAllTreeItem} onHideAll={this.onHideAllTreeItem} />
        )}
        <HotKeys
          keyMap={{ events: ['up', 'down', 'space', 'enter'] }}
          handlers={{ events: this.onKeyDown }}
        >
          <TreeBase
            ref={this.treeRef}
            treeData={this.createTree({ datasource, ...nodeProps })}
            expandedKeys={expandedKeys}
            selectedKeys={selectedKeys}
            onSelect={this.onSelect}
            checkable={<CheckboxN2O inline />}
            switcherIcon={this.renderSwitcherIcon}
            onExpand={this.onExpand}
            autoExpandParent={autoExpandParent}
            {...treeOtherProps}
          />
        </HotKeys>
      </Fragment>
    );
  }
}

Tree.propTypes = propTypes;
Tree.defaultProps = defaultProps;

export default Tree;
