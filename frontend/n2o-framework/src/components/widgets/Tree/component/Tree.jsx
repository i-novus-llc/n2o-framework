import React, { Component } from 'react'
import TreeBase from 'rc-tree'
import pick from 'lodash/pick'
import isEqual from 'lodash/isEqual'
import map from 'lodash/map'
import difference from 'lodash/difference'
import filter from 'lodash/filter'
import isArray from 'lodash/isArray'
import values from 'lodash/values'
import { HotKeys } from 'react-hotkeys/cjs'

// components
import { BaseNode } from '../TreeNodes'
import {
    createTreeFn,
    takeKeysWhenSearching,
    customTreeActions,
    FILTER_MODE,
    animationTree,
    singleDoubleClickFilter,
} from '../until'
import Icon from '../../../snippets/Icon/Icon'
import CheckboxN2O from '../../../controls/Checkbox/CheckboxN2O'

import Filter from './Filter'
import ExpandBtn from './ExpandBtn'
// fns
import {
    propTypes,
    defaultProps,
    TREE_NODE_PROPS,
    TREE_PROPS,
} from './treeProps'
import { KEY_CODES } from './constants'

class Tree extends Component {
    constructor(props) {
        super(props)

        this.treeRef = React.createRef()

        this.state = {
            expandedKeys: [],
            autoExpandParent: true,
            searchValue: '',
            checkedKeys: [],
            selectedKeys: [],
            searchKeys: [],
        }

        this.elems = []

        this.createTree = createTreeFn(BaseNode)

        this.onFilter = this.onFilter.bind(this)
        this.onExpand = this.onExpand.bind(this)
        this.onHideAllTreeItem = this.onHideAllTreeItem.bind(this)
        this.onShowAllTreeItem = this.onShowAllTreeItem.bind(this)
        this.renderSwitcherIcon = this.renderSwitcherIcon.bind(this)
        this.onCustomActions = this.onCustomActions.bind(this)
        this.onCheck = this.onCheck.bind(this)
        this.onSelect = this.onSelect.bind(this)
        this.createSelectedKeys = this.createSelectedKeys.bind(this)
        this.selectedObjToTreeKeys = this.selectedObjToTreeKeys.bind(this)
        this.onDoubleClickHandler = this.onDoubleClickHandler.bind(this)
    }

    componentDidUpdate(prevProps) {
        if (!isEqual(prevProps.resolveModel, this.props.resolveModel)) {
            this.createSelectedKeys()
        }
    }

    onFilter(value) {
        const propsFromSearch = pick(this.props, [
            'labelFieldId',
            'filter',
            'valueFieldId',
            'datasource',
        ])
        const expandedKeys = takeKeysWhenSearching({
            value,
            ...propsFromSearch,
        })

        this.setState({
            expandedKeys,
            autoExpandParent: true,
            searchKeys: expandedKeys,
            searchValue: value,
        })
    }

    onExpand(expandedKeys) {
        this.setState({
            expandedKeys,
            autoExpandParent: false,
        })
    }

    onShowAllTreeItem() {
        const { valueFieldId, datasource } = this.props
        const filteredData = filter(datasource, item => !item.disabled)

        this.setState({
            expandedKeys: map(filteredData, valueFieldId),
            autoExpandParent: false,
        })
    }

    onHideAllTreeItem() {
        this.setState({
            expandedKeys: [],
        })
    }

    renderSwitcherIcon() {
        const { showLine } = this.props

        if (!showLine) {
            return (
                <div className="icon-wrapper">
                    <Icon className="switcher" name="fa fa-angle-right" />
                </div>
            )
        }

        return <CheckboxN2O inline />
    }

    onSelect(keys, { nativeEvent }) {
        const { multiselect, hasCheckboxes } = this.props
        const { selectedKeys } = this.state

        if (multiselect && hasCheckboxes) {
            return false
        }

        const multiOnlySelect = multiselect && !hasCheckboxes

        let selectedKeysForResolve = null

        if (multiOnlySelect && keys.length > 1) {
            if (nativeEvent.ctrlKey) {
                selectedKeysForResolve = keys
            } else {
                selectedKeysForResolve = difference(keys, selectedKeys)
            }
        } else {
            selectedKeysForResolve = keys
        }

        this.props.onResolve(selectedKeysForResolve)
    }

