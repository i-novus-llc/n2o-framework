import React from 'react'
import map from 'lodash/map'
import isUndefined from 'lodash/isUndefined'
import set from 'lodash/set'
import get from 'lodash/get'
import unset from 'lodash/unset'
import { ButtonToolbar, ButtonGroup } from 'reactstrap'
import classNames from 'classnames'
import { Placement } from '@popperjs/core'

import { Factory } from '../../core/factory/Factory'
import { BUTTONS } from '../../core/factory/factoryLevels'
import { TooltipHocProps } from '../snippets/Tooltip/TooltipHOC'

interface Button {
    id: string
    label: string
    hint: string
    enabled?: boolean
    visible?: boolean
    subMenu?: Button[]
}

export type ToolbarProps = Array<{
    className?: string
    style?: React.CSSProperties
    id?: string
    buttons: Button[]
}>

export interface ToolbarMeta {
    toolbar?: {
        bottomLeft?: ToolbarProps
        bottomCenter?: ToolbarProps
        bottomRight?: ToolbarProps
    }
}

interface Props {
    className?: string
    toolbar?: ToolbarProps
    entityKey?: string
    onClick?(): void
    placement?: Placement
    hint?: TooltipHocProps['hint']
}

export const Toolbar = ({ className, entityKey, toolbar = [], onClick = () => {} }: Props) => {
    const { className: toolbarClassName, style, id } = toolbar[0] || {}

    const handleClick = (e: React.MouseEvent) => {
        e.stopPropagation()
        onClick()
    }

    const remapButtons = (buttonProps: Button) => {
        const newProps = { ...buttonProps }

        const subMenu = get(newProps, 'subMenu')
        const enabled = get(newProps, 'enabled')

        if (!isUndefined(enabled)) {
            set(newProps, 'disabled', !enabled)
            set(newProps, 'entityKey', entityKey)

            unset(newProps, 'enabled')
        }
        if (!isUndefined(subMenu)) {
            set(newProps, 'subMenu', map(subMenu, item => remapButtons(item)))
        }

        return newProps
    }

    const renderButtons = (buttonProps: Button, i: number) => (
        <Factory
            key={getButtonKey(buttonProps, i)}
            level={BUTTONS}
            {...remapButtons(buttonProps)}
            entityKey={entityKey}
        />
    )

    const renderBtnGroup = (toolbarProps: { buttons: Button[]; id?: string }) => {
        const { buttons } = toolbarProps

        return (
            <ButtonGroup
                key={toolbarProps.id || map(buttons, getButtonKey).join('_')}
            >
                {map(buttons, renderButtons)}
            </ButtonGroup>
        )
    }

    const idWithoutNumbers = id?.replace(/\d/g, '')
    const visibleButtons = toolbar.filter(({ buttons }) => buttons.some(({ visible }) => visible !== false))

    const toolbarClass = classNames(
        'buttons-toolbar',
        [`toolbar_placement_${idWithoutNumbers}`],
        className,
        toolbarClassName,
        {
            'single-button': visibleButtons.length === 1,
        },
    )

    return (
        <ButtonToolbar
            className={toolbarClass}
            onClick={handleClick}
            style={style}
        >
            {map(toolbar, renderBtnGroup)}
        </ButtonToolbar>
    )
}

function getButtonKey(buttonProps: Button, i: number) {
    const { id, label, hint } = buttonProps

    return `${id}-${label}-${hint}-${i}`
}

export default Toolbar
