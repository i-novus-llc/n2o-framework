import React, { useContext, useMemo } from 'react'
import PropTypes from 'prop-types'

import { TooltipHOC } from '../snippets/Tooltip/TooltipHOC'
import { FactoryContext } from '../../core/factory/context'
import { OVERLAYS } from '../../core/factory/factoryLevels'
import { ConfirmMode } from '../overlays/Confirm'

function ActionButtonBody({
    Component,
    componentProps,
    url,
    componentDisabled,
    componentVisible,
    onClick,
    id,
    confirm,
}) {
    const { getComponent } = useContext(FactoryContext)
    const { ConfirmDialog, ConfirmPopover } = useMemo(() => ({
        ConfirmDialog: getComponent('ConfirmDialog', OVERLAYS),
        ConfirmPopover: getComponent('ConfirmPopover', OVERLAYS),
    }), [getComponent])

    const confirmComponent = useMemo(() => {
        if (!confirm) { return null }

        const { mode } = confirm

        if (mode === ConfirmMode.MODAL) {
            return <ConfirmDialog {...confirm} />
        }
        if (mode === ConfirmMode.POPOVER) {
            return <ConfirmPopover {...confirm} />
        }

        return null
    }, [ConfirmDialog, ConfirmPopover, confirm])

    return (
        <>
            <Component
                {...componentProps}
                url={url}
                disabled={componentDisabled}
                visible={componentVisible}
                onClick={onClick}
                id={id}
            />
            {confirmComponent}
        </>
    )
}

ActionButtonBody.propTypes = {
    Component: PropTypes.node,
    componentProps: PropTypes.object,
    url: PropTypes.string,
    componentDisabled: PropTypes.bool,
    componentVisible: PropTypes.bool,
    onClick: PropTypes.func,
    id: PropTypes.string,
    confirm: PropTypes.object,
    close: PropTypes.func,
}

export const ActionButton = TooltipHOC(ActionButtonBody)
