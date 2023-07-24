import React, { ReactNode } from 'react'
import { TreeNode as TreeNodeBase } from 'rc-tree-select'

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
export function TreeNode({ options, disabled, key, format, children, ...rest }: Props) {
    return (
        <TreeNodeBase
            disabled={disabled}
            key={key}
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            value={(rest as any)[options.valueFieldId ? options.valueFieldId : options.id]}
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            title={(rest as any).formattedTitle || visiblePartPopup(rest, options)
            }
            isLeaf
        >
            {children}
        </TreeNodeBase>
    )
}

export type Options = {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    [key: string]: any,
    badgeColorFieldId?: string,
    badgeFieldId?: string,
    hasChildrenFieldId?: string,
    iconFieldId: string,
    imageFieldId: string,
    labelFieldId: string,
    parentFieldId?: string,
    valueFieldId?: string
}

type Props = {
    children?: ReactNode,
    disabled: boolean,
    format?: string,
    key: string,
    options: Options,
    rest?: object,
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
} as Props