    onCheck(keys) {
        this.props.onResolve(keys)
    }

    onCustomActions(_, key) {
        const inState = pick(this.state, ['expandedKeys'])
        const inProps = pick(this.props, [
            'prefixCls',
            'valueFieldId',
            'parentFieldId',
            'datasource',
            'hasCheckboxes',
        ])

        customTreeActions({
            key,
            treeRef: this.treeRef,
            ...inProps,
            ...inState,
        })
    }

    selectedObjToTreeKeys() {
        const { resolveModel, valueFieldId } = this.props

        if (isArray(resolveModel)) {
            return map(resolveModel, valueFieldId)
        }
        if (!resolveModel) {
            return []
        }

        return [resolveModel[valueFieldId]]
    }

    createSelectedKeys() {
        const { hasCheckboxes, multiselect } = this.props

        if (hasCheckboxes && multiselect) {
            this.setState({
                selectedKeys: [],
                checkedKeys: this.selectedObjToTreeKeys(),
            })
        } else {
            this.setState({
                selectedKeys: this.selectedObjToTreeKeys(),
                checkedKeys: [],
            })
        }
    }

    onDoubleClickHandler() {
        this.onCustomActions(null, 'DB_CLICK')
    }

    //
    // onDrop(info) {
    //   const dropKey = info.node.props.eventKey;
    //   const dragKey = info.dragNode.props.eventKey;
    //   const dropPos = info.node.props.pos.split('-');
    //   const dropPosition = info.dropPosition - Number(dropPos[dropPos.length - 1]);
    //
    //   this.props.onDrop({ dragKey, dropKey, dropPosition });
    // }

    render() {
        const nodeProps = pick(this.props, TREE_NODE_PROPS)
        const treeOtherProps = pick(this.props, TREE_PROPS)

        const {
            expandedKeys,
            autoExpandParent,
            selectedKeys,
            checkedKeys,
            searchValue,
            searchKeys,
        } = this.state
        const {
            filter,
            expandBtn,
            datasource,
            hasCheckboxes,
            multiselect,
            prefixCls,
            filterPlaceholder,
        } = this.props

        const checkable =
      hasCheckboxes && multiselect ? <CheckboxN2O inline /> : false

        return (
            <div className={`${prefixCls}-wrapper pt-4`}>
                {filter && FILTER_MODE.includes(filter) && (
                    <Filter
                        onFilter={this.onFilter}
                        filterPlaceholder={filterPlaceholder}
                    />
                )}
                {expandBtn && (
                    <ExpandBtn
                        onShowAll={this.onShowAllTreeItem}
                        onHideAll={this.onHideAllTreeItem}
                    />
                )}
                <HotKeys
                    className="hotkey"
                    keyMap={{ events: values(KEY_CODES) }}
                    handlers={{ events: this.onCustomActions }}
                >
                    <div tabIndex={1}>
                        <TreeBase
                            openAnimation={animationTree}
                            ref={this.treeRef}
                            treeData={this.createTree({
                                datasource,
                                ...nodeProps,
                                searchKeys,
                                searchValue,
                            })}
                            expandedKeys={expandedKeys}
                            selectedKeys={selectedKeys}
                            checkedKeys={checkedKeys}
                            onCheck={this.onCheck}
                            onSelect={singleDoubleClickFilter(this.onSelect, null, 200)}
                            onDoubleClick={this.onDoubleClickHandler}
                            multiple={multiselect}
                            onDragEnter={this.onDragEnter}
                            checkable={checkable}
                            switcherIcon={this.renderSwitcherIcon}
                            onExpand={this.onExpand}
                            autoExpandParent={autoExpandParent}
                            {...treeOtherProps}
                        />
                    </div>
                </HotKeys>
            </div>
        )
    }
}

Tree.propTypes = propTypes
Tree.defaultProps = defaultProps

export default Tree
