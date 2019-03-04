import React, { Component, Fragment } from 'react';
import TreeBase from 'rc-tree';
import { pick, isEqual, map, eq, difference } from 'lodash';
import { HotKeys } from 'react-hotkeys';
//components
import { BaseNode } from '../treeNodes';
import Filter from './Filter';
import ExpandBtn from './ExpandBtn';
//fns
import { createTreeFn, takeKeysWhenSearching, keyDownAction, FILTER_MODE } from '../until';
import { propTypes, defaultProps, TREE_NODE_PROPS, TREE_PROPS } from './treeProps';
import Icon from '../../../snippets/Icon/Icon';
import CheckboxN2O from '../../../controls/Checkbox/CheckboxN2O';

class Tree extends Component {
  constructor(props) {
    super(props);

    this.treeRef = React.createRef();

    this.state = {
      expandedKeys: [],
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
    this.onCheck = this.onCheck.bind(this);
    this.onSelect = this.onSelect.bind(this);
    this.elems = [];
  }

  onFilter(value) {
    const propsFromSearch = pick(this.props, [
      'labelFieldId',
      'filter',
      'valueFieldId',
      'datasource'
    ]);
    const expandedKeys = takeKeysWhenSearching({
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
    const { valueFieldId, datasource } = this.props;
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
      return <Icon className="switcher" name="fa fa-chevron-right" />;
    }
    return <CheckboxN2O inline />;
  }

  onSelect(keys, { nativeEvent }) {
    const { multiselect, hasCheckboxes } = this.props;
    const { selectedKeys } = this.state;

    if (multiselect && hasCheckboxes) {
      return false;
    }

    const multiOnlySelect = multiselect && !hasCheckboxes;

    if (multiOnlySelect && keys.length > 1) {
      if (nativeEvent.ctrlKey) {
        this.setState({ selectedKeys: keys });
      } else {
        this.setState({ selectedKeys: difference(keys, selectedKeys) });
      }
    } else {
      this.setState({ selectedKeys: keys });
    }
  }

  onCheck(keys) {
    // this.setState({ selectedKeys: keys });
  }

  onKeyDown(_, key) {
    const inState = pick(this.state, ['expandedKeys']);
    const inProps = pick(this.props, ['prefixCls', 'valueFieldId', 'parentFieldId', 'datasource']);
    keyDownAction({
      key,
      treeRef: this.treeRef,
      ...inProps,
      ...inState
    });
  }

  render() {
    const nodeProps = pick(this.props, TREE_NODE_PROPS);
    const treeOtherProps = pick(this.props, TREE_PROPS);

    const { expandedKeys, autoExpandParent, selectedKeys } = this.state;
    const { filter, expandBtn, datasource, hasCheckboxes, multiselect } = this.props;

    const checkable = hasCheckboxes && multiselect ? <CheckboxN2O inline /> : false;

    return (
      <Fragment>
        {filter && FILTER_MODE.includes(filter) && <Filter onFilter={this.onFilter} />}
        {expandBtn && (
          <ExpandBtn onShowAll={this.onShowAllTreeItem} onHideAll={this.onHideAllTreeItem} />
        )}
        <HotKeys
          className="hotkey"
          keyMap={{ events: ['up', 'down', 'space', 'enter', 'ctrl+enter'] }}
          handlers={{ events: this.onKeyDown }}
        >
          <TreeBase
            ref={this.treeRef}
            treeData={this.createTree({ datasource, ...nodeProps })}
            expandedKeys={expandedKeys}
            selectedKeys={selectedKeys}
            onCheck={this.onCheck}
            onSelect={this.onSelect}
            multiple={multiselect}
            checkable={checkable}
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
