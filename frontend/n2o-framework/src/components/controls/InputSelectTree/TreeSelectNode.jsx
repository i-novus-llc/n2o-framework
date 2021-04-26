import React from 'react'
import { TreeNode as TreeNodeBase } from 'rc-tree-select'
import PropTypes from 'prop-types'

import propsResolver from '../../../utils/propsResolver'

import { visiblePartPopup } from './until'

/**
 * Компонент TreeNode
 * @param {object} options
 * @param {boolean} disabled
 * @param {string} key
 * @param {string} format
 * @param {node} children
 * @param {object} rest
 * @returns {*}
 * @constructor
 */
function TreeNode({ options, disabled, key, format, children, ...rest }) {
    return (
        <TreeNodeBase
            disabled={disabled}
            key={key}
            value={rest[options.valueFieldId]}
            title={
                format
                    ? propsResolver({ format }, rest).format
                    : visiblePartPopup(rest, options)
            }
            isLeaf
        >
            {children}
        </TreeNodeBase>
    )
}

TreeNode.propTypes = {
    options: PropTypes.object,
    disabled: PropTypes.bool,
    key: PropTypes.string,
    format: PropTypes.string,
    children: PropTypes.node,
    rest: PropTypes.object,
}
TreeNode.defaultProps = {
    options: {
        parentFieldId: 'parentId',
        valueFieldId: 'id',
        labelFieldId: 'name',
        iconFieldId: 'icon',
        imageFieldId: 'image',
        badgeFieldId: 'badge',
        badgeColorFieldId: 'color',
        hasChildrenFieldId: 'hasChildren',
    },
    disabled: false,
    key: '',
}

export default TreeNode
