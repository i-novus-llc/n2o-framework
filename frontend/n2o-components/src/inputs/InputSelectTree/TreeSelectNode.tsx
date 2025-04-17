import React, { ReactNode } from 'react'
import { TreeNode as TreeNodeBase } from 'rc-tree-select'

import { visiblePartPopup } from './helpers'

export type Options = {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    [key: string]: any,
    parentFieldId: string,
    valueFieldId: string,
    labelFieldId: string,
    iconFieldId: string,
    imageFieldId: string,
    badgeFieldId: string,
    badgeColorFieldId: string,
    hasChildrenFieldId: string
}

export interface Props {
    children?: ReactNode
    disabled?: boolean
    format?: string
    key?: string
    options?: Options
    rest?: Record<string, unknown>
    [key: string]: unknown
}

const DEFAULT_OPTIONS: Options = {
    parentFieldId: 'parentId',
    valueFieldId: 'id',
    labelFieldId: 'name',
    iconFieldId: 'icon',
    imageFieldId: 'image',
    badgeFieldId: 'badge',
    badgeColorFieldId: 'color',
    hasChildrenFieldId: 'hasChildren',
}

/**
 * Компонент TreeNode
 * @param {object} options - параметры узла
 * @param disabled
 * @param key
 * @param format
 * @param children
 * @param rest
 */
export function TreeNode({
    options = DEFAULT_OPTIONS,
    disabled = false,
    key = '',
    format,
    children,
    ...rest
}: Props) {
    const valueFieldId = options.valueFieldId ?? 'id'
    const value = rest[valueFieldId] as string

    return (
        <TreeNodeBase
            disabled={disabled}
            key={key}
            value={value}
            title={rest.formattedTitle || visiblePartPopup(rest, options)}
            isLeaf
        >
            {children}
        </TreeNodeBase>
    )
}
