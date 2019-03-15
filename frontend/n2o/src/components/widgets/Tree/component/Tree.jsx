import React, { Component } from 'react';
import TreeBase from 'rc-tree';
import { pick, isEqual, map, eq, difference, filter, isArray, isFunction, values } from 'lodash';
import { HotKeys } from 'react-hotkeys';
//components
import { BaseNode } from '../TreeNodes';
import Filter from './Filter';
import ExpandBtn from './ExpandBtn';
//fns
import {
  createTreeFn,
  takeKeysWhenSearching,
  keyDownAction,
  FILTER_MODE,
  animationTree
} from '../until';
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
      autoExpandParent: true,
      searchValue: '',
      checkedKeys: [],
      selectedKeys: [],
      searchKeys: []
    };

    this.elems = [];

    this.createTree = createTreeFn(BaseNode);

    this.onFilter = this.onFilter.bind(this);
    this.onExpand = this.onExpand.bind(this);
    this.onHideAllTreeItem = this.onHideAllTreeItem.bind(this);
    this.onShowAllTreeItem = this.onShowAllTreeItem.bind(this);
    this.renderSwitcherIcon = this.renderSwitcherIcon.bind(this);
    this.onKeyDown = this.onKeyDown.bind(this);
    this.onCheck = this.onCheck.bind(this);
    this.onSelect = this.onSelect.bind(this);
    this.prepareDataAndResolve = this.prepareDataAndResolve.bind(this);
    this.createSelectedKeys = this.createSelectedKeys.bind(this);
    this.selectedObjToTreeKeys = this.selectedObjToTreeKeys.bind(this);
  }

  componentDidUpdate(prevProps) {
    if (!isEqual(prevProps.resolveModel, this.props.resolveModel)) {
      this.createSelectedKeys();
    }
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
      autoExpandParent: true,
      searchKeys: expandedKeys,
      searchValue: value
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

  prepareDataAndResolve(keys) {
    const {
      onResolve,
      datasource,
      valueFieldId,
      multiselect,
      rowClick,
      onRowClickAction
    } = this.props;
    const value = filter(datasource, data => keys.includes(data[valueFieldId]));
    if (multiselect) {
      onResolve(value);
    } else {
      onResolve(value ? value[0] : null);
    }

    if (rowClick) onRowClickAction();
  }

  onSelect(keys, { nativeEvent }) {
    const { multiselect, hasCheckboxes } = this.props;
    const { selectedKeys } = this.state;

    if (multiselect && hasCheckboxes) {
      return false;
    }

    const multiOnlySelect = multiselect && !hasCheckboxes;

    let selectedKeysForResolve = null;

    if (multiOnlySelect && keys.length > 1) {
      if (nativeEvent.ctrlKey) {
        selectedKeysForResolve = keys;
      } else {
        selectedKeysForResolve = difference(keys, selectedKeys);
      }
    } else {
      selectedKeysForResolve = keys;
    }

    this.prepareDataAndResolve(selectedKeysForResolve);
  }

  onCheck(keys) {
    this.prepareDataAndResolve(keys);
  }

  onKeyDown(_, key) {
    const inState = pick(this.state, ['expandedKeys']);
    const inProps = pick(this.props, [
      'prefixCls',
      'valueFieldId',
      'parentFieldId',
      'datasource',
      'hasCheckboxes'
    ]);
    keyDownAction({
      key,
      treeRef: this.treeRef,
      ...inProps,
      ...inState
    });
  }

  selectedObjToTreeKeys() {
    const { resolveModel, valueFieldId } = this.props;

    if (isArray(resolveModel)) {
      return map(resolveModel, valueFieldId);
    }
    if (!resolveModel) {
      return [];
    }
    return resolveModel[valueFieldId];
  }

  createSelectedKeys() {
    const { hasCheckboxes, multiselect } = this.props;
    if (hasCheckboxes && multiselect) {
      this.setState({ selectedKeys: [], checkedKeys: this.selectedObjToTreeKeys() });
    } else {
      this.setState({ selectedKeys: this.selectedObjToTreeKeys(), checkedKeys: [] });
    }
  }

  animation;

  render() {
    const nodeProps = pick(this.props, TREE_NODE_PROPS);
    const treeOtherProps = pick(this.props, TREE_PROPS);

    const {
      expandedKeys,
      autoExpandParent,
      selectedKeys,
      checkedKeys,
      searchValue,
      searchKeys
    } = this.state;
    const { filter, expandBtn, datasource, hasCheckboxes, multiselect, prefixCls } = this.props;

    const checkable = hasCheckboxes && multiselect ? <CheckboxN2O inline /> : false;

    return (
      <div className={`${prefixCls}-wrapper`}>
        {filter && FILTER_MODE.includes(filter) && <Filter onFilter={this.onFilter} />}
        {expandBtn && (
          <ExpandBtn onShowAll={this.onShowAllTreeItem} onHideAll={this.onHideAllTreeItem} />
        )}
        <HotKeys
          className="hotkey"
          keyMap={{ events: values(KEY_CODES) }}
          handlers={{ events: this.onKeyDown }}
        >
          <TreeBase
            openAnimation={animationTree}
            ref={this.treeRef}
            treeData={this.createTree({ datasource, ...nodeProps, searchKeys, searchValue })}
            expandedKeys={expandedKeys}
            selectedKeys={selectedKeys}
            checkedKeys={checkedKeys}
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
      </div>
    );
  }
}

Tree.propTypes = propTypes;
Tree.defaultProps = defaultProps;

export default Tree;
