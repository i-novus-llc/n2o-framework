import React, { Component, RefObject } from 'react'
import TreeBase, { type TreeProps as TreeBaseProps } from 'rc-tree'
import pick from 'lodash/pick'
import isEqual from 'lodash/isEqual'
import map from 'lodash/map'
import difference from 'lodash/difference'
import filter from 'lodash/filter'
import isArray from 'lodash/isArray'
import values from 'lodash/values'
// @ts-ignore import from js file
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
} from '../helpers'
import { Icon } from '../../../snippets/Icon/Icon'
import { Checkbox } from '../../../controls/Checkbox/Checkbox'
import { DatasourceItem, KEY_CODES, TreeProps } from '../types'

import { Filter } from './Filter'
import { ExpandBtn } from './ExpandBtn'
import {
    defaultProps,
    TREE_NODE_PROPS,
    TREE_PROPS,
} from './treeProps'

interface TreeState {
    expandedKeys: string[]
    autoExpandParent: boolean
    searchValue: string
    checkedKeys: string[]
    selectedKeys: string[]
    searchKeys: string[]
}

// TODO legacy требуется рефакторинг
class Tree extends Component<TreeProps & TreeBaseProps, TreeState> {
    treeRef: RefObject<HTMLElement>

    elems: unknown[] // Замените any на конкретный тип, если возможно

    createTree: (props: TreeProps & TreeBaseProps) => DatasourceItem[]

    constructor(props: TreeProps) {
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
        this.createTree = createTreeFn(BaseNode as never)
    }

    componentDidUpdate(prevProps: TreeProps) {
        const { resolveModel } = this.props

        if (!isEqual(prevProps.resolveModel, resolveModel)) {
            this.createSelectedKeys()
        }
    }

    onFilter = (value: string) => {
        const propsFromSearch = pick(this.props, [
            'labelFieldId',
            'filter',
            'valueFieldId',
            'datasource',
        ])
        const expandedKeys = takeKeysWhenSearching({ value, ...propsFromSearch } as TreeProps)

        this.setState({
            expandedKeys,
            autoExpandParent: true,
            searchKeys: expandedKeys,
            searchValue: value,
        })
    }

    onExpand = (expandedKeys: string[]) => {
        this.setState({ expandedKeys, autoExpandParent: false })
    }

    onShowAllTreeItem = () => {
        const { valueFieldId, datasource } = this.props
        const filteredData = filter(datasource, item => !item.disabled)

        this.setState({
            expandedKeys: map(filteredData, valueFieldId),
            autoExpandParent: false,
        })
    }

    onHideAllTreeItem = () => {
        this.setState({
            expandedKeys: [],
        })
    }

    renderSwitcherIcon = () => {
        const { showLine } = this.props

        if (!showLine) {
            return (
                <div className="icon-wrapper">
                    <Icon className="switcher" name="fa fa-angle-right" />
                </div>
            )
        }

        return <Checkbox inline />
    }

    // eslint-disable-next-line consistent-return
    onSelect = (keys: string[], { nativeEvent }: { nativeEvent: MouseEvent }) => {
        const { multiselect, hasCheckboxes } = this.props
        const { selectedKeys } = this.state

        if (multiselect && hasCheckboxes) {
            return false
        }

        const multiOnlySelect = multiselect && !hasCheckboxes

        let selectedKeysForResolve: string[]

        if (multiOnlySelect && keys.length > 1) {
            if (nativeEvent.ctrlKey) {
                selectedKeysForResolve = keys
            } else {
                selectedKeysForResolve = difference(keys, selectedKeys)
            }
        } else {
            selectedKeysForResolve = keys
        }

        const { onResolve } = this.props

        onResolve(selectedKeysForResolve)
    }

    onCheck = (keys: string[]) => {
        const { onResolve } = this.props

        onResolve(keys)
    }

    onCustomActions = (_: unknown, key: string) => {
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

    selectedObjToTreeKeys = () => {
        const { resolveModel, valueFieldId } = this.props

        if (isArray(resolveModel)) { return map(resolveModel, valueFieldId) }
        if (!resolveModel) { return [] }

        return [resolveModel[valueFieldId]]
    }

    createSelectedKeys = () => {
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

    onDoubleClickHandler = () => { this.onCustomActions(null, 'DB_CLICK') }

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

        const checkable = hasCheckboxes && multiselect ? <Checkbox preventDefault inline /> : false

        return (
            <div className={`${prefixCls}-wrapper pt-4`}>
                {filter && FILTER_MODE.includes(filter) && (
                    <Filter onFilter={this.onFilter} filterPlaceholder={filterPlaceholder} />
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
                    tabindex={0}
                >
                    {/* @ts-ignore требуется рефакторинг */}
                    <TreeBase
                        openAnimation={animationTree}
                        ref={this.treeRef}
                        // @ts-ignore требуется рефакторинг
                        treeData={this.createTree({ datasource, ...nodeProps, searchKeys, searchValue })}
                        expandedKeys={expandedKeys}
                        selectedKeys={selectedKeys}
                        checkedKeys={checkedKeys}
                        onCheck={this.onCheck}
                        onSelect={singleDoubleClickFilter(this.onSelect, undefined, 200)}
                        onDoubleClick={this.onDoubleClickHandler}
                        multiple={multiselect}
                        checkable={checkable}
                        switcherIcon={this.renderSwitcherIcon}
                        onExpand={this.onExpand}
                        autoExpandParent={autoExpandParent}
                        {...treeOtherProps}
                    />
                </HotKeys>
            </div>
        )
    }
}

// @ts-ignore требуется рефакторинг
Tree.defaultProps = defaultProps

export default Tree
