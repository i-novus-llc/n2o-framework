import React from 'react'
import PropTypes from 'prop-types'
import DropdownItem from 'reactstrap/lib/DropdownItem'
import find from 'lodash/find'

import NothingFound from '../InputSelect/NothingFound'
import groupData from '../../../utils/groupData'

import NODE_SELECTED from './nodeSelected'
import TreeNode from './TreeNode'

/**
 * Компонент TreeItems
 * @reactProps {boolean} hasChildrenFiledId
 * @reactProps {string} labelFieldId
 * @reactProps {string} valueFieldId
 * @reactProps {string} iconFieldId
 * @reactProps {array} disabledValues
 * @reactProps {string} imageFieldId
 * @reactProps {boolean} hasCheckboxes
 * @reactProps {string} format
 * @reactProps {boolean} ajax
 * @reactProps {string} badgeFieldId
 * @reactProps {string} badgeColorFieldId
 * @reactProps {object} options
 * @reactProps {object} treeState
 * @reactProps {object} active
 * @reactProps {function} handleSelect
 * @reactProps {function} handleDelete
 * @reactProps {function} onExpandClick
 * @reactProps {string} parentFieldId
 * @reactProps {function} handleFocus
 * @reactProps {any} value
 * @reactProps {string} groupFieldId
 */
class TreeItems extends React.Component {
    render() {
        const {
            hasChildrenFieldId,
            labelFieldId,
            valueFieldId,
            iconFieldId,
            disabledValues,
            imageFieldId,
            hasCheckboxes,
            format,
            ajax,
            badgeFieldId,
            badgeColorFieldId,
            options,
            treeStates,
            active,
            handleSelect,
            handleDelete,
            onExpandClick,
            parentFieldId,
            handleFocus,
            value,
            groupFieldId,
        } = this.props

        const defaultItems = options => options.map(renderTreeNode)

        const groupedItems = (key, value) => (
            <>
                <DropdownItem key header>
                    {key}
                </DropdownItem>
                {defaultItems(value)}
                <DropdownItem key={`${key}2222`} divider />
            </>
        )

        const renderTreeItems = (options) => {
            if (groupFieldId) {
                const groupedData = groupData(options, groupFieldId)

                return Object.keys(groupedData).map(key => groupedItems(key, groupedData[key]))
            }

            return defaultItems(options)
        }

        const renderTreeNode = (item) => {
            const childs = options.filter(
                node => node[parentFieldId] === item[valueFieldId],
            )
            const itemState = treeStates[item[valueFieldId]]

            return (
                <TreeNode
                    key={item[valueFieldId]}
                    hasCheckboxes={hasCheckboxes}
                    imageFieldId={imageFieldId}
                    iconFieldId={iconFieldId}
                    labelFieldId={labelFieldId}
                    item={item}
                    selected={find(value, [valueFieldId, item[valueFieldId]])}
                    indeterminate={itemState.selected === NODE_SELECTED.PARTIALLY}
                    format={format}
                    onSelect={handleSelect}
                    onDelete={handleDelete}
                    ajax={ajax}
                    expanded={itemState.expanded}
                    hasChildren={itemState[hasChildrenFieldId]}
                    onExpandClick={onExpandClick}
                    ref={itemState.ref}
                    handleFocus={handleFocus}
                    disabled={disabledValues.find(
                        node => node[valueFieldId] === item[valueFieldId],
                    )}
                    active={active && active.id === item[valueFieldId]}
                    badgeFieldId={badgeFieldId}
                    badgeColorFieldId={badgeColorFieldId}
                >
                    {!!childs.length && childs.map(renderTreeNode)}
                </TreeNode>
            )
        }

        const rootNodes = options.filter(node => !node[parentFieldId])

        return (
            <>
                {rootNodes && renderTreeItems(rootNodes)}
                {!rootNodes.length && <NothingFound />}
            </>
        )
    }
}

TreeItems.propTypes = {
    hasChildrenFieldId: PropTypes.bool,
    options: PropTypes.array.isRequired,
    treeStates: PropTypes.object.isRequired,
    disabledValues: PropTypes.array,
    hasCheckboxes: PropTypes.bool,
    imageFieldId: PropTypes.string,
    iconFieldId: PropTypes.string,
    labelFieldId: PropTypes.string.isRequired,
    valueFieldId: PropTypes.string,
    badgeFieldId: PropTypes.string,
    badgeColorFieldId: PropTypes.string,
    format: PropTypes.string,
    handleSelect: PropTypes.func.isRequired,
    handleDelete: PropTypes.func.isRequired,
    ajax: PropTypes.bool,
    onExpandClick: PropTypes.func.isRequired,
    parentFieldId: PropTypes.string,
    handleFocus: PropTypes.func,
    value: PropTypes.any,
    active: PropTypes.object,
    groupFieldId: PropTypes.string,
}

export default TreeItems
