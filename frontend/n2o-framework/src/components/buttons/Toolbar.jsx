import React from 'react'
import PropTypes from 'prop-types'
import map from 'lodash/map'
import isUndefined from 'lodash/isUndefined'
import set from 'lodash/set'
import get from 'lodash/get'
import unset from 'lodash/unset'
import { ButtonToolbar, ButtonGroup } from 'reactstrap'
import classNames from 'classnames'

import { Factory } from '../../core/factory/Factory'
import { BUTTONS } from '../../core/factory/factoryLevels'

function Toolbar({ className, toolbar, entityKey, onClick, tooltipTriggerRef = null }) {
    const { className: toolbarClassName, style, id } = toolbar[0] || {}

    const handleClick = (e) => {
        e.stopPropagation()
        onClick()
    }

    const remapButtons = (buttonProps) => {
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

    const renderButtons = (buttonProps, i) => (
        <Factory
            key={getButtonKey(buttonProps, i)}
            level={BUTTONS}
            {...remapButtons(buttonProps)}
            entityKey={entityKey}
            tooltipTriggerRef={tooltipTriggerRef}
        />
    )

    const renderBtnGroup = (toolbarProps) => {
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

function getButtonKey(buttonProps, i) {
    const { id, label, hint } = buttonProps

    return `${id}-${label}-${hint}-${i}`
}

Toolbar.propTypes = {
    toolbar: PropTypes.array,
    entityKey: PropTypes.string,
    onClick: PropTypes.func,
    className: PropTypes.string,
    tooltipTriggerRef: PropTypes.func,
}

Toolbar.defaultProps = {
    toolbar: [],
    onClick: () => {},
}

export default Toolbar
